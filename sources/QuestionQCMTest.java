import org.junit.Test;
import org.junit.Assert;

public class QuestionQCMTest {

	@Test
	public void testAjoute_reponse() {
		String[] reponses = {"Reponse 1", "Reponse 2", "Reponse 3"};
		QuestionQCM question = new QuestionQCM(1, "test", reponses, 1);
		Assert.assertEquals("nouvelle réponse", true, question.ajoute_reponse("Reponse 4"));
		Assert.assertEquals("réponse déjà présente", false, question.ajoute_reponse("Reponse 1"));
		Assert.assertEquals("espace", false, question.ajoute_reponse(" "));
		Assert.assertEquals("retour à la ligne", false, question.ajoute_reponse("\n"));
		Assert.assertEquals("chaîne vide", false, question.ajoute_reponse(null));
	}
	
	@Test
	public void testPermute_reponses() {
		String[] reponses = {"Reponse 1", "Reponse 2", "Reponse 3"};
		QuestionQCM question = new QuestionQCM(1, "test", reponses, 1);
		Assert.assertEquals("données valides", true, question.permute_reponses(1, 2));
		Assert.assertEquals("même emplacement", true, question.permute_reponses(0, 0));
		Assert.assertEquals("premier emplacement hors tableau", false, question.permute_reponses(-1, 1));
		Assert.assertEquals("second emplacement hors tableau", false, question.permute_reponses(2, 3));
		Assert.assertEquals("même emplacement hors tableau", false, question.permute_reponses(5, 5));
	}
}