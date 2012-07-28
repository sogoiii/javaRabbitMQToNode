package TestingStuff;

import org.apache.commons.math3.stat.StatUtils;

import TestingStuff.byStudent;

public class byTest {

	
	public double PercentCorrectlyAnswered; // total correct answers selected across sum(byquestions.correctlyanswered) / (Test.quesions*numberofstudents)
	public double PercentIncorrectlyAnswered; // total correct answers selected across sum(byquestions.Incorrectlyanswered) / (Test.quesions*numberofstudents)
	public double ScoreMean; //grab the score for every student 
	public double ScoreSTD; //std of each student score 
	
	private int numQ; //number of questions
	private int numS; //number of students
	
	private byStudent students = null; //byStudent class instance
	


	public byTest(int numofquestions, int numstudents, byStudent bystudent){
		numS = numstudents;
		numQ = numofquestions;
		students = bystudent;
	}//end of constructor
	
	
	public void ComputeMeanScoreTest(){
		ScoreMean = StatUtils.mean(students.ScoreTotal);
		System.out.println("mean score of all tests = " + ScoreMean);
	}//end o ComputeMeanScoreTest
	
	
	public void ComputeMeanScoreSTD(){
		double variance = StatUtils.variance(students.ScoreTotal);
		ScoreSTD = Math.sqrt(variance);
//		System.out.println("Test variance and std = " + variance + " and std = " + ScoreSTD);
	}//end ComputeMeanScoreSTD	
	
	
	public void ComputePercentCorrecltyAnswered(){
		int sumcorrectlyanswered = 0;
		for(int i = 0; i < numS; i++){
			sumcorrectlyanswered = students.CorrectlyAnswered[i] + sumcorrectlyanswered;
		}//end of for loop	
		PercentCorrectlyAnswered = (double) sumcorrectlyanswered/(numS*numQ);
//		System.out.println("total correclty answered questions = " +  sumcorrectlyanswered);
//		System.out.println("Percent correctly asnwered by test  = " +  PercentCorrectlyAnswered);	
	}//end ComputePercentCorrecltyAnswered
	
	
	
	public void ComputePercentIncorrecltyAnswered(){
		int sumincorrectlyanswered = 0;
		for(int i = 0; i < numS; i++){
			sumincorrectlyanswered = students.IncorrectlyAnswered[i] + sumincorrectlyanswered;
		}//end of for loop	
		PercentIncorrectlyAnswered = (double) sumincorrectlyanswered/(numS*numQ);
//		System.out.println("total Incorreclty answered questions = " +  sumincorrectlyanswered);
//		System.out.println("Percent Incorrectly asnwered by test  = " +  PercentIncorrectlyAnswered);	
	}//end ComputePercentCorrecltyAnswered
	
	
	
	
	
	
	
}//end of class
