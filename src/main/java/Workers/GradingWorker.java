package Workers;


import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCircle;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSeqElem;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_GAUSSIAN;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_HOUGH_GRADIENT;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_INTER_CUBIC;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_RGB2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_THRESH_OTSU;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvHoughCircles;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvResize;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvSmooth;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvThreshold;
import static com.googlecode.javacv.cpp.opencv_core.cvGet2D;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.TreeMap;
import java.util.Vector;

import javax.imageio.ImageIO;


import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.Stream2BufferedImage;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import com.google.zxing.qrcode.QRCodeReader;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvPoint3D32f;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;


import TestingStuff.byStudent;
import TestingStuff.byTest;
import TestingStuff.byQuestion;




public class GradingWorker {

	//static DBCollection coll = null; //name of test collection
	static ObjectMapper mapper = null;
	static DBCollection coll = null; //name of test collection
	
	public GradingWorker(DBCollection collection){
		coll = collection;
		
	}//end of class constructorimag
	
	public void Grader(InputStream is, String message) throws IOException, InterruptedException, IM4JavaException, PdfException{ //is - is the inputstream of the pdf file
		System.out.println("inside grader");
		
		
//		File PDF_file = new File("/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/Resources/CreatedPDF_Mongo_Grade_Random.pdf");
//		ByteArrayOutputStream outstream = new ByteArrayOutputStream(); 
//		byte[] mar = outstream.toByteArray();//put f data in to byte array mar //mar should equal the pdf in byte[] form
//		byte[] f = IOUtils.toByteArray(is);
//		
		
//		BufferedImage newimage = ImageIO.read(is);
		
		 //workign with jpedal, will read from inputstream
	      PdfDecoder decode_pdf = new PdfDecoder(true);
	      try{
//	      decode_pdf.openPdfArray(f);
	      decode_pdf.openPdfFileFromInputStream(is,true); //file
	      BufferedImage img = decode_pdf.getPageAsImage(1);
//	      decode_pdf.closePdfFile();
	      File fileToSave = new File("/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/decode_pdf_firstPage.jpg");
		  ImageIO.write(img, "jpg", fileToSave);
//		  JFrame frame = new JFrame("jpedal buffered image");
//			Panel panel = new Panel();
//			frame.getContentPane().add(new JLabel(new ImageIcon(img)));
//			frame.pack();
////			frame.setLocationRelativeTo(null);
//			frame.setVisible(true);
	      }
	      catch(PdfException e) {
			    e.printStackTrace();//return back and do the rpc to the user ... use return and check returns?
	    	  
	      }
		

	      
	      
	      
		 
		int numpages = decode_pdf.getPageCount(); //get page numbers for for loop
		System.out.println("number of pages = " + numpages); //check to make sure the number of pages is reasonable, dont want this to be too large call Db and return
	    
		
		
		System.out.println("message = " + message);
		ObjectMapper mapper = new ObjectMapper(); 
		DBObject TestObject = coll.findOne(new BasicDBObject("_id", new ObjectId(message))); //the actual mongo query
        System.out.println("Test Object = " + TestObject);
        JsonNode rootNode = mapper.readValue(TestObject.toString().getBytes("UTF-8"), JsonNode.class);
        JsonNode TestAnswerSheet = rootNode.get("TestAnswerSheet"); //TestAnswerSheet
        JsonNode Questions = rootNode.get("Questions");
        System.out.println("size of Questions = " + Questions.size());
        int numofquestions = Questions.size();
        System.out.println("size of answers = " + TestAnswerSheet.size());
        int numofstudents = rootNode.get("NumberOfStudents").getIntValue(); //grab the number of students 
	    System.out.println("Numer of students = " + numofstudents);
		
		
		
		
		
	    int[] CorrectlyAnswered = new int[Questions.size()]; //number of correct answers 
	    int[] IncorrectlyAnswered = new int[Questions.size()]; //number of incorrectly answered responses
	    byStudent bystudent = new byStudent(numofquestions, numofstudents); //create grading instance //Initialize with number of students 
	    byQuestion byquestion = new byQuestion(numofquestions, numofstudents);
	    System.out.println("result size = " + CorrectlyAnswered.length);
	    //need to fill the score array in byquestions
		for(int i = 0; i < Questions.size();i++){
//			System.out.println("Score for this question = " + Questions.get(i).get("Score").getDoubleValue()); 
			byquestion.ScoreDefault[i] = Questions.get(i).get("Score").getDoubleValue();
		}//end of filling score array in byquestion
	    
		 
//		int numpages = decode_pdf.getPageCount(); //get page numbers for for loop
		System.out.println("number of pages = " + numpages); //check to make sure the number of pages is reasonable, dont want this to be too large call Db and return
		System.out.println("____________________________________");
//		   JFrame frame = new JFrame(); //window popup 
//		ArrayList Results = new ArrayList(); //Array of the answer locations
//		ArrayList WA = new ArrayList(); //array of wrong answers that were selected by the students
//		ArrayList SR = new ArrayList(); //holding accumulated data below. selected answers array
		int numoffails = 0;
		int index = 0;//debug line to save images
		int Aindex = 0;
//		int Qindex = 0;
		int[][] Selections = new int[2][Questions.size()]; // student , question
		int[][] SelectionTotal = new int[Questions.size()][4]; // question, answer selected		
		for(int i = 0; i < numpages;i++){ //for every page
	    	
//	    	File PDF_file = new File("/Users/angellopozo/Documents/TestImages/PDF_CRICLEV2.pdf");
	    	//convert page to PDF
//			 BufferedImage PDF_img = ConvertPageToImage(PDF_file,i);//debugging
	    	 BufferedImage PDF_img = decode_pdf.getPageAsImage(i+1); //production?
//			  
			 System.out.println("(width,height) = " + PDF_img.getWidth() + " " + PDF_img.getHeight());
//			 PDF_img = resizeImage(PDF_img);
			 PDF_img = scaleImage(PDF_img, 638, 830);
			 System.out.println("(width,height) = " + PDF_img.getWidth() + " " + PDF_img.getHeight());
			 
			 
			//START creating luminance source
				 LuminanceSource lumSource = new BufferedImageLuminanceSource(PDF_img);
				 BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(lumSource));		
				 
				 Reader reader = new QRCodeReader(); //create qr reader
				 GenericMultipleBarcodeReader multireader = new GenericMultipleBarcodeReader(reader); 
				 System.out.println(" I BETTER DAMN WELL SEE THIS!!!");
				 Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();
				 hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
				 
				 TreeMap<String, Rectangle2D> sortedBarcodeResults = new TreeMap<String, Rectangle2D>();
				 Result results[] = null;
				 try {
					    results = multireader.decodeMultiple(bitmap, hints);
					} catch (ReaderException re) {
					    return;
				 }//end of try
			//END creating luminance source 
				 
				 
				 
				 
				 
				 
				 
		   //go through each found QR Code and draw a box around it		 
				 BufferedImage outimage = PDF_img;//copy of the pdf image
				 Graphics2D g2 = outimage.createGraphics();
				 g2.setColor(Color.green);
				 g2.setStroke(new BasicStroke(3));
				//draw boxes around the found qrcodes 
				 index = 0;//debug line to save images
				 for ( Result result: results ) {
					    System.out.println("barcode result: " + result.getText());
					    double x1 = result.getResultPoints()[0].getX(); //top left
					    double y1 = result.getResultPoints()[0].getY(); // top left
					    double x2 = result.getResultPoints()[1].getX(); //top right
					    double y2 = result.getResultPoints()[1].getY(); //top right
					    double x3 = result.getResultPoints()[2].getX();// bottom left
					    double y3 = result.getResultPoints()[2].getY(); //bottom left
					    // double x4 = result.getResultPoints()[3].getX(); //bottom right (bottom right square location..some qr have it)
					    //  double y4 = result.getResultPoints()[3].getY(); //bottom right (bottom right square location..some qr have it)
					    Rectangle2D rectbox = new Rectangle2D.Double(x2, y2, (x3-x2), (y1-y2));
					    // Double buffer = 10.0;//highly dependent on the size of the qrcode
					    // Rectangle2D rectbox = new Rectangle2D.Double(x2-buffer, y2-buffer, (x3-x2)+2*buffer, (y1-y2)+2*buffer);
//					    System.out.println("barcode location: " + x1 +" "+ y1 +" "+ x2 +" "+ y2 + " " + x3 +" "+ y3);
					    // System.out.println("barcode location: " + x3 +" "+ y3+" "+ x4+" "+ y4+"\n");// +" "+ (x2-x1) +" "+ (y2-y1) +"\n");
					    sortedBarcodeResults.put(result.getText(), rectbox); //(qrdecoded string , rectangle box in pixels)

					    g2.draw(rectbox); //draw box around qrcode 
					    
					    Rectangle2D bubblebox = new Rectangle2D.Double(x2 + (x3-x2) + 15 ,y2 -20, 45, (y1-y2)+55);	//box around bubbles
					    g2.draw(bubblebox);//area that the bubbles exist in the image
					    
					    BufferedImage subBubble = PDF_img.getSubimage((int)(x2 + (x3-x2) + 15) ,(int)(y2 - 20) , 45, (int)((y1-y2)+55));//box around bubbles
					    IplImage ipl_subBubble = IplImage.createFrom(subBubble);//convert subimage into iplimage
					    IplImage ipl_subBubble_large = cvCreateImage(cvSize(ipl_subBubble.width()*4,ipl_subBubble.height()*4),ipl_subBubble.depth(),ipl_subBubble.nChannels());
					    cvResize(ipl_subBubble, ipl_subBubble_large, CV_INTER_CUBIC);//enlarge image 
					    IplImage ipl_subBubble_gray = cvCreateImage( cvSize( ipl_subBubble_large.width(), ipl_subBubble_large.height() ), IPL_DEPTH_8U, 1 ); //create black and white version of page
					   // IplImage ipl_subBubble_gray = ipl_subBubble_large.clone();
					    
					    
					    
					    if(ipl_subBubble_large.nChannels() > 1){
					    	cvCvtColor(ipl_subBubble_large,ipl_subBubble_gray,CV_RGB2GRAY );
					    }
					    else{
					  //  	IplImage ipl_subBubble_gray = ipl_subBubble_large.clone();
					    }
					    
					    
					    cvThreshold(ipl_subBubble_gray,ipl_subBubble_gray,100,255,CV_THRESH_OTSU);
						cvSmooth(ipl_subBubble_gray,ipl_subBubble_gray,CV_GAUSSIAN,9,9,2,2);
						CvMemStorage circles = CvMemStorage.create();
						
						
						//show bubbles, check this if no grading is working
//						CanvasFrame smoothed = new CanvasFrame("gray image");
//						smoothed.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
//						smoothed.showImage(ipl_subBubble_gray);
						
	
						CvSeq seq = cvHoughCircles(ipl_subBubble_gray, circles, CV_HOUGH_GRADIENT,
                                				1, 50, 
                                				80, 20, 
                                				32,  (int)(ipl_subBubble_gray.height()/(7)));
						
						Integer[][] FilledBubbles = new Integer[4][4]; //arry holds the #of pixels seen and the y dimention of subimage
//						Vector<CvPoint> centers = new Vector<CvPoint>(4);//the 4 can be seq.total()
						for(int j=0; j<seq.total(); j++){ //draw a circle around each circle found
					        CvPoint3D32f xyr = new CvPoint3D32f(cvGetSeqElem(seq, j));
					        CvPoint center = new CvPoint(Math.round(xyr.x()), Math.round(xyr.y()));
					        int radius = Math.round(xyr.z());
					        cvCircle(ipl_subBubble_large, center, 3, CvScalar.GREEN, -1, 8, 0);//center of circle
					        cvCircle(ipl_subBubble_large, center, radius, CvScalar.BLUE, 3, 8, 0);//outer circle
					        FilledBubbles[j][0] = FindBubbleSelected(center, radius, ipl_subBubble_gray); //bubble selected area
//					        FilledBubbles[j][0] = 1; //here to get rid of dimensions error
					        FilledBubbles[j][1] = Math.round(center.x());
					        FilledBubbles[j][2] = Math.round(center.y());
					        FilledBubbles[j][3] = Math.round(radius);
					        //System.out.println("Filled bubble Count = "+ FilledBubbles[j]);
						}//end of look for circles for
						
						
//						//the algorithm may not find circles //was trying to fix an old error, solved it by fixing th size of the image on hte pdf to image conversion
//						int anynull = anynulls(FilledBubbles);
////						System.out.println("anynull = "+ anynull);
//						if(anynull == 1){
//							numoffails++;
//							continue; //this question, not all circles were found.
//						}//end of null check //this means not all 4 circles were found
						
//						System.out.println("filled bubbles size = " + FilledBubbles[0].length);
//						System.out.println("filled bubbles size = " + FilledBubbles.length);
				        FilledBubbles = SortbyYdimention(FilledBubbles); 		     //note to self, check for nulls because that woud be an issue....   
				        
				        //print out area of bubble
//				        for(Integer[] tp : FilledBubbles){
//				        	System.out.println("Filled bubble Count = "+ tp[0] + " loc = "+ tp[1]);
//				        }
				        
				        
				        
				        int[] selectResult = ReturnIndexOfmax(FilledBubbles);//maxindex = the answer submitted by the student 
				        int maxIndex = selectResult[0];
				        int isfound = 1;
				        int ismulti = 0;
				        if(selectResult[1] > 1 || selectResult[2] == 1){ //selectResult[1] = number of bubbles , selectResult[2] = no selections made 
				        	System.out.println("more than one bubble was selected");
//				        	Aindex++; //index for looping through answer array //need to be incremented to keep data correct
//				        	index++; //(0-number of questions) //need to be incremented to keep data correct
//				        	numoffails++; //student selected too many inputs, hence trying to cheat and 
					        isfound = 0;
					        ismulti = 1;			        	
//				        	continue;
				        }//end of slectResults[1] if
				        
	/* GRADE THE RESULTS!!! */ //  TestObject =mongo query result, Aindex  = question being looked at
				        
				        

					   	String QID = new String(TestAnswerSheet.get(Aindex).get("IDS").getTextValue()); //grab the question  ID 
					   	int CorrectAnswerloc = TestAnswerSheet.get(Aindex).get("Answer").getIntValue(); //correct answer location
					   	
					   	System.out.println("Correc answer location = " + CorrectAnswerloc);
					   	System.out.println("IDS = " + QID  + " QI = " + Aindex);
					   	
					   	int iscorrect = 0;
					   	if(ismulti == 1){//if multiple selected
					   		iscorrect = 0;
					   	}
					   	else{ //if only one input for a question is found
					   		iscorrect = checkcorrectness(CorrectAnswerloc, maxIndex); 
					   	}
					   	
					   	//create the student selections by question found
				        BasicDBObject newvals = new BasicDBObject();
				        String Answersnum = new String("TestAnswerSheet." + Integer.toString(Aindex));
				        newvals.put(Answersnum + ".found", isfound);
				        newvals.put(Answersnum + ".multiselect", ismulti);
//				        newvals.put(Answersnum + ".correct", iscorrect);
//				        newvals.put(Answersnum + ".selected", maxIndex);
				        BasicDBObject posop = new BasicDBObject("$set", newvals);
				        System.out.println("inc query = " + posop.toString());
				        coll.update(new BasicDBObject("_id", new ObjectId(message)), posop);
					   	

				        
//				        System.out.println("first character = " + QID.charAt(0));
//				        System.out.println("last character = " + QID.charAt(2));
				        
			        	char stud = QID.charAt(0); //this is the student //QID starts at 1, not at 0 hence the negative
			        	char Q = QID.charAt(2); // this is the question
				        System.out.println("Student num = " + stud);
				        System.out.println("Q num = " + Character.getNumericValue(Q-1));//QID starts at 1, not at 0 hence the negative
				        
				        //Aggregate information to create Test Results array
				        int Qint = Aindex % numofquestions; //Qint = the question number of the test -1(includes 0 hence the -1) //should be equivalent to char Q
//				        System.out.println("Score for this question = " + Questions.get(Qint).get("Score").getDoubleValue()); 
				        if(iscorrect == 1){
				        	System.out.println("mod result = " + Qint);
				        	System.out.println("Question = " + Qint + " is correct = " + iscorrect );
				        	CorrectlyAnswered[Qint] = CorrectlyAnswered[Qint] + 1; // byquestion.IncrementCorrectlyAnswered(Qint);
				        	byquestion.IncrementCorrectlyAnswered(Qint);
				        	bystudent.IncrementCorrectlyAnswered(Character.getNumericValue(stud));
				        	byquestion.InsertScore(Character.getNumericValue(stud), Qint);
				        }
				        else if(iscorrect == 0){ //wrong answer was selected // Selections // or multiple selections
				        	System.out.println("mod result = " + Qint);
				        	System.out.println("Question = " + Qint + " is Incorrect = " + iscorrect );
				            IncorrectlyAnswered[Qint] = IncorrectlyAnswered[Qint] + 1; // byquestion.IncrementCorrectlyAnswered(Qint);
				            byquestion.IncrementIncorrectlyAnswered(Qint);
				            bystudent.IncrementIncorrectlyAnswered(Character.getNumericValue(stud));
				        }
				        
				        byquestion.IncrementSelectedAnswer(maxIndex, Qint); //increment the number of times a selection was made
				        
			        	Selections[Character.getNumericValue(stud)][Qint] = maxIndex;
			        	SelectionTotal[Qint][maxIndex] = SelectionTotal[Qint][maxIndex] + 1; //byquestion.IncrementSelectedWrongAnwer(Qint, maxIndex);
			        	bystudent.IncrementRepliedTo(Character.getNumericValue(stud));
				        
				        Aindex++; //index for looping through answer array 
	/* END GRADE THE RESULTS!!! */ //  TestObject
				        
				        
				        
				        
				    
				        
				        //draw the red circles
				        CvPoint slectedcenter = new CvPoint(FilledBubbles[maxIndex][1].intValue(),FilledBubbles[maxIndex][2].intValue());
				        cvCircle(ipl_subBubble_large,slectedcenter,FilledBubbles[maxIndex][3].intValue(), CvScalar.RED, 3,8,0);
				        	
				  
						
						
						//saving subimages to i can debug results
//						String subimagename = new String("subimage_"+i+"_"+index+".jpg");
						index++; //(0-number of questions) 
//						cvSaveImage(subimagename,ipl_subBubble_large);
						// create image window named "My Image"
//						String que = new String("_for_"+ result.getText());
//					    final CanvasFrame canvas = new CanvasFrame("Bubbles_Found"+que);
//					 // request closing of the application when the image window is closed
//					    canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
//					 // show image on window
//					    canvas.showImage(ipl_subBubble_large);
						
						
					    
				System.out.println("____________________________________");
				}//end of for results loop
			//end drawing boxes around each QR CODE
				
//				//START code to display in JFRAME
//				if(i == 0){
//		       frame.getContentPane().setLayout(new FlowLayout());
//		       frame.getContentPane().add(new JLabel(new ImageIcon(outimage)));
//		       frame.pack();
//		       frame.setVisible(true);
//				}
//				else {
//					
//					frame.getContentPane().add(new JLabel(new ImageIcon(outimage)));
//			        frame.pack();
//			        frame.setVisible(true);
//					
//				}  
//				//END code to display in JFRAME
				
	
	    }//end of for loop of pages
	 
	    
	    //putput how well teh students performed on test
	 for(int i = 0; i < numofstudents;i++){   
		 System.out.println("student" + i +"answered Correctly: " + bystudent.CorrectlyAnswered[i]  + " Questions");
		 System.out.println("student" + i +"answered Incorrectly: " + bystudent.IncorrectlyAnswered[i]  + " Questions");
		 System.out.println("student" + i +"answered: " + bystudent.RepliedTo[i]  + " Questions");
	 }
	 
	 
	 
	//results by student and question
	for(int i = 0; i < Selections.length; i++){
		for(int j = 0; j < Selections[0].length;j++){
			System.out.println("Student (" + i + "," + j +") selected = "+ Selections[i][j]);
		}
	}
	
	//results by question and reply
	for(int i =0 ; i < SelectionTotal.length; i++){
		System.out.println("Selection below = " + byquestion.SelectedWrongAnswer_0[i] + " " 
				 + byquestion.SelectedWrongAnswer_1[i] + " "
				 + byquestion.SelectedWrongAnswer_2[i] + " "
				 + byquestion.SelectedCorrectAnswer[i] + " ");		
		System.out.println("correctly answered = " + byquestion.CorrectlyAnswered[i] + " " + CorrectlyAnswered[i]);
		for(int j = 0; j < SelectionTotal[0].length;j++){
			System.out.println("Quesetion (" + i + "," + j +") selected = "+ SelectionTotal[i][j]);
		}

	}//end of selctiontotal for loop
	    
	byquestion.ComputePercentCorrectlyAnswered();
	byquestion.ComputePercentIncorrectlyAnswered();
	byquestion.ComputePercentCorrectSTD();
	byquestion.ComputeMeanScoreByQuestion(); //average score for any question by question
//	byquestion.ComputeMeanScoreByStudent(); //average score for any one question by student
	byquestion.ComputeMeanbyQuestionSTD();
	bystudent.ComputeTotalScores(byquestion.Scoresbystudent);//compute the total scores for any student
	bystudent.ComputeMeanTotalScore(byquestion.Scoresbystudent);
	byTest bytest = new byTest(numofquestions, numofstudents, bystudent);
	bytest.ComputeMeanScoreTest();
	bytest.ComputeMeanScoreSTD();
	bytest.ComputePercentCorrecltyAnswered();
	bytest.ComputePercentIncorrecltyAnswered();
	 
	//create Test Results by question
	ArrayList<BasicDBObject> TestResultbyQuestion = new ArrayList<BasicDBObject>(); //Array of the answer locations    
	for(int j = 0; j < byquestion.CorrectlyAnswered.length;j++){
		BasicDBObject ByQuestionVals = new BasicDBObject();
		ByQuestionVals.put("SelectedWrongAnswer_0", byquestion.SelectedWrongAnswer_0[j]);
		ByQuestionVals.put("SelectedWrongAnswer_1", byquestion.SelectedWrongAnswer_1[j]);
		ByQuestionVals.put("SelectedWrongAnswer_2", byquestion.SelectedWrongAnswer_2[j]);
		ByQuestionVals.put("SelectedCorrectAnswer", byquestion.SelectedCorrectAnswer[j]);
		ByQuestionVals.put("CorrectlyAnswered", byquestion.CorrectlyAnswered[j]);
		ByQuestionVals.put("IncorrectlyAnswered", byquestion.IncorrectlyAnswered[j]);
		ByQuestionVals.put("PercentCorrect", byquestion.PercentCorrectlyAnswered[j]);
		ByQuestionVals.put("PercentIncorrect", byquestion.PercentIncorrectlyAnswered[j]);
		ByQuestionVals.put("STD", byquestion.STD[j]);
		ByQuestionVals.put("Mean", byquestion.ScoreMean[j]);//means score for this question 
		ByQuestionVals.put("_id", new ObjectId());
		TestResultbyQuestion.add(ByQuestionVals); //add Rvals into the Testresultarray listarray
//		System.out.println("Question " + j + " numcorrect = " + CorrectlyAnswered[j]);
	}
	    
	//create Test Results by test
	BasicDBObject ByTestVals = new BasicDBObject();
	ByTestVals.put("Mean", bytest.ScoreMean);
	ByTestVals.put("STD", bytest.ScoreSTD);
	ByTestVals.put("PercentCorrect", bytest.PercentCorrectlyAnswered);
	ByTestVals.put("PercentInorrect", bytest.PercentIncorrectlyAnswered);
	ByTestVals.put("_id", new ObjectId());
	
	//create graded exists
	BasicDBObject TestGradedVals = new BasicDBObject();
	TestGradedVals.put("WasGraded", 1);
	Date now = new Date();
	TestGradedVals.put("GradeOn", now);
	TestGradedVals.put("_id", new ObjectId());
	
	//create Test Results by  student
	ArrayList<BasicDBObject> TestResultbyStudent = new ArrayList<BasicDBObject>(); //Array of the answers by student 
	for(int j = 0; j < bystudent.CorrectlyAnswered.length;j++){
		BasicDBObject ByStudentVals = new BasicDBObject();
		ByStudentVals.put("CorrectlyAnswered", bystudent.CorrectlyAnswered[j]);
		ByStudentVals.put("IncorrectlyAnswered", bystudent.IncorrectlyAnswered[j]);
		ByStudentVals.put("RepliedTo", bystudent.RepliedTo[j]);
		ByStudentVals.put("ScoreTotal", bystudent.ScoreTotal[j]);
//		ByStudentVals.put("ScoreMean", bystudent.ScoreMean[j]); //this is still wrong, unless i want ot show the mean of score for any 1 question
		ByStudentVals.put("_id", new ObjectId());
		TestResultbyStudent.add(ByStudentVals); //add Rvals into the Testresultarray listarray
//		System.out.println("Question " + j + " numcorrect = " + CorrectlyAnswered[j]);
	}
	
	
	
	
	//v1
	BasicDBObject TRbyQuestions = new BasicDBObject("TRbyQuestions", TestResultbyQuestion);
	BasicDBObject set = new BasicDBObject("$set", TRbyQuestions);
//	System.out.println("Test result query = " + TRbyQuestions);
	coll.update(new BasicDBObject("_id", new ObjectId(message)),  set);
	
	BasicDBObject TRbyTest = new BasicDBObject("TRbyTest", ByTestVals);
	BasicDBObject settest = new BasicDBObject("$set", TRbyTest);
	coll.update(new BasicDBObject("_id", new ObjectId(message)),  settest);
	
	BasicDBObject TestGradedobject = new BasicDBObject("TestGraded", TestGradedVals);
	BasicDBObject settestgraded = new BasicDBObject("$set", TestGradedobject);
	coll.update(new BasicDBObject("_id", new ObjectId(message)),  settestgraded);
	
	BasicDBObject TRbyStudent = new BasicDBObject("TRbyStudents", TestResultbyStudent);
	BasicDBObject set1 = new BasicDBObject("$set", TRbyStudent);
	coll.update(new BasicDBObject("_id", new ObjectId(message)),  set1);
	
	//v2
//	DBObject TestObject2 = coll.findOne(new BasicDBObject("_id", new ObjectId(message))); //the actual mongo query
//	TestObject2.put("CorrectlyAnswered", TestResultsarray);
//	coll.save(TestObject2);

	
	
	
	System.out.println("Failed to grade " + numoffails + " questions");
//	doc.close();
	decode_pdf.closePdfFile();//
	

	}//end of Grader
		

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public BufferedImage scaleImage(BufferedImage img, int width, int height) {
	    int imgWidth = img.getWidth();
	    int imgHeight = img.getHeight();
	    if (imgWidth*height < imgHeight*width) {
	        width = imgWidth*height/imgHeight;
	    } else {
	        height = imgHeight*width/imgWidth;
	    }
	    BufferedImage newImage = new BufferedImage(width, height,
	            BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = newImage.createGraphics();
	    try {
	        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
//	        g.setBackground(background);
	        g.clearRect(0, 0, width, height);
	        g.drawImage(img, 0, 0, width, height, null);
	    } finally {
	        g.dispose();
	    }
	    return newImage;
	}
	
	
	 private static BufferedImage resizeImage(BufferedImage originalImage){
		 int IMG_WIDTH = 640;
		 int IMG_HEIGHT = 830;
		 BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = resizedImage.createGraphics();
			g.drawImage(originalImage, 0, 0, 7, IMG_HEIGHT, null);
			g.dispose();
			return resizedImage;
	 }









public static int checkcorrectness(int TestAnswerLoc, int StudentSelected){ //1 equals correct, 0 equals wrong
	//do assertions on expected values?
	
	int iscorrect = 0;

	if(TestAnswerLoc == StudentSelected){
		iscorrect++;
		System.out.println("from check correctness = "  + iscorrect);
		return iscorrect;
	}
	else{
		System.out.println("from check correctness = "  + iscorrect);
		return iscorrect;
	}

}//end of checkcorrectness

	


public static  int anynulls(Integer[][] ar){//check the double array of any nulls, if they exit return 1
	int anynull = 0;
	
	for(int i = 0; i < ar.length; i++ ){
		for(int j = 0; j < ar[0].length; j++){
			if(ar[i][j] == null){
				return 1;
			}
		}//end of j loop
	}//end of i loop
	
	
	return anynull;
}//end of anynulls



public static BufferedImage ConvertPageToImage(File afile,int i) throws IOException, InterruptedException, IM4JavaException{
	
	 IMOperation op = new IMOperation();		  
	  op.addImage(afile.getAbsolutePath()+"["+i+"]"); //look at myscript.sh
	  op.colorspace("RGB");
	  op.resize(640, 830);
	  op.type("TrueColor");
	  op.addImage("jpg:-"); //place holder for output file
	  

	 // op.alpha("off"); //this command is slow and not needed anymore
	//  op.interpolate("spline");
	//  op.colorspace("RGB");
	  ConvertCmd convert = new ConvertCmd();
//	  convert.setSearchPath("/usr/local/Cellar/imagemagick/6.7.7-6/bin/convert");
//	  convert.setSearchPath("/usr/bin/convert");
//	  convert.setSearchPath("/Volumes/Main Drive/Users/angellopozo/ImageMagick-6.7.8/bin/convert");
//	  convert.setSearchPath("Users/angellopozo/ImageMagick-6.7.8/bin/convert");
//	  convert.setSearchPath("/usr/local/bin");
	  Stream2BufferedImage s2b = new Stream2BufferedImage();
	  convert.setOutputConsumer(s2b);
	  convert.run(op);
//	  convert.createScript("myscript.sh",op);//debug`` line
	  BufferedImage aPDF_img = s2b.getImage(); //PDF_img is the pdf_image
	  ImageIO.write(aPDF_img,"jpeg",new File("Page_"+ (i+1) +"of_PDF.jpg"));//write whole pdf page to png
	return aPDF_img;
}//end of ConvertPagetoImage



 public static int FindBubbleSelected(CvPoint cent, int r, IplImage bubbles){ //this finds the area 
	 //There exists an error if the pdf dimensions are not correct //remove when that bug has been fixed!
	int FilledArea = 0;
	int xl = cent.x() - r;//top left x
	int yl = cent.y() - r;//top left y
	int xr = cent.x() + r;//bottom right x
	int yr = cent.y() + r;//bottom right y
	
//	System.out.println("(xl,yl,xr,yr) = (" + xl + ","  + yl + "," + xr + "," + yr + ")");
	CvScalar c = null;
	//search area around bubble and sum pixels found
	for(int i = xl; i < xr; i++){
//		System.out.println("i = " + i);
		for(int j = yl; j < yr; j++){
//			System.out.println("(i,j) = (" + i + "," + j + ")");
			c = cvGet2D(bubbles, j,i);
			if(c.val(0) < 100){ //since image is gray only 0 needs to be looked at
				FilledArea++;
			}//end of pixel intensity 
		}//end of y loop
	}//end of x loop
	
//	System.out.println("area of bubble = " + FilledArea);
	return FilledArea;
}//end of FindBubbleSelected



 public static int[] ReturnIndexOfmax(Integer[][] ar){
		int max = ar[0][0];
		int index = 0;
		int count = 0;
		int numselected = 0;
		int[] out = new int[3];//declare out array
		for(Integer[] inter : ar){
			System.out.println("inter[0] = " + inter[0] );
			if(inter[0] >= 3000){
				numselected++;
			}
			if(inter[0]>max){
				max = inter[0];
				index = count;
			}
			count++;
		}
//		System.out.println("Max found in this array = "+index);
		System.out.println("answer selected = " + index);
		out[0] = index;
		out[1] = numselected;			
		
		System.out.println("area of selected box = " + ar[index][0]);
		if(ar[index][0] < 3000){ //if the number of pixels is less than 3000, then non of them were selected
			out[2] = 1;
		}
		else{
			out[2] = 0;
		}
		

		return out;
	}//end of ReturnIndexOfMax
	
 
 
	public static Integer[][] SortbyYdimention(Integer[][] ar){
//		System.out.println("ar size = " + ar.length);
//		for(int i = 0; i < ar.length; i++ ){
//			System.out.println("ar[i] = " + ar[i][0] + " i = "+ i);
//		}
		
		Arrays.sort(ar, new Comparator<Integer[]>() {
            public int compare(Integer[] entry1, final Integer[] entry2) { //the 2 index is the y dimenion of hte found circle 
                Integer time1 = entry1[2];
                Integer time2 = entry2[2];
                if(time1 == null || time2 == null){
                	System.out.println("found a null and will exit next!");
                	return time1;
                }
//                System.out.println("(time1, time2) = (" + time1 + "," + time2 +")");
                return time1.compareTo(time2);
            }//end of new comparator
        });
		return ar;
	}//end of my sort





public void test(String input){
	System.out.println("inside grader received = " + input);
}//end of test

}//end of GradingWorker
