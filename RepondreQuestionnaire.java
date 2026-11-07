import java.util.Scanner;
import java.util.Random;
import java.time.LocalDate;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import sources.*;

public class RepondreQuestionnaire{
	static Scanner sc = new Scanner(System.in);
	static Random rand = new Random();

	/**vide l'admin de commande
	 */
	public static void clear_display(){ 
	    String os = System.getProperty("os.name");
        try{
            if(os.startsWith("Windows")){
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }else{
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        }catch(Exception e){
            System.out.println("Error while clearing console: " + e.getMessage());
        } 
	}

	/**change l'ordre des questions aléatoirement
	 * @param questionnaire le questionnaire duquel changer l'ordre des questions
	 */
	public static void randomQuestions(Questionnaire questionnaire){
		int nbQuestions = questionnaire.get_nb_questions();
		for(int i = 0; i < nbQuestions; i++){
			int premiereQuestion = rand.nextInt(nbQuestions);
			int secondeQuestion = rand.nextInt(nbQuestions);
			questionnaire.permute_questions(premiereQuestion, secondeQuestion);
		}
	}

	/**change l'ordre des réponses aléatoirement
	 * @param questionnaire le questionnaire duquel changer l'ordre des réponses
	 */
	public static void randomReponses(Questionnaire questionnaire){
		for(int i = 0; i < questionnaire.get_nb_questions(); i++){
			int nbReponses = questionnaire.get_question(i).get_nb_reponses();
			if(nbReponses != -1){
				for(int j = 0; j < nbReponses; j++){
					boolean changementEffectue = true;
					int premiereReponse = rand.nextInt(nbReponses);
					int secondeReponse = rand.nextInt(nbReponses);
					if(premiereReponse == questionnaire.get_question(i).get_solution()){
						changementEffectue = questionnaire.get_question(i).set_solution("" + (secondeReponse+1));
					}else if(secondeReponse == questionnaire.get_question(i).get_solution()){
						changementEffectue = questionnaire.get_question(i).set_solution("" + (premiereReponse+1));
					}
					if(changementEffectue){
						questionnaire.get_question(i).permute_reponses(premiereReponse, secondeReponse);
					}
				}
			}
		}
	}

	/**affiche le questionnaire auquel l'utilisateur veut répondre
	 * @param questionnaire le questionnaire à poser à l'utilisateur, il est soumis et la date butoir n'est pas dépassée
	 */
	public static void repondreQuestionnaire(Questionnaire questionnaire){
		Questionnaire cloneQuestionnaire = questionnaire.clone(0);
		randomQuestions(cloneQuestionnaire);
		randomReponses(cloneQuestionnaire);
		String choix = "0";
		int turn = 0;
		int points = 0;
		while(turn < cloneQuestionnaire.get_nb_questions() && choix.equals("00") == false){
			clear_display();
			System.out.println("Question " + (turn + 1) + "/" + cloneQuestionnaire.get_nb_questions() + " :\n" + cloneQuestionnaire.getQuestionEtPropositions(turn));
			System.out.print("01 : Passer\n00 : Quitter\n\nVotre réponse : ");
			choix = sc.nextLine();
			if(choix.equals("01")){
				turn++;
				points += 2;
			}else{
				int pointTour = 1;
				while(choix.equals("00") == false && choix.equals("01") == false && pointTour == 1){
					pointTour = cloneQuestionnaire.get_question(turn).repondre(choix);
					if(pointTour == 1){
						System.out.print("La touche entrée n'est pas une touche valide.\nEssayer à nouveau : ");
						choix = sc.nextLine();
					}else{
						points += pointTour;
						turn++;
					}
				}
				if(choix.equals("01")){
					turn++;
					points += 2;
				}
			}
		}
		clear_display();
		if(choix.equals("00") == false){
			System.out.println("Fin du questionnaire. Votre score est de " + points + "/" + (5 * cloneQuestionnaire.get_nb_questions()) + "\n");
		}
	}

	/**ouvre et lis le fichier Nom_Questionnaire.txt
	 * @param noms_questionnaires tableau contenant les noms des questionnaires
	 */
	public static void ouvrir_noms_questionnaire(String[] noms_questionnaires){
		File questionnaire_dispo = new File("Questionnaires/Nom_Questionnaire.txt");
		try{
			FileReader lectureFichier = new FileReader(questionnaire_dispo);
			int caractereLu = lectureFichier.read();
			int i = 0;
			while(caractereLu != -1){
				if(caractereLu == ';'){
					i++;
				}else if(noms_questionnaires[i] == null){
					noms_questionnaires[i] = "" + (char) caractereLu;
				}else{
					noms_questionnaires[i] += (char) caractereLu;
				}
				caractereLu = lectureFichier.read();
			}
			lectureFichier.close();
		}catch(FileNotFoundException e){
			System.out.println("Le fichier " + questionnaire_dispo + " n'a pas pu être ouvert.");
		}catch(IOException e){
			System.out.println("Impossible de lire dans le fichier " + questionnaire_dispo);
		}	
	}

	public static void main(String[] args){
		clear_display();
		String[] noms_questionnaires = new String[10];
		ouvrir_noms_questionnaire(noms_questionnaires);
		int choix = -2;
		while(choix != -1){
			int i = 0;
			System.out.println("\tMenu\nQuestionnaires existant :");
			boolean aucun_questionnaire = noms_questionnaires[i] == null;
			if(aucun_questionnaire){
				System.out.println("Aucun questionnaire existant.\nImpossible de continuer.\n");
				choix = -1;
			}else{
				while(i < noms_questionnaires.length && noms_questionnaires[i] != null) {
					System.out.println("\t" + (1 + i) + " : " + noms_questionnaires[i]);
					i++;
				}
				choix = -2;
				System.out.println("\nVous gagnez 5 points par bonnes réponses, 2 si vous passez et 0 si vous répondez incorrectement.\n");
				System.out.print("Entrez le numéro du questionnaire auquel répondre (0 pour quitter) : ");
				choix = sc.nextInt() - 1;
				sc.nextLine();
				clear_display();
				if(choix >= 0 && choix < noms_questionnaires.length && noms_questionnaires[choix] != null){
					Questionnaire questionnaire = Questionnaire.lecture_Fichier("Questionnaires/" + noms_questionnaires[choix] + ".txt");
					if(questionnaire != null){
						if(questionnaire.get_date_soumission() == null){
							System.out.println("Le questionnaire choisi n'a pas été soumis, il est impossible d'y répondre\n");
						}else if(questionnaire.get_date_butoir().compareTo(LocalDate.now()) < 0){
							System.out.println("La date butoir du questionnaire est dépacée, il est impossible d'y répondre\n");
						}else{
							repondreQuestionnaire(questionnaire);
						}
					}
				}else if(choix != -1){
					System.out.println("Le nombre entré est invalide.\n");
				}
			}
		}
	}
}