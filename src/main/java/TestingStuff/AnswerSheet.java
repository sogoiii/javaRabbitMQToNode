package TestingStuff;

public class AnswerSheet {

	
	
	private static String[] PossibleAnswers;
	private static String Questiontext;
	private static String CorrectAnswertext;
	
	
	public  void setQuestion(String Q){
		this.Questiontext = Q;
	}
	public void  setAnswer(String A){
		this.CorrectAnswertext = A;
	}
	public void setPossibleAnswer(String[] PA){
		this.PossibleAnswers = PA;
	}

	
	
	
	
}//end of class
