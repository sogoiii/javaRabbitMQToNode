package TestingStuff;

public class byQuestion {
	
	public int[] CorrectlyAnswered;
	public int[] IncorrectlyAnswered;
	public int[] SelectedCorrectAnswer;
	public int[] SelectedWrongAnswer_0;
	public int[] SelectedWrongAnswer_1;
	public int[] SelectedWrongAnswer_2;
	public float[] PercentCorrectlyAnswered;
	public float[] PercentIncorrectlyAnswered;
	
	private int numQ; //number of questions
	private int numS; //number of students
	
	byQuestion(int numofquestions, int numofstudents){
		System.out.println("byquestion num of questions = " + numofquestions);
		numQ = numofquestions;
		numS = numofstudents;
		CorrectlyAnswered = new int[numofquestions];
		IncorrectlyAnswered = new int[numofquestions];
		SelectedCorrectAnswer = new int[numofquestions];
		SelectedWrongAnswer_0 = new int[numofquestions];
		SelectedWrongAnswer_1 = new int[numofquestions];
		SelectedWrongAnswer_2 = new int[numofquestions];
		PercentCorrectlyAnswered  = new float[numofquestions];
		PercentIncorrectlyAnswered  = new float[numofquestions];
	}//end of constructor
	
	public void IncrementCorrectlyAnswered(int Qint){
//		System.out.println("class correctlyasnwered for question " + Qint  + " was = " +CorrectlyAnswered[Qint]);
		CorrectlyAnswered[Qint] = CorrectlyAnswered[Qint] + 1;
//		System.out.println("class correctlyasnwered for question " + Qint  + " is now = " +CorrectlyAnswered[Qint]);
	}//end of IncrementCorrectlyAnswered

	
	
	public void IncrementIncorrectlyAnswered(int Qint){
		IncorrectlyAnswered[Qint] = IncorrectlyAnswered[Qint] + 1;
	}//end of IncrementIncorrectlyAnswered
	
	public void IncrementSelectedAnswer(int selection, int question){
		
		switch (selection){
		case 0: SelectedWrongAnswer_0[question] = SelectedWrongAnswer_0[question] + 1;
				System.out.println("updated wrong answer 0");
				break;
		case 1: SelectedWrongAnswer_1[question] = SelectedWrongAnswer_1[question] + 1;
				System.out.println("updated wrong answer 1");
				break;
		case 2: SelectedWrongAnswer_2[question] = SelectedWrongAnswer_2[question] + 1;
				System.out.println("updated wrong answer 2");
				break;
		case 3: SelectedCorrectAnswer[question] = SelectedCorrectAnswer[question] + 1;
				System.out.println("updated Correct answer");
				break;
		default:  //thinks of something
				break;
		}//end of switch
	}//end of IncrementSelectedWrongAnswer
	
	
	public void ComputePercentCorrectlyAnswered(){
		for(int i =0; i < PercentCorrectlyAnswered.length; i++){
//			System.out.println("Computation = " + CorrectlyAnswered[i] + " / " + numS);
			PercentCorrectlyAnswered[i] = (float) CorrectlyAnswered[i]/numS;
			System.out.println("Percent Correct = " + PercentCorrectlyAnswered[i]);
		}//end of for loop	
	}//end ComputePercentCorrectlyAnswered
	
	public void ComputePercentIncorrectlyAnswered(){
		for(int i =0; i < PercentIncorrectlyAnswered.length; i++){
			PercentIncorrectlyAnswered[i] = (float) IncorrectlyAnswered[i]/numS;
			System.out.println("Percent incorrect = " + PercentIncorrectlyAnswered[i]);
		}//end of for loop	
	}//end ComputePercentIncorrectlyAnswered
	
	
	
}//end of class
