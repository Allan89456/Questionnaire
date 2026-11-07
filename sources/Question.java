package sources;

public abstract class Question{
	protected int difficulte;
	//0 <= difficulte < 3
	protected String enonce;

	/**crée une question avec une difficulté et un énoncé
	 * @param difficulte difficulté de la question si 0 <= difficulte < 3, sinon 0
	 * @param enonce énoncé de la question 
	 */
	public Question(int difficulte, String enonce){
		if(difficulte >= 0 && difficulte < 3){
			this.difficulte = difficulte;
		}else{
			this.difficulte = 0;
		}
		this.enonce = enonce;
	}

	/**crée une question avec une difficulté par défaut (0) et un énoncé
	 * @param enonce énoncé de la question 
	 */
	public Question(String enonce){
		difficulte = 0;
		this.enonce = enonce;
	}

	/**transforme un caractère en entier
	 * @param caractere caractère à transformer
	 * @return le caractère transformé en entier
	 */ 
	public static int char_to_int(char caractere){
		int reponse;
		if(caractere >= 'a' && caractere <= 'z'){
			reponse = caractere - 'a';
		}else if(caractere >= 'A' && caractere <= 'Z'){
			reponse = caractere - 'A';
		}else if(caractere > '0' && caractere <= '9'){
			reponse = caractere - '1';
		}else if(caractere == '0'){
			reponse = -1;
		}else{
			reponse = -2;
		}
		return reponse;
	}

	/**@return difficulte de la question
	 */
	public int get_difficulte(){
		return difficulte;
	}

	/**@return enonce de la question
	 */
	public String get_enonce(){
		return enonce;
	}

	/**change la difficulté de la question
	 * @param difficulte nouvelle difficulté de la question
	 * @return true si la difficulté a été changée, false sinon
	 */
	public boolean set_difficulte(int difficulte){
		boolean est_Change = difficulte >= 0 && difficulte < 3;
		if(est_Change){
			this.difficulte = difficulte;
		}
		return est_Change;
	}

	/**change l'énoncé de la question
	 * @param enonce nouvel énoncé de la question
	 */
	public void set_enonce(String enonce){
		this.enonce = enonce;
	}

	public abstract String get_reponses();
	public abstract int get_solution();
	public abstract int get_nb_reponses();
	public abstract boolean set_reponse(String new_reponse, int num_reponse);
	public abstract boolean ajoute_reponse(String new_reponse);
	public abstract boolean supprime_reponse(int num_reponse);
	public abstract boolean set_solution(String solution);
	public abstract Question clone();
	public abstract boolean permute_reponses(int premier, int second);
	public abstract int repondre(String phrase);

	/**check l'égalité en profondeur
	 * @param q question à comparer
	 * @return true si q et this ont exactement les mêmes champs, false sinon
	 */
	public boolean equals(Question q){
		if(q == null) return false;
		if(q == this) return true;
		return difficulte == q.difficulte && enonce.equals(q.enonce);
	}

	/**@return la question this sous forme de String
	 */
	public String toString(){
		String phrase = "(*";
		for(int i = 0; i < difficulte; i++){
			phrase += "*";
		}
		return phrase + ")\n" + enonce + "\n\n";
	}
}