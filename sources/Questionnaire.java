package sources;

import java.time.LocalDate;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Questionnaire{
	private String nom;
	private LocalDate date_creation;
	private LocalDate date_soumission;
	//pour tout i : 0 <= questions.length : questions[i] = null || questions[i].nb_reponses < 2 => date_soumission = null
	private LocalDate date_butoir;
	//date_soumission = null => date_butoir = null
	//date_creation <= date_soumission <= date_butoir
	private Question[] questions;
	//pour tout i : 0 <= i < nb_questions : questions[i] != null
	//pour tout i : nb_questions <= i < questions.length : questions[i] = null
	private int nb_questions;
	//0 <= nb_questions <= questions.length
	
	/**crée un questionnnaire avec un nom, une date de création et le nombre de questions maximal
	 * @param nom nom du questionnaire
	 */
	public Questionnaire(String nom, int nb_questions_max){
		this.nom = nom;
		date_creation = LocalDate.now();
		nb_questions = 0;
		questions = new Question[nb_questions_max];
	}
	
	/**crée un questionnnaire avec un nom, une date de création, une date de soumission, une date butoire, un nombre de questions et des questions
	 * @param nom nom du questionnaire
	 * @param date_creation date de création du questionnaire
	 * @param date_soumission date de soumission du questionnaire
	 * @param date_butoir date butoire du questionnaire
	 * @param questions questions du questionnaire
	 * @param nb_questions_max nombre de questions qu§il est possible de mettre dans le questionnaire
	 */
	public Questionnaire(String nom, LocalDate date_creation, LocalDate date_soumission, LocalDate date_butoir, Question[] questions, int nb_questions_max){
		this.nom = nom;
		this.date_creation = date_creation;
		if(date_soumission != null) this.date_soumission = date_soumission;
		if(date_butoir != null) this.date_butoir = date_butoir;
		nb_questions = 0;
		if(questions != null){
			this.questions = new Question[nb_questions_max];
			while(nb_questions < nb_questions_max && questions[nb_questions] != null){
				this.questions[nb_questions] = questions[nb_questions].clone();
				nb_questions++;
			}
		}
	}

	/**@return nom du questionnaire
	 */
	public String get_nom(){
		return nom;
	}

	/**@return date de création du questionnaire
	 */
	public LocalDate get_date_creation(){
		return date_creation;
	}

	/**@return date de soumission du questionnaire
	 */
	public LocalDate get_date_soumission(){
		return date_soumission;
	}

	/**@return date butoir du questionnaire
	 */
	public LocalDate get_date_butoir(){
		return date_butoir;
	}

	public Question get_question(int num_question){
		return questions[num_question];
	}

	/**@return nombre de questions du questionnaire
	 */
	public int get_nb_questions(){
		return nb_questions;
	}

	/**@return nombre de questions maximal du questionnaire
	 */
	public int get_nb_questions_max(){
		return questions.length;
	}

	/**change le nom du questionnaire
	 * @param nom nouveau nom du questionnaire
	 */
	public void set_nom(String nom){
		this.nom = nom;
	}

	/**ajoute une question QCM au questionnaire si c'est encore possible
	 * (nb_questions < questions.length et la question n'est pas encore présente dans le questionnaire)
	 * @param q question à ajouter
	 * @return true si la question a été ajoutée, false sinon
	 */
	public boolean ajoute_question(QuestionQCM q){
		boolean est_ajouter = date_soumission == null && nb_questions < questions.length;
		int i = 0;
		while(i < nb_questions && est_ajouter){
			est_ajouter = questions[i].get_enonce().equals(q.get_enonce()) == false;
			i++;
		}
		if(est_ajouter){
			questions[nb_questions] = new QuestionQCM(q.clone());
			nb_questions++;
		}
		return est_ajouter;
	}

	/**ajoute une question Ouverte au questionnaire si c'est encore possible
	 * (nb_questions < questions.length et la question n'est pas encore présente dans le questionnaire)
	 * @param q question à ajouter
	 * @return true si la question a été ajoutée, false sinon
	 */
	public boolean ajoute_question(QuestionOuverte q){
		boolean est_ajouter = date_soumission == null && nb_questions < questions.length;
		int i = 0;
		while(i < nb_questions && est_ajouter){
			est_ajouter = questions[i].get_enonce().equals(q.get_enonce()) == false;
			i++;
		}
		if(est_ajouter){
			questions[nb_questions] = new QuestionOuverte(q);
			nb_questions++;
		}
		return est_ajouter;
	}

	/**ajoute une question au questionnaire si c'est encore possible (le questionnaire est en construction et pas rempli)
	 * @param num_question : numéro de la question à supprimer
	 * @return true si la question a été ajoutée, false sinon
	 */
	public boolean supprime_question(int num_question){
		boolean est_supprimer = num_question >= 0 && num_question < nb_questions;
		if(est_supprimer){
			for(int i = num_question; i+1 < nb_questions; i++) {
				questions[i] = questions[i + 1].clone();
			}
			nb_questions--;
			questions[nb_questions] = null;
		}
		return est_supprimer;
	}

	/**soumet le questionnaire s'il n'a pas encore été soumis et si toutes les questions sont présentes
	 * @return true si le questionnaire a été soumis, false sinon
	 */
	public boolean soumet_questionnaire(){
		boolean peutEtreSoumis = date_soumission == null && nb_questions == questions.length;
		if(peutEtreSoumis){
			int i = 0;
			while(i < nb_questions && peutEtreSoumis){
				peutEtreSoumis = questions[i].get_nb_reponses() > 1 || questions[i].get_nb_reponses() == -1;
				i++;
			}
			if(peutEtreSoumis){
				date_soumission = LocalDate.now();
				date_butoir = date_soumission.plusMonths(1);
			}
		}
		return peutEtreSoumis;
	}

	/**change la date butoir du questionnaire si elle est après la date de soumission
	 * @param date_butoir nouvelle date butoir du questionnaire
	 * @return true si la date butoir du questionnaire a été changé, false sinon
	 */
	public boolean set_date_butoir(LocalDate date_butoir){
		boolean est_change = date_soumission != null && date_butoir.isAfter(date_soumission);
		if(est_change){
			this.date_butoir = date_butoir;
		}
		return est_change;
	}

	/**@return le clone du questionnaire this avec nb_questions_ajoutes supplementaires
	 */
	public Questionnaire clone(int nb_questions_ajoutes){
		return new Questionnaire(nom + "_2", date_creation, date_soumission, date_butoir, questions, nb_questions + nb_questions_ajoutes);
	}

	/**permute les questions du questionnaire
	 * @param premier une des deux question à permuter
	 * @param second une des deux question à permuter
	 * @return true si le changement a été éffectué, false sinon
	 */
	public boolean permute_questions(int premier, int second){
		boolean changement_effectue = premier >= 0 && premier < nb_questions && second >= 0 && second < nb_questions && questions[premier] != null && questions[second] != null;
		if(changement_effectue){
			if(premier == second) return true;
			Question tmp = questions[premier].clone();
			questions[premier] = questions[second].clone();
			questions[second] = tmp.clone();
		}
		return changement_effectue;
	}

	/**renvoie la question a posée et ses propositions
	 * @param numQ numéro de la question a posée
	 * @return la question et ses propositions
	 */
	public String getQuestionEtPropositions(int numQ){
		String phrase = "(*";
		for(int i = 0; i < questions[numQ].get_difficulte(); i++){
			phrase += "*";
		}
		return phrase + ")\n" + questions[numQ].get_enonce() + "\n\n" + questions[numQ].get_reponses();
	}

	/**transforme un String en LocalDate
	 * @param date la date format " aaaa-mm-jj"
	 * @return null si date  = " null", date sous forme de LocalDate sinon
	 */
	private static LocalDate transformeDate(String date){
		if(date.equals(" null")) return null;
		int annee = 0;
		for(int i = 1; i < 5; i++){
			annee *= 10;
			annee += date.charAt(i) - '0';
		}
		int mois = (date.charAt(6) - '0') * 10 + (date.charAt(7) - '0');
		int jour = (date.charAt(9) - '0') * 10 + (date.charAt(10) - '0');
		return LocalDate.of(annee, mois, jour);
	}

	/**lit un fichier .txt et place dans une class Questionnaire (on suppose le ficgier correctement formaté)
	 * @param accesFichier le chemin d'accès du fichier à lire 
	 * @return le fichier lu sous format Questionaire
	 */
	public static Questionnaire lecture_Fichier(String accesFichier) {
		File fichierALire = new File(accesFichier);
		try {
			Scanner sc_file = new Scanner(fichierALire);
			for(int i = 0; i < 4; i++) {
				sc_file.next();
			}
			String nom = sc_file.next();
			for(int i = 0; i < 4; i++) {
				sc_file.next();
			}
			int nb_questions = sc_file.nextInt();
			for(int i = 0; i < 3; i++) {
				sc_file.next();
			}
			LocalDate date_creation = transformeDate(sc_file.nextLine());
			for(int i = 0; i < 3; i++) {
				sc_file.next();
			}
			LocalDate date_soumission = transformeDate(sc_file.nextLine());
			for(int i = 0; i < 3; i++) {
				sc_file.next();
			}
			LocalDate date_butoir = transformeDate(sc_file.nextLine());
			Question[] questions = new Question[20];
			for(int i = 0; i < nb_questions; i++) {
				sc_file.nextLine();sc_file.nextLine();
				String difficulte = sc_file.nextLine();
				int nb_etoiles = difficulte.length() - 3;
				String enonce = sc_file.nextLine();
				String phraseFin = sc_file.next();
				if(phraseFin.equals("rep") == false){
					String[] reponses = new String[20];
					int j = 0;
					while(phraseFin.equals("rep") == false){
						sc_file.next();
						reponses[j] = sc_file.nextLine();
						j++;
						phraseFin = sc_file.next();
					}
					sc_file.next();
					int solution = sc_file.nextLine().charAt(1) - 'A';
					questions[i] = new QuestionQCM(nb_etoiles, enonce, reponses, solution);
				}else{
					sc_file.next();
					String solution = sc_file.nextLine();
					questions[i] = new QuestionOuverte(nb_etoiles, enonce, solution);
				}	
			}
			int nb_questions_max = nb_questions;
			while(sc_file.hasNextLine()){
				nb_questions_max++;
				sc_file.nextLine();sc_file.nextLine();sc_file.nextLine();
			}
			sc_file.close();
			return new Questionnaire(nom, date_creation, date_soumission, date_butoir, questions, nb_questions_max);
		}catch(FileNotFoundException e){
			System.out.println("Le fichier " + fichierALire + " n'a pas pu être ouvert.");
			return null;
		}
	}

	/**@return le questionnaire this sous forme de String
	 */
	public String toString() {
		String phrase = "Nom du questionnaire : " + nom + "\nNombre de questions : " + nb_questions + "\nDate création : " + date_creation + "\nDate soumission : " + date_soumission + "\nDate butoir : " + date_butoir;
		int i = 0;
		while(i < nb_questions) {
			phrase += "\n\nQuestion " + (i+1) + " :\n" + questions[i].toString();
			i++;
		}
		while(i < questions.length){
			phrase += "\n\nQuestion " + (i+1) + " :\nQuestion non encodée";
			i++;
		}
		return phrase;
	}
}