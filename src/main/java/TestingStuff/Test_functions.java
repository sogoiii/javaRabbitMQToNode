package TestingStuff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test_functions {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		
		String[] WAA = new String[4]; 
		WAA[0] = "Angello";
		WAA[1] = "Pietro";
		WAA[2] = "Bruno";
		WAA[3] = "Fiorella";
		
		
		
		PossibleAnswersOrder(WAA);
		PossibleAnswersOrder(WAA);
		PossibleAnswersOrder(WAA);
		PossibleAnswersOrder(WAA);
		PossibleAnswersOrder(WAA);
		PossibleAnswersOrder(WAA);
		PossibleAnswersOrder(WAA);
		PossibleAnswersOrder(WAA);
	}//end of main

	
	
	
	private static void PossibleAnswersOrder(String[] WAA){
//		byStudent bystudent = new byStudent
		
//		AnswerSheet AS = new AnswerSheet();
		
		
//		List<Integer> Selections = new ArrayList<Integer>();
		
		List<Integer> list = new ArrayList<Integer>();
		list.add(0);
		list.add(1);
		list.add(2);
		list.add(3);
		Collections.shuffle(list);
	
		
		for(int i = 0; i < list.size();i++){
			System.out.println("Order " + i + " = " + WAA[list.get(i)] );
			if(WAA[list.get(i)].equals("Angello")){
				System.out.println("Answer location = " + i);
			}//end of if
		}//end of for loop
	
		System.out.println("________________________________");
		
	}//end of PossibleAnswersOrder
	
	
	
}//end of class
