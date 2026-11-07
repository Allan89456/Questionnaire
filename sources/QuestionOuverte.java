package sources;

public class QuestionOuverte extends Question{
	private int solution;

	/**crée une question ouverte avec une solution
	 * @param solution solution à la question
	 */
	public QuestionOuverte(int difficulte, String enonce, int solution){
		super(difficulte, enonce);
		this.solution = solution;
	}

	/**crée une question ouverte avec une solution
	 * @param solution solution à la question
	 */
	public QuestionOuverte(int difficulte, String enonce, String solution){
		super(difficulte, enonce);
		int rep = 0;
		for(int i = 1; i < solution.length(); i++){
			char caractere = solution.charAt(i);
			if(caractere >= '0' && caractere <= '9'){
				rep = rep * 10 + caractere - '0';
			}
		}
		if(solution.charAt(1) == '-'){
			rep *= -1;
		}
		this.solution = rep;
	}

	/**crée une question ouverte avec une solution
	 * @param solution solution à la question
	 */
	public QuestionOuverte(QuestionOuverte q){
		super(q.difficulte, q.enonce);
		solution = q.solution;
	}

	public String get_reponses(){
		return "C'est une question ouverte, aucune proposition de réponse disponible.\nLa réponse est un entier.\n";
	}

	/**@return numéro de la solution de la question
	 */
	public int get_solution(){
		return solution;
	}

	public int get_nb_reponses(){
		return -1;
	}

	public boolean set_reponse(String new_reponse, int num_reponse){
		return false;
	}

	public boolean ajoute_reponse(String new_reponse){
		return false;
	}

	public boolean supprime_reponse(int num_reponse){
		return false;
	}

	/**change la solution de la question si 0 <= solution < reponses.length
	 * @param solution nouveau numéro de la solution à la question
	 * @return true si la solution a été changée, false sinon
	 */
	public boolean set_solution(String solution){
		int new_soluce = 0;
		for(int i = 0; i < solution.length(); i++){
			char caractere = solution.charAt(i);
			if(caractere >= '0' && caractere <= '9'){
				new_soluce = new_soluce * 10 + caractere - '0';
			}else if(i != 0 || caractere != '-'){
				return false;
			}
		}
		if(solution.charAt(0) == '-'){
			new_soluce *= -1;
		}
		this.solution = new_soluce;
		return true;
	}

	/**check l'égalité en profondeur
	 * @param q question à comparer
	 * @return true si q et this ont exactement les mêmes champs, false sinon
	 */
	public boolean equals(QuestionOuverte q){
		if(q == null) return false;
		if(q == this) return true;
		return solution == q.solution;
	}

	/**@return le clone de la question this
	 */
	public QuestionOuverte clone(){
		return new QuestionOuverte(super.difficulte, super.enonce, solution);
	}

	public boolean permute_reponses(int premier, int second){
		return true;
	}

	/**compare la réponse entrée par l'utilisateur à la solution à la question
	 * @param phrase reponse entrée par l'utilisateur
	 * @return 5 si l'utilisateur a rentré la bonne réponse, 0 si la réponse est incorrect, 1 si réponse est hors des bornes
	 */
	public int repondre(String phrase){
		int points = 0;
		int rep = 0;
		for(int i = 0; i < phrase.length(); i++){
			char caractere = phrase.charAt(i);
			if(caractere >= '0' && caractere <= '9'){
				rep = rep * 10 + caractere - '0';
			}else if(i != 0 || caractere != '-'){
				return 1;
			}
		}
		if(phrase.charAt(0) == '-'){
			rep *= -1;
		}
		if(rep == solution){
			points = 5;
		}
		return points;
	}

	/**@return la question this sous forme de String
	 */
	public String toString(){
		return super.toString() + "rep : " + solution;
	}
}