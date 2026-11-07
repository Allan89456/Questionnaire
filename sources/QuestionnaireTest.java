import org.junit.Test;
import org.junit.Assert;
import java.time.LocalDate;

public class QuestionnaireTest {

	@Test
	public void testAjoute_question(){
		String[] reponses = {"Reponse 1", "Reponse 2", "Reponse 3"};
		QuestionQCM question = new QuestionQCM(3, "test1", reponses, 1);
		QuestionQCM question2 = new QuestionQCM(1, "test2", reponses, 3);
		QuestionQCM question3 = new QuestionQCM(2, "test3", reponses, 2);
		Questionnaire questionnaire = new Questionnaire("test", 2);
		Assert.assertEquals("ajoute première question", true, questionnaire.ajoute_question(question));
		Assert.assertEquals("ajoute seconde question", true, questionnaire.ajoute_question(question2));
		Assert.assertEquals("question déjà présente", false, questionnaire.ajoute_question(question));
		Assert.assertEquals("questionnaire rempli", false, questionnaire.ajoute_question(question3));
	}
	
	@Test
	public void testSoumet_questionnaireEtSet_date_butoir(){
		String[] reponses = {"Reponse 1", "Reponse 2", "Reponse 3"};
		QuestionQCM question = new QuestionQCM(1, "test1", reponses, 1);
		QuestionQCM question2 = new QuestionQCM(1, "test2", reponses, 1);
		QuestionQCM[] questions = {question, question2};
		Questionnaire questionnaire = new Questionnaire("test", LocalDate.now(), null, null, questions, 2);
		Assert.assertEquals("change date butoir et questionnaire non soumis", false, questionnaire.set_date_butoir(LocalDate.of(2026, 5, 15)));
		Assert.assertEquals("soumet questionnaire une première fois", true, questionnaire.soumet_questionnaire());
		Assert.assertEquals("soumet questionnaire une seconde fois", false, questionnaire.soumet_questionnaire());
		Assert.assertEquals("change date butoir et questionnaire soumis, date valide", true, questionnaire.set_date_butoir(LocalDate.of(2026, 5, 15)));
		Assert.assertEquals("nouvelle date identique à l'ancienne et questionnaire soumis", true, questionnaire.set_date_butoir(LocalDate.of(2026, 5, 15)));
		Assert.assertEquals("change date butoir et questionnaire soumis, date non valide", false, questionnaire.set_date_butoir(LocalDate.of(2020, 4, 17)));
	}
	
	@Test
	public void testPermute_questions(){
		String[] reponses = {"Reponse 1", "Reponse 2", "Reponse 3"};
		QuestionQCM question = new QuestionQCM(1, "test1", reponses, 1);
		QuestionQCM question2 = new QuestionQCM(1, "test2", reponses, 1);
		QuestionQCM[] questions = {question, question2};
		Questionnaire questionnaire = new Questionnaire("test", LocalDate.now(), null, null, questions, 2);
		Assert.assertEquals("données valides", true, questionnaire.permute_questions(0, 1));
		Assert.assertEquals("même emplacement", true, questionnaire.permute_questions(0, 0));
		Assert.assertEquals("premier emplacement hors tableau", false, questionnaire.permute_questions(2, 1));
		Assert.assertEquals("second emplacement hors tableau", false, questionnaire.permute_questions(1, -1));
		Assert.assertEquals("même emplacement hors tableau", false, questionnaire.permute_questions(10, 10));

	}
	
	@Test
	public void testRepondre(){
		String[] reponses = {"Reponse 1", "Reponse 2", "Reponse 3"};
		QuestionQCM question = new QuestionQCM(1, "test1", reponses, 1);
		QuestionQCM question2 = new QuestionQCM(1, "test2", reponses, 0);
		QuestionQCM[] questions = {question, question2};
		Questionnaire questionnaire = new Questionnaire("test", LocalDate.now(), null, null, questions, 2);
		Assert.assertEquals("bonne réponse majuscule", 5, questionnaire.repondre(0, 'B'));
		Assert.assertEquals("bonne réponse minuscule", 5, questionnaire.repondre(0, 'b'));
		Assert.assertEquals("hors des bornes", 1, questionnaire.repondre(0, 'Z'));
		Assert.assertEquals("mauvaise réponse", 0, questionnaire.repondre(1, 'c'));
		Assert.assertEquals("un chiffre", 1, questionnaire.repondre(1, '0'));
		Assert.assertEquals("un caractère spécial", 1, questionnaire.repondre(1, '*'));
	}
}
