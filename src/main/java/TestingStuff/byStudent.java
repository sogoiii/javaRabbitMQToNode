package TestingStuff;

import java.util.ArrayList;

public class byStudent {
	
	
	
	public  int[] CorrectlyAnswered;
	public  int[] IncorrectlyAnswered;
	public  int[] RepliedTo;
	
	
	byStudent(int numstudents){ //constructor 
		CorrectlyAnswered = new int[numstudents];
		IncorrectlyAnswered = new int[numstudents];
		RepliedTo = new int[numstudents];
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

}
