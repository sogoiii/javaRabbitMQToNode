package TestingStuff;

import org.apache.commons.math3.stat.StatUtils;
public class byQuestion {
	
	public double[] CorrectlyAnswered;
	public int[] IncorrectlyAnswered;
	public int[] SelectedCorrectAnswer;
	public int[] SelectedWrongAnswer_0;
	public int[] SelectedWrongAnswer_1;
	public int[] SelectedWrongAnswer_2;
	public float[] PercentCorrectlyAnswered; //this a specific question across student intput
	public float[] PercentIncorrectlyAnswered; //for a specific question across student input
	public double[] ScoreDefault;//list of the score value for any question
//	public double[] ScoreResult;//
	public double[][] Scoresbystudent; //list of scores across students (num students)*(num questions)
	public double[][] Scoresbyquestion; //list of scores across students (num questions)*(num students)
	public double[] ScoreMean; //average score for a question across every student
	public double[] STD;
	
	private int numQ; //number of questions
	private int numS; //number of students
	
	public byQuestion(int numofquestions, int numofstudents){
//		System.out.println("byquestion num of questions = " + numofquestions);
		numQ = numofquestions;
		numS = numofstudents;
		CorrectlyAnswered = new double[numofquestions];
		IncorrectlyAnswered = new int[numofquestions];
		SelectedCorrectAnswer = new int[numofquestions];
		SelectedWrongAnswer_0 = new int[numofquestions];
		SelectedWrongAnswer_1 = new int[numofquestions];
		SelectedWrongAnswer_2 = new int[numofquestions];
		PercentCorrectlyAnswered  = new float[numofquestions];
		PercentIncorrectlyAnswered  = new float[numofquestions];
		ScoreDefault = new double[numofquestions]; //list of scores for each question
		Scoresbystudent = new double[numofstudents][numofquestions];//list of scores by student
		Scoresbyquestion = new double[numofquestions][numofstudents];//list of scores by question
		ScoreMean = new double[numofquestions]; // for any one question, what was the average score
		STD = new double[numQ];
		
	}//end of constructor
	
	
	public void InsertScore(int stud, int quest){//insert the result from the test
		Scoresbystudent[stud][quest] = ScoreDefault[quest];
		Scoresbyquestion[quest][stud] = ScoreDefault[quest];
	}
	
	public void ComputeMeanScoreByQuestion(){
		
//		for(int i = 0; i < numQ; i++){//i = the question
//			double[] ScorbyQ = new double[numQ];
//			for(int j = 0; j < numS;j++){ //j = the student
//				ScorbyQ[i] = Scores[j][i];
//			}
//		}
		
		for(int i = 0; i < numQ;i++){
			ScoreMean[i] = StatUtils.mean(Scoresbyquestion[i]);
//			System.out.println("Mean Score by question = " + ScoreMean[i]);
		}//end of for
	}//end of ComputeMean by question
	
	
//	public void ComputeMeanScoreByStudent(){ //this is using ScoreMean which is overidding older data
//		for(int i = 0; i < numS;i++){
//			ScoreMean[i] = StatUtils.mean(Scoresbystudent[i]);
//			System.out.println("Mean Score by student = " + ScoreMean[i] );
//		}//end of for
//	}//end of computemeanbystudent
	
	
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
//				System.out.println("updated wrong answer 0");
				break;
		case 1: SelectedWrongAnswer_1[question] = SelectedWrongAnswer_1[question] + 1;
//				System.out.println("updated wrong answer 1");
				break;
		case 2: SelectedWrongAnswer_2[question] = SelectedWrongAnswer_2[question] + 1;
//				System.out.println("updated wrong answer 2");
				break;
		case 3: SelectedCorrectAnswer[question] = SelectedCorrectAnswer[question] + 1;
//				System.out.println("updated Correct answer");
				break;
		default:  //thinks of something
				break;
		}//end of switch
	}//end of IncrementSelectedWrongAnswer
	
	
	public void ComputePercentCorrectlyAnswered(){
		for(int i =0; i < PercentCorrectlyAnswered.length; i++){
//			System.out.println("Computation = " + CorrectlyAnswered[i] + " / " + numS);
			PercentCorrectlyAnswered[i] = ( (float) CorrectlyAnswered[i]/numS)*100; //loss of data issue, if output is a float
//			System.out.println("Percent Correct = " + PercentCorrectlyAnswered[i]);
		}//end of for loop	
	}//end ComputePercentCorrectlyAnswered
	
	public void ComputePercentIncorrectlyAnswered(){
		for(int i =0; i < PercentIncorrectlyAnswered.length; i++){
			PercentIncorrectlyAnswered[i] = ( (float) IncorrectlyAnswered[i]/numS)*100;
//			System.out.println("Percent incorrect = " + PercentIncorrectlyAnswered[i]);
		}//end of for loop	
	}//end ComputePercentIncorrectlyAnswered
	
	
	public void ComputePercentCorrectSTD(){
		double variance = StatUtils.variance(CorrectlyAnswered);
		double std = Math.sqrt(variance);
		System.out.println("variance = " + variance + " and std = " + std);
	}//end of compute STD
	
	public void ComputeMeanbyQuestionSTD(){
		for(int i = 0; i < numQ;i++){
			double variance = StatUtils.variance(Scoresbyquestion[i]);
			STD[i] = Math.sqrt(variance);
//			System.out.println("Mean by Question variance = " + variance + " and std = " + STD[i]);
		}
	}//end computemeanbyquestionstd
	
	
	
	
}//end of class
