package TestingStuff;

import org.apache.commons.math3.stat.StatUtils;


public class byStudent {
	
	
	
	public int[] CorrectlyAnswered;
	public int[] IncorrectlyAnswered;
	public int[] RepliedTo;
	public double[] ScoreTotal; //the end score for any student
	public double[] ScoreMean;//average score submitted by student
	
//	private int numQ; //number of questions
	private int numS; //number of students
	
	public byStudent(int numofquestions, int numstudents){ //constructor 
		numS = numstudents;
//		numQ = numofquestions;
		CorrectlyAnswered = new int[numstudents];
		IncorrectlyAnswered = new int[numstudents];
		RepliedTo = new int[numstudents];
		ScoreTotal = new double[numstudents];
		ScoreMean = new double[numstudents];
	}//end of class constructor
	
	
	public void IncrementCorrectlyAnswered(int student){
		CorrectlyAnswered[student] = CorrectlyAnswered[student] + 1;
	}
	
	public  void IncrementIncorrectlyAnswered(int student){
		IncorrectlyAnswered[student] = IncorrectlyAnswered[student] + 1;
	}
	
	public  void IncrementRepliedTo(int student){
		RepliedTo[student] = RepliedTo[student] + 1;
	}

	
	
	
	public void ComputeTotalScores(double[][] scoresbystudent){ //want to Compute find out the total scores from the input double array
		for(int i = 0; i < numS;i++){
			ScoreTotal[i] = StatUtils.sum(scoresbystudent[i]);
//			System.out.println("Total score for student " + i + " = " + ScoreTotal[i]);
		}//end of for loop
	}//end of inserttotalscores
	
	
	
	public void ComputeMeanTotalScore(double[][] scorebystudent){
		for(int i = 0; i < numS;i++){
			ScoreMean[i] = StatUtils.mean(scorebystudent[i]);
//			System.out.println("Score mean for student =" +  ScoreMean[i]);
		}//end of for loop
	}//end of ComputeMeanScore
	
	
	
	
}//end of class
