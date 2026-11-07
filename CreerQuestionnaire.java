import java.util.Scanner;
import java.time.LocalDate;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import sources.*;

public class CreerQuestionnaire{
	static Scanner sc = new Scanner(System.in);

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
	
	/**sauvegarde le questionnaire dans un fichier .txt
	 * @param questionnaire questionnaire à sauvegarder
	 */
	public static void sauvegarder(Questionnaire questionnaire){
		File fichierASauver = new File("Questionnaires/" + questionnaire.get_nom() + ".txt");
		try {
			FileWriter ecrivainFichier = new FileWriter(fichierASauver);
			ecrivainFichier.write(questionnaire.toString());
			ecrivainFichier.close();
		}catch(FileNotFoundException e){
			System.out.println("Le fichier " + questionnaire.get_nom() + ".txt n'a pas pu être ouvert.");
		}catch(IOException e){
			System.out.println("Impossible de lire dans le fichier " + questionnaire.get_nom() + ".txt");
		}
	}



	/**demande à l'utilisateur le numéro de la réponse avec laquelle interagir (en lettre ou en chiffre) et vérifie si elle existe
	 * @param question la question dans laquelle sélectionner une réponse 
	 * @return le numéro de la réponse avec laquelle interagir ou -1 pour annuler
	 */
	public static int get_num_reponse(Question question){
		int num_reponse = -1;
		if(question.get_nb_reponses() == 0){
			clear_display();
			System.out.println("Aucune réponse présente dans cette question.\n");
		}else if(question.get_nb_reponses() == -1){
			clear_display();
			System.out.println("Les questions ouvertes n'ont pas de propositions de réponses\n");
		}else{
			do{
				System.out.print("Entrez le numéro de la réponse (0 pour annuler) : ");
				num_reponse =question.char_to_int(sc.nextLine().charAt(0));
				if(num_reponse < -1 || num_reponse > question.get_nb_reponses()) System.out.println("Le numéro entré est invalide");
			}while(num_reponse < -1 || num_reponse > question.get_nb_reponses());
			clear_display();
		}
		return num_reponse;
	}

	/**demande à l'utilisateur la question avec lequel interagir et vérifie si elle existe
	 * @param questionnaire le questionnaire dans lequel sélectionner une question 
	 * @return le numéro de la question avec laquelle interagir ou -1 pour annuler
	 */
	public static int get_num_question(Questionnaire questionnaire){
		if(questionnaire.get_nb_questions() == 0){
			clear_display();
			System.out.println("Aucune questions présente dans ce questionnaire.\n");
			return -1;
		}
		System.out.print("Entrez le numéro de la question (0 pour annuler) : ");
		int num_question = sc.nextInt() - 1;
		sc.nextLine();
		while(num_question < -1 || num_question >= questionnaire.get_nb_questions()){
			System.out.print("Le numéro entré est invalide\nEntrez le numéro de la question (0 pour annuler) : ");
			num_question = sc.nextInt() - 1;
			sc.nextLine();
		}
		clear_display();
		return num_question;
	}
	
	/**Ajoute une réponse à une question
	 * @param questionnaire le questionnaire dans lequel ajouter une réponse
	 * @param num_question_ajouter le numéro de la question
	 */
	public static void ajouterReponse(Questionnaire questionnaire, int num_question_ajouter){
		if(questionnaire.get_question(num_question_ajouter).get_nb_reponses() != -1){
			System.out.println(questionnaire.get_question(num_question_ajouter));
			System.out.print("Entrez la réponse a ajoutée (00 pour annuler) : ");
			String new_reponse = " " + sc.nextLine();
			if(new_reponse.equals(" 00") == false){
				boolean estAjouter = questionnaire.get_question(num_question_ajouter).ajoute_reponse(new_reponse);
				clear_display();
				if(estAjouter) {
					System.out.println("La réponse a bien été ajoutée.\n");
				}else {
					System.out.println("La réponse n'a pas pu être ajoutée.\n");
				}
			}
		}else{
			System.out.println("Les questions ouvertes n'ont pas de propositions de réponses\n");
		}
	}

	/**Modifie une réponse d'une question
	 * @param questionnaire le questionnaire dans lequel modifier une réponse
	 * @param num_question_modifier le numéro de la question
	 */
	public static void modifierReponse(Questionnaire questionnaire, int num_question_modifier){
		System.out.println(questionnaire.get_question(num_question_modifier));
		int num_reponse_modifier = get_num_reponse(questionnaire.get_question(num_question_modifier));
		if(num_reponse_modifier != -1){
			if(num_reponse_modifier == questionnaire.get_question(num_question_modifier).get_solution()){
				System.out.println("Vous ne pouvez pas modifier la solution à la question\n");
			}else{
				System.out.print("Entrez la nouvelle réponse (00 pour annuler) : ");
				String reponse_modifier = " " + sc.nextLine();
				if(reponse_modifier.equals(" 00") == false){
					boolean estModifier = questionnaire.get_question(num_question_modifier).set_reponse(reponse_modifier, num_reponse_modifier);
					clear_display();
					if(estModifier){
						System.out.println("La réponse a bien été modifiée.\n");
					}else{
						System.out.println("La réponse n'a pas pu être modifiée.\n");
					}
				}
			}
		}
	}

	/**Supprime une réponse d'une question
	 * @param questionnaire le questionnaire dans lequel supprimer une réponse
	 * @param num_question_supprimer le numéro de la question
	 */
	public static void supprimerReponse(Questionnaire questionnaire, int num_question_supprimer){
		System.out.println(questionnaire.get_question(num_question_supprimer));
		System.out.print("Entrez le numéro de la réponse à supprimer (0 pour annuler) : ");
		int num_reponse_supprimer = get_num_reponse(questionnaire.get_question(num_question_supprimer));
		if(num_reponse_supprimer != -1){
			if(num_reponse_supprimer == questionnaire.get_question(num_question_supprimer).get_solution()){
				System.out.println("Vous ne pouvez pas supprimer la solution à la question\n");
			}else{
				boolean estSupprimer = questionnaire.get_question(num_question_supprimer).supprime_reponse(num_reponse_supprimer);
				clear_display();
				if(estSupprimer){
					System.out.println("La réponse a bien été supprimée.\n");
				}else{
					System.out.println("La réponse n'a pas pu être supprimée.\n");
				}
			}
		}	
	}

	/**Modifie la solution d'une question
	 * @param questionnaire le questionnaire dans lequel modifier la solution
	 * @param num_question_modifier_solution le numéro de la question
	 */
	public static void modifierSolution(Questionnaire questionnaire, int num_question_modifier_solution){
		System.out.println(questionnaire.get_question(num_question_modifier_solution));
		System.out.print("Entrez le nouveau numéro de la solution à la question (00 pour annuler) : ");
		String new_soluce = sc.nextLine();
		if(new_soluce.equals("00") == false){
			boolean estNouvelleSoluce = questionnaire.get_question(num_question_modifier_solution).set_solution(new_soluce);
			clear_display();
			if(estNouvelleSoluce){
				System.out.println("La solution a bien été modifiée.\n\n");
			}else{
				System.out.println("La solution n'a pas pu être modifiée.\n");
			}
		}
	}

	/**Modifie la difficulté d'une question
	 * @param questionnaire le questionnaire dans lequel modifier la difficulté
	 * @param num_question_modifier_difficulte le numéro de la question
	 */
	public static void modifierDifficulte(Questionnaire questionnaire, int num_question_modifier_difficulte){
		System.out.println(questionnaire.get_question(num_question_modifier_difficulte));
		System.out.print("Entrez la nouvelle difficulté de la question (1, 2 ou 3) (0 pour annuler) : ");
		int new_difficulte = sc.nextInt() - 1;
		if(new_difficulte != -1){
			boolean estNouveauDiff = questionnaire.get_question(num_question_modifier_difficulte).set_difficulte(new_difficulte);
			clear_display();
			if(estNouveauDiff){
				System.out.println("La difficulté a bien été modifiée.\n");
			}else{
				System.out.println("La difficulté n'a pas pu être modifiée.\n");
			}
		}
	}

	/**Modifie l'énoncé d'une question
	 * @param questionnaire le questionnaire dans lequel modifier l'énoncé
	 * @param num_question_modifier_enonce le numéro de la question
	 */
	public static void modifierEnonce(Questionnaire questionnaire, int num_question_modifier_enonce){
		System.out.println(questionnaire.get_question(num_question_modifier_enonce));
		System.out.print("Entrez le nouveau énonce de la question (0 pour annuler) : ");
		String new_enonce = sc.nextLine();
		if(new_enonce.equals("0") == false){
			questionnaire.get_question(num_question_modifier_enonce).set_enonce(new_enonce);
			clear_display();
			System.out.println("L'énoncé a bien été modifié.\n");
		}	
	}

	/**affiche un menu proposant des modifications sur les questions du questionnaire et effectue ces modifications 
	 * @param questionnaire le questionnaire dans lequel modifier des questions 
	 */
	public static void modifier_question(Questionnaire questionnaire){
		int choice = 1;
		while(choice != 0){
			System.out.println(questionnaire);
			System.out.print("\n1 : Ajouter une réponse\n2 : Modifier une réponse\n3 : Supprimer une réponse\n4 : Modifier la solution\n5 : Modifier la difficulté\n6 : Modifier l'énoncé\n0 : Retour au questionnaire\n\nVotre choix : ");
			choice = sc.nextInt();
			sc.nextLine();
			if(choice != 0){
				if(choice > 0 && choice < 7){
					int numReponse = get_num_question(questionnaire);
					if(numReponse != -1){
						clear_display();
						switch(choice){
						case 1 :
							ajouterReponse(questionnaire, numReponse);
							break;
						case 2 :
							if(questionnaire.get_nb_questions() == 0){
								clear_display();
								System.out.println("Aucune questions présente dans ce questionnaire.\n");
							}else{
								modifierReponse(questionnaire, numReponse);
							}
							break;
						case 3 :
							if(questionnaire.get_nb_questions() == 0){
								clear_display();
								System.out.println("Aucune questions présente dans ce questionnaire.\n");
							}else{
								supprimerReponse(questionnaire, numReponse);
							}
							break;
						case 4 :
							if(questionnaire.get_nb_questions() == 0){
								clear_display();
								System.out.println("Aucune questions présente dans ce questionnaire.\n");
							}else{
								modifierSolution(questionnaire, numReponse);
							}
							break;
						case 5 :
							modifierDifficulte(questionnaire, numReponse);
							break;
						case 6 :
							modifierEnonce(questionnaire, numReponse);
							break;
						default :
							clear_display();
							break;
						}
					}
				}else{
					clear_display();
				}
			}
		}
		clear_display();
	}

	/**Ajoute une question au questionnaire
	 * @param questionnaire le questionnaire dans lequel ajouter une question
	 */
	public static void ajouterQuestion(Questionnaire questionnaire){
		if(questionnaire.get_date_soumission() == null){
			if(questionnaire.get_nb_questions() < questionnaire.get_nb_questions_max()){
				int format_question = -1;
				while(format_question < 0 || format_question > 2){
					clear_display();
					System.out.print("1 : Question ouverte (la réponse doit-être un entier)\n2 : Question QCM\n0 : Annuler\n\nVotre choix : ");
					format_question = sc.nextInt();
					sc.nextLine();
				}
				if(format_question != 0){
					System.out.print("\nEntrez l'énoncé de la question (0 pour annuler) : ");
					String ennonce_question = sc.nextLine();
					if(ennonce_question.equals("0") == false){
						boolean est_ajouter;
						if(format_question == 2){
							QuestionQCM nouvelle_question;
							int nombre_questions = 0;
							String[] reponses = new String[10];
							String phrase;
							do{
								System.out.print("\nEntrez la proposition n°" + (nombre_questions + 1) + " à la question (00 pour arreter) : ");
								phrase = " " + sc.nextLine();
								if(phrase.equals(" 00") == false && phrase.equals("\n") == false && phrase.equals(" ") == false){
									reponses[nombre_questions] = phrase;
									nombre_questions++;
								}
							}while(phrase.equals(" 00") == false);
							if(nombre_questions == 0 || nombre_questions == 1){
								nouvelle_question = new QuestionQCM(ennonce_question, reponses);
							}else{
								System.out.print("Quelle est le numéro de la réponse à la question ? ");
								int solution = sc.nextInt() - 1;
								sc.nextLine();
								System.out.print("Quelle est la difficulté de la question (1, 2 ou 3) ? ");
								int difficulte = sc.nextInt() - 1;
								nouvelle_question = new QuestionQCM(difficulte, ennonce_question, reponses, solution);
							}
							est_ajouter = questionnaire.ajoute_question(nouvelle_question);
						}else{
							QuestionOuverte nouvelle_question;
							System.out.print("Quelle est la réponse à la question (un entier) ? ");
							int solution = sc.nextInt();
							sc.nextLine();
							System.out.print("Quelle est la difficulté de la question (1, 2 ou 3) ? ");
							int difficulte = sc.nextInt() - 1;
							nouvelle_question = new QuestionOuverte(difficulte, ennonce_question, solution);
							est_ajouter = questionnaire.ajoute_question(nouvelle_question);
						}
						clear_display();
						if(est_ajouter) {
							System.out.println("La question a bien été ajoutée.\n");
						}else{
							System.out.println("La question n'a pas pu être ajoutée.\n");
						}
					}else{
						clear_display();
					}
				}else{
					clear_display();
				}
			}else{
				System.out.println("Le questionnaire a atteint la limite de questions disponibles.\n");
			}
		}else{
			System.out.println("Le questionnaire a déjà été soumis, vous ne pouvez plus ajouter de questions.\n");
		}
	}

	/**Supprime une question du questionnaire
	 * @param questionnaire le questionnaire dans lequel supprimer une question
	 */
	public static void supprimerQuestion(Questionnaire questionnaire){
		if(questionnaire.get_date_soumission() == null){
			int aSupprimer = get_num_question(questionnaire);
			if(aSupprimer != -1){
				boolean est_supprime = questionnaire.supprime_question(aSupprimer);
				if(est_supprime) {
					System.out.println("La question a bien été supprimée.\n");
				}else {
					System.out.println("La question n'a pas pu être supprimée.\n");
				}
			}
		}else{
			System.out.println("Le questionnaire ayant été soumis, vous ne pouvez plus supprimer de questions.\n");
		}
	}

	/**Modifie la date butoir du questionnaire
	 * @param questionnaire le questionnaire dans lequel modifier la date butoir
	 */
	public static void modifierDateButoir(Questionnaire questionnaire){
		if(questionnaire.get_date_butoir() == null || questionnaire.get_date_soumission() == null){
			clear_display();
			System.out.println("La date butoir ne peut-être modifiée.\n");
		}else{
			System.out.print("Entrez la nouvelle année de la date butoir (0 pour annuler) : ");
			int annee = sc.nextInt();
			if(annee != 0){
				System.out.print("Entrez le nouveau mois de la date butoir (en chiffre) (0 pour annuler) : ");
				int mois = sc.nextInt();
				if(mois != 0){
					System.out.print("Entrez le nouveau jour de la date butoir (en chiffre) (0 pour annuler) : ");
					int jour = sc.nextInt();
					if(jour != 0){
						clear_display();
						boolean est_change = questionnaire.set_date_butoir(LocalDate.of(annee, mois, jour));
						sc.nextLine();
						if(est_change){
							System.out.println("La date butoir a bien été modifiée.\n");
						}else{
							System.out.println("La date butoir n'a pas pu être modifiée.\n");
						}
					}	
				}	
			}
		}
	}

	/**affiche un menu proposant des modifications sur le questionnaire et effectue ces modifications 
	 * @param questionnaire le questionnaire dans lequel effectuer des modifications
	 */
	public static void modifier_questionnaire(Questionnaire questionnaire){
		int choice = 1;
		while(choice != 0){
			System.out.println(questionnaire);
			System.out.print("\n1 : Ajouter une question\n2 : Modifier une question\n3 : Supprimer une question\n4 : Soumettre le questionnaire\n5 : Modifier la date butoir\n0 : Retour au menu\n\nVotre choix : ");
			choice = sc.nextInt();
			sc.nextLine();
			switch (choice){
			case 1 :
				clear_display();
				ajouterQuestion(questionnaire);
				break;
			case 2 : 
				clear_display();
				modifier_question(questionnaire);
				break;
			case 3 :
				supprimerQuestion(questionnaire);
				break;
			case 4 : 
				clear_display();
				boolean est_soumis = questionnaire.soumet_questionnaire();
				if(est_soumis) {
					System.out.println("Le questionnaire a bien été soumis.\n");
				}else {
					System.out.println("Le questionnaire n'a pas pu être soumis.\n");
				}
				break;
			case 5 : 
				modifierDateButoir(questionnaire);
				break;
			default :
				clear_display();
				break;
			}
			sauvegarder(questionnaire);
		}
		clear_display();
	}

	/**sauvegarde les noms des questionnaires dans un fichier .txt
	 * @param noms_questionnaires tableau contenant les noms des questionnaires
	 * @param questionnaires_dispo fichier dans lequel sauvegarder les noms des questionnaires
	 */
	public static void sauvegarder_noms(String[] noms_questionnaires, File questionnaires_dispo){
		try {
			FileWriter ecrivainFichier = new FileWriter(questionnaires_dispo);
			int l = 0;
			while(l < noms_questionnaires.length && noms_questionnaires[l] != null){
				ecrivainFichier.write(noms_questionnaires[l] + ";");
				l++;
			}
			ecrivainFichier.close();
		}catch(FileNotFoundException e){
			System.out.println("Le fichier " + questionnaires_dispo + " n'a pas pu être ouvert.");
		}catch(IOException e){
			System.out.println("Impossible de lire dans le fichier " + questionnaires_dispo);
		}
	}

	/**permet à l'utilisateur de creer un questionnaire
	 * @param noms_questionnaires tableau contenant les noms des questionnaires
	 * @param questionnaires_dispo fichier contenant les noms des questionnaires disponibles
	 */
	public static void creerQuestionnaire(String[] noms_questionnaires, File questionnaires_dispo){
		boolean est_indispo = true;
		String nom_questionnaire_creer = "";
		int j = 0;
		while(est_indispo && nom_questionnaire_creer.equals("0") == false){
			System.out.print("Entrez le nom du questionnaire (0 pour annuler) : ");
			nom_questionnaire_creer = sc.nextLine();
			if(nom_questionnaire_creer.equals("0") == false){
				j = 0;
				est_indispo = false;
				while(j < noms_questionnaires.length && noms_questionnaires[j] != null && est_indispo == false){
					est_indispo = nom_questionnaire_creer.equals(noms_questionnaires[j]);
					j++;
				}
				if(est_indispo) System.out.println("Le nom entré est déjà pris.\n");
			}
		}
		if(nom_questionnaire_creer.equals("0") == false){
			System.out.print("Combien de questions composeront ce questionnaire (0 pour annuler) : ");
			int nb_questions = sc.nextInt();
			while(nb_questions < 0){
				System.out.print("\nVous ne pouvez pas entrer de nombre négatif.\nCombien de questions composeront ce questionnaire (0 pour annuler) : ");
				nb_questions = sc.nextInt();
			}
			if(nb_questions != 0){
				Questionnaire nouveau_questionnaire = new Questionnaire(nom_questionnaire_creer, nb_questions);
				noms_questionnaires[j] = nom_questionnaire_creer;
				j++;
				sauvegarder_noms(noms_questionnaires, questionnaires_dispo);
				clear_display();
				modifier_questionnaire(nouveau_questionnaire);
			}
		}
	}

	/**permet à l'utilisateur d'éditer un questionnaire
	 * @param noms_questionnaires tableau contenant les noms des questionnaires
	 */
	public static void editerQuestionnaire(String[] noms_questionnaires){
		boolean est_hors_longueur = true;
		int num_questionnaire_editer = 0;
		while(est_hors_longueur && num_questionnaire_editer != -1){
			System.out.print("Entrez le numéro du questionnaire à éditer (0 pour annuler) : ");
			num_questionnaire_editer = sc.nextInt() - 1;
			sc.nextLine();
			if(num_questionnaire_editer != -1){
				est_hors_longueur = num_questionnaire_editer < 0 || num_questionnaire_editer > noms_questionnaires.length || noms_questionnaires[num_questionnaire_editer] == null;
				if(est_hors_longueur) System.out.println("Le numéro entré est invalide.\n");
			}
		}
		clear_display();
		if(num_questionnaire_editer != -1){
			Questionnaire editable = Questionnaire.lecture_Fichier("Questionnaires/" + noms_questionnaires[num_questionnaire_editer] + ".txt");
			if(editable != null) modifier_questionnaire(editable);
		}
	}

	/**permet à l'utilisateur de creer un questionnaire
	 * @param noms_questionnaires tableau contenant les noms des questionnaires
	 * @param questionnaires_dispo fichier contenant les noms des questionnaires disponibles
	 */
	public static void changeNomQuestionnaire(String[] noms_questionnaires, File questionnaires_dispo){
		int num_questionnaire_changer = 0;
		boolean est_hors_longueur = true;
		while(est_hors_longueur && num_questionnaire_changer != -1){
			System.out.print("Entrez le numéro du questionnaire duquel changer le nom (0 pour annuler) : ");
			num_questionnaire_changer = sc.nextInt() - 1;
			sc.nextLine();
			if(num_questionnaire_changer != -1){
				est_hors_longueur = (num_questionnaire_changer < 0 || num_questionnaire_changer > noms_questionnaires.length) && noms_questionnaires[num_questionnaire_changer] == null;
				if(est_hors_longueur) System.out.println("Le numéro entré est invalide.\n");
			}else{
				clear_display();
			}
		}
		if(num_questionnaire_changer != -1){
			String nom_questionnaire_changer = "";
			boolean est_deja_pris = true;
			while(est_deja_pris && nom_questionnaire_changer.equals("0") == false){
				System.out.print("Entrez le nouveau nom du questionnaire (0 pour annuler) : ");
				nom_questionnaire_changer = sc.nextLine();
				if(nom_questionnaire_changer.equals("0") == false){
					int nom = 0;
					est_deja_pris = false;
					while(nom < noms_questionnaires.length && noms_questionnaires[nom] != null && est_deja_pris == false){
						est_deja_pris = nom_questionnaire_changer.equals(noms_questionnaires[nom]);
						nom++;
					}
					if(est_deja_pris) System.out.println("Le nom entré est déjà pris.\n");
				}else{
					clear_display();
				}
			}
			if(nom_questionnaire_changer.equals("0") == false){
				clear_display();
				File new_name = new File("Questionnaires/" + nom_questionnaire_changer + ".txt");
				File old_name = new File("Questionnaires/" + noms_questionnaires[num_questionnaire_changer] + ".txt");
				boolean success = old_name.renameTo(new_name);
				clear_display();
				if(success){
					Questionnaire modifier_nom = Questionnaire.lecture_Fichier("Questionnaires/" + nom_questionnaire_changer + ".txt");
					modifier_nom.set_nom(nom_questionnaire_changer);
						sauvegarder(modifier_nom);
					System.out.println("Le fichier a bien été renommé.\n");
					old_name.delete();
					noms_questionnaires[num_questionnaire_changer] = nom_questionnaire_changer;
					sauvegarder_noms(noms_questionnaires, questionnaires_dispo);
				}else{
					System.out.println("Le fichier n'a pas pu être renommé.\n");
				}
			}
		}
	}

	/**supprime un questionnaire
	 * @param noms_questionnaires tableau contenant les noms des questionnaires
	 * @param questionnaires_dispo fichier contenant les noms des questionnaires disponibles
	 */
	public static void supprimerQuestionnaire(String[] noms_questionnaires, File questionnaires_dispo){
		int num_questionnaire_supprimer = 0;
		boolean est_hors_longueur = true;
		while(est_hors_longueur){
			System.out.print("Entrez le numéro du questionnaire à supprimer (0 pour annuler) : ");
			num_questionnaire_supprimer = sc.nextInt() - 1;
			sc.nextLine();
			est_hors_longueur = num_questionnaire_supprimer < -1 || num_questionnaire_supprimer > noms_questionnaires.length || noms_questionnaires[num_questionnaire_supprimer] == null;
			if(est_hors_longueur) System.out.println("Le numéro entré est invalide.\n");
			}
		if(num_questionnaire_supprimer != -1){
			File aSupprimer = new File("Questionnaires/" + noms_questionnaires[num_questionnaire_supprimer] + ".txt");
			aSupprimer.delete();
			num_questionnaire_supprimer++;
			while(num_questionnaire_supprimer < noms_questionnaires.length && noms_questionnaires[num_questionnaire_supprimer] != null){
				noms_questionnaires[num_questionnaire_supprimer - 1] = noms_questionnaires[num_questionnaire_supprimer];
				num_questionnaire_supprimer++;
			}
			noms_questionnaires[num_questionnaire_supprimer - 1] = null;
			sauvegarder_noms(noms_questionnaires, questionnaires_dispo);
			clear_display();
			System.out.println("Le fichier a bien été supprimé.\n");
		}else{
			clear_display();
		}
	}

	/**ouvre et lis le fichier Nom_Questionnaire.txt
	 * @param questionnaires_dispo fichier contenant les noms des questionnaires disponibles
	 * @param noms_questionnaires tableau contenant les noms des questionnaires
	 */
	public static void ouvrir_noms_questionnaire(File questionnaires_dispo, String[] noms_questionnaires){
		try{
			FileReader lectureFichier = new FileReader(questionnaires_dispo);
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
			System.out.println("Le fichier " + questionnaires_dispo + " n'a pas pu être ouvert.");
		}catch(IOException e){
			System.out.println("Impossible de lire dans le fichier " + questionnaires_dispo);
		}	
	}

	public static void main(String[] args){
		clear_display();
		File questionnaires_dispo = new File("Questionnaires/Nom_Questionnaire.txt");
		String[] noms_questionnaires = new String[10];
		ouvrir_noms_questionnaire(questionnaires_dispo, noms_questionnaires);
			int choix = 1;
			while(choix != 0){
			int i = 0;
			System.out.println("\tMenu\nQuestionnaires existant :");
			boolean aucun_questionnaire = noms_questionnaires[i] == null;
			if(aucun_questionnaire) {
				System.out.println("Aucun questionnaire existant.");
			}else{
				while(i < noms_questionnaires.length && noms_questionnaires[i] != null) {
					System.out.println("\t" + (1 + i) + " : " + noms_questionnaires[i]);
					i++;
				}
			}
			System.out.print("\n1 : Créer un questionnaire\n2 : Éditer un questionnaire\n3 : Modifier le nom d'un questionnaire\n4 : Supprimer un questionnaire\n0 : Quitter\n\nVotre choix : ");
			choix = sc.nextInt();
			sc.nextLine();
			switch(choix){
			case 1 :
				clear_display();
				creerQuestionnaire(noms_questionnaires, questionnaires_dispo);
				clear_display();
				break;
			case 2 : 
				if(aucun_questionnaire){
					clear_display();
					System.out.println("Impossible, aucun questionnaire existant.\n");
				}else{
					editerQuestionnaire(noms_questionnaires);
				}
				break;
			case 3 : 
				if(aucun_questionnaire){
					clear_display();
					System.out.println("Impossible, aucun questionnaire existant.\n");
				}else{
					changeNomQuestionnaire(noms_questionnaires, questionnaires_dispo);
				}
				break;
			case 4 :
				if(aucun_questionnaire){
					clear_display();
					System.out.println("Impossible, aucun questionnaire existant.\n");
				}else{
					supprimerQuestionnaire(noms_questionnaires, questionnaires_dispo);
				}
				break;
			default :
				clear_display();
				break;
			}
		}
	}
}