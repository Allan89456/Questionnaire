package sources;

public class QuestionQCM extends Question{
	private String[] reponses;
	//INVARIANT:
	//pour tout i : 0 <= i < reponses.length : reponses[i] != null
	private int solution;
	//0 <= solution < reponses.length

	/**crée une question avec des réponses possibles et la bonne réponse
	 * @param reponses reponses possibles à la question
	 * @param solution solution à la question si 0 <= solution < reponses.length, sinon 0
	 */
	public QuestionQCM(int difficulte, String enonce, String[] reponses, int solution){
		super(difficulte, enonce);
		int nb_reponses = 0;
		if(reponses != null){
			while(nb_reponses < reponses.length && reponses[nb_reponses] != null){
				nb_reponses++;
			}
			this.reponses = new String[nb_reponses];
			for(int i = 0; i < nb_reponses; i++){
				this.reponses[i] = reponses[i];
			}
		}
		if(reponses != null && solution >= 0 && solution < nb_reponses && reponses[solution] != null){
			this.solution = solution;
		}else{
			this.solution = 0;
		}
	}

	/**crée une question avec des réponses possibles et la bonne réponse
	 * @param reponses reponses possibles à la question
	 * @param solution solution à la question si 0 <= solution < reponses.length, sinon 0
	 */
	public QuestionQCM(QuestionQCM q){
		super(q.difficulte, q.enonce);
		int nb_reponses = 0;
		if(q.reponses != null){
			while(nb_reponses < q.reponses.length && q.reponses[nb_reponses] != null){
				nb_reponses++;
			}
			reponses = new String[nb_reponses];
			for(int i = 0; i < nb_reponses; i++){
				reponses[i] = q.reponses[i];
			}
		}
		if(q.reponses != null && q.solution >= 0 && q.solution < nb_reponses && q.reponses[solution] != null){
			solution = q.solution;
		}else{
			solution = 0;
		}
	}

	/**crée une question avec des réponses possibles, ainsi qu'une bonne réponse par défaut (0)
	 * @param reponses reponses possibles à la question
	 */
	public QuestionQCM(String enonce, String[] reponses){
		super(enonce);
		solution = 0;
		int nb_reponses = 0;
		if(reponses != null){
			while(nb_reponses < reponses.length && reponses[nb_reponses] != null){
				nb_reponses++;
			}
			this.reponses = new String[nb_reponses];
			for(int i = 0; i < nb_reponses; i++){
				this.reponses[i] = reponses[i];
			}
		}
	}

	/**@return liste des reponses possibles à la question
	 */
	public String get_reponses(){
		String phrase = "";
		for(int i = 0; i < reponses.length; i++){
			phrase += (char) ('A' + i) + ") " + reponses[i] + "\n";
		}
		return phrase;
	}

	/**@return numéro de la solution de la question
	 */
	public int get_solution(){
		return solution;
	}

	/**@return nombre de reponses possibles à la question
	 */
	public int get_nb_reponses(){
		return reponses.length;
	}

	/**change une réponse à la question si 0 <= num_reponse < reponses.length et new_reponse absente de reponses[], sinon reponses[num_reponse] reste la même
	 * @param new_reponse nouvelle réponse possible à la question
	 * @param num_reponse numéro de la réponse a changée
	 * @return true si la réponse a été changée, false sinon
	 */
	public boolean set_reponse(String new_reponse, int num_reponse){
		boolean est_Change = num_reponse >= 0 && num_reponse < reponses.length && reponses[num_reponse] != null;
		int i = 0;
		while(i < reponses.length && est_Change){
			est_Change = new_reponse.equals(reponses[i]) == false;
			i++;
		}
		if(est_Change){
			reponses[num_reponse] = new_reponse;
		}
		return est_Change;
	}

	/**ajoute une réponse à la question si elle n'est pas encore présente dans cette question
	 * @param new_reponse nouvelle réponse possible à la question
	 * @return true si la réponse a été ajoutée, false sinon
	 */
	public boolean ajoute_reponse(String new_reponse){
		boolean est_ajouter = new_reponse != null && new_reponse.equals(" ") == false && new_reponse.equals("\n") == false;
		int i = 0;
		while(i < reponses.length && est_ajouter){
			est_ajouter = new_reponse.equals(reponses[i]) == false;
			i++;
		}
		if(est_ajouter){
			String[] tab = new String[reponses.length + 1];
			for(i = 0; i < reponses.length; i++) {
				tab[i] = reponses[i];
			}
			tab[i] = new_reponse;
			reponses = tab;
		}
		return est_ajouter;
	}

	/**change une réponse à la question
	 * @param num_reponse numéro de la réponse a changée
	 * @return true si la réponse a été supprimée, false sinon
	 */
	public boolean supprime_reponse(int num_reponse){
		boolean est_enleve = num_reponse >= 0 && num_reponse < reponses.length && reponses[num_reponse] != null;
		if(est_enleve){
			String[] tab = new String[reponses.length - 1];
			int j = 0;
			int i = 0;
			while(i < reponses.length && reponses[i] != null) {
				if(i != num_reponse) {
					tab[j] = reponses[i];
					j++;
				}
				i++;
			}
			reponses = tab;
		}
		if(num_reponse <= solution && solution > 0) solution--;
		return est_enleve;
	}

	/**change la solution de la question si 0 <= solution < reponses.length
	 * @param solution nouveau numéro de la solution à la question
	 * @return true si la solution a été changée, false sinon
	 */
	public boolean set_solution(String solution){
		int new_soluce = super.char_to_int(solution.charAt(0));
		boolean est_Change = new_soluce >= 0 && new_soluce < reponses.length;
		if(est_Change){
			this.solution = new_soluce;
		}
		return est_Change;
	}

	/**check l'égalité en profondeur
	 * @param q question à comparer
	 * @return true si q et this ont exactement les mêmes champs, false sinon
	 */
	public boolean equals(QuestionQCM q){
		if(q == null) return false;
		if(q == this) return true;
		boolean est_egal = q.reponses.length == reponses.length && q.solution == solution;
		int i = 0;
		while(est_egal && i < reponses.length && q.reponses[i] != null){
			est_egal = q.reponses[i].equals(reponses[i]);
			i++;
		}
		return est_egal;
	}

	/**@return le clone de la question this
	 */
	public QuestionQCM clone(){
		return new QuestionQCM(super.difficulte, super.enonce, reponses, solution);
	}

	/**permute les réponses possibles à la question
	 * @param premier une des deux réponse à permuter
	 * @param second une des deux réponse à permuter
	 * @return true si le changement a été éffectué, false sinon
	 */
	public boolean permute_reponses(int premier, int second){
		boolean changement_effectue = premier >= 0 && premier < reponses.length && second >= 0 && second < reponses.length && reponses[premier] != null && reponses[second] != null;
		if(changement_effectue){
			if(premier == second) return true;
			String tmp = reponses[premier];
			reponses[premier] = reponses[second];
			reponses[second] = tmp;

		}
		return changement_effectue;
	}

	/**compare la réponse entrée par l'utilisateur à la solution à la question
	 * @param phrase reponse entrée par l'utilisateur
	 * @return 5 si l'utilisateur a rentré la bonne réponse, 0 si la réponse est incorrect, 1 si réponse est hors des bornes
	 */
	public int repondre(String phrase){
		int points = 0;
		int rep = super.char_to_int(phrase.charAt(0));
		if(rep == solution){
			points = 5;
		}else if(rep < 0 || rep >= reponses.length){
			points = 1;
		}
		return points;
	}

	/**@return la question this sous forme de String
	 */
	public String toString(){
		String phrase = super.toString();
		int i = 0;
		if(reponses != null){
			while(i < reponses.length && reponses[i] != null){
				phrase += (char) ('A' + i) + " :";
				phrase += reponses[i] + "\n";
				i++;
			}
		}
		return phrase + "rep : " + (char) ('A' + solution);
	}
}