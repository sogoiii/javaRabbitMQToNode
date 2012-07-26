package TestingStuff;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.io.RandomAccessFileInputStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;
import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;


import TestingStuff.AnswerSheet;

public class CreatePDF2_RAND {

	/**
	 * @param args
	 */
	
	
	static int sizeofQRCode = 100; //this is the size of the QR code 

	
	String testrepo = "/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/Resources/";

	public static void main(String[] args) throws MongoException, JsonParseException, JsonMappingException, UnsupportedEncodingException, IOException, WriterException, COSVisitorException {

		//questions These will come from the database in probably in BSON if mongoDB, JSON if CouchDB

		
		Mongo m = new Mongo();
		DB db = m.getDB("ecomm_database");
		DBCollection coll = db.getCollection("testschemas");
		
		
		
		ObjectMapper mapper = new ObjectMapper(); 
		
		
		String message = "4fda1af52f910cc6200000d3"; //test id, that i will have in the real version
//		String message = "500bb8811a316fda2400003b" ;// this one has a score associatd with it
//		BasicDBObject keys = new BasicDBObject(); //create search object, keys will be what is returned
//        keys.put("Questions",1); //tell mongo to grab key, this is still a setup
//        DBObject QuestionObjects = coll.findOne(new BasicDBObject("_id", new ObjectId(message)) , keys ); //the actual mongo query
		DBObject QuestionObjects = coll.findOne(new BasicDBObject("_id", new ObjectId(message))); //the actual mongo query //markasparitalObject?
        System.out.println("Questions and answers = " + QuestionObjects);
        
        //grab data from json object
        JsonNode rootNode = mapper.readValue(QuestionObjects.toString().getBytes("UTF-8"), JsonNode.class);
        JsonNode Questions = rootNode.get("Questions");
//        JsonNode toplvl = rootNode.get("NumberOfStudents");
	       int numofstudents = rootNode.get("NumberOfStudents").getIntValue(); //grab the number of students 
//	       System.out.println("Numer of students = " + numofstudents);
	       
	       
	       //this list is teh a,b,c,d order
			List<Integer> SelectionRandomOrderlist = new ArrayList<Integer>();
			SelectionRandomOrderlist.add(0);//a
			SelectionRandomOrderlist.add(1);//b
			SelectionRandomOrderlist.add(2);//c
			SelectionRandomOrderlist.add(3);//d
			Collections.shuffle(SelectionRandomOrderlist);//this randomizes the above list.
	       
	       //VERSION 1
		//These loops will create a Question Object for every question in the QuestionObjects that was grabbed using the objectID for the test we wanted
	       Question[] QA = new Question[(Questions.size())];	
	       //Formatting the question array
	        for(int i = 0; i < Questions.size(); i++ ){ //i is the question number
	        	
	        	Question Q = new Question();
	        	
	        	String Quest = new String(Questions.get(i).get("Questionhtml").getTextValue()); //grab the question 
	        	Q.setQuestion(StringEscapeUtils.unescapeHtml(Quest));
	    		String CA = new String(Questions.get(i).get("CorrectAnswertext").getTextValue()); //grab correct answer
	    		Q.setAnswer(CA);
	        	
//	    		System.out.println("Question = " + Q.Question);
	    		System.out.println("Answer = " + Q.Answer);
	    		
	    		Collections.shuffle(SelectionRandomOrderlist);//this randomizes the selection list order
	        	int WA_num = Questions.get(i).get("WrongAnswers").size();//number of wrong answers
	        	int PA_num = 4;
	        	String[] WAA2 = new String[PA_num]; 
	        	String[] WAA = new String[PA_num]; 
	        	WAA2[0] = CA;
	        	for(int j = 0; j < WA_num; j++){ 
//	        		System.out.println("j = " + j);
	        		WAA2[j+1] = new String(Questions.get(i).get("WrongAnswers").get(j).get("WrongAnswertext").getTextValue()); //grab wrong answer
//	        		System.out.println("Wrong Answer = " + WAA[j]);
	        	}//possible answers for loop
	        	
//	        	WAA2[3] = CA;
	        	
	        	
	    		for(int j = 0; j < SelectionRandomOrderlist.size();j++){
//	    			System.out.println("Order " + i + " = " + WAA[SelectionRandomOrderlist.get(i)] );
	    			WAA[j]= WAA2[SelectionRandomOrderlist.get(j)];
	    			if(WAA[j].equals(CA)){
	    				System.out.println("Answer location = " + j);
	    				Q.Answerlocation = j;
	    			}//end of if
	    		}//end of for loop
	        	
	        	
//	        	AnswerSheet AS = new AnswerSheet();
	        	
	        	
	        	
	        	Q.setPossibleAnswer(WAA); //pt wrong answer array in Possible answer array of question i
	        	QA[i] = Q;
	        }//end of num questions for loop
	        
	        
	        
//	        for(int i = 0; i < Questions.size();i++){
//	        	System.out.println("Accessing: answer locations = " + QA[i].Answerlocation);    
//	        }//end of for loop
	        
	        
//	        //VERSION 2
//	        AnswerSheet[] AnswerSheet = new AnswerSheet[(Questions.size())];	
//	        for(int i = 0; i < Questions.size();i++){
//	        	
//	        	
//	        	
//	        	
//	        	
//	        }
//	        
	        
	        
	        
	        
	        
		

		int numofquestions = QA.length;
//		System.out.println("num of q = " + numofquestions);
		
		 BufferedImage bubblgeImage = ImageIO.read(new File("/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/Resources/LargeBubble2.jpg")); //read manually, because it is the bubble object
		 PDDocument doc = new PDDocument(); //create pdf document
		 PDFont Questionfont = PDType1Font.HELVETICA; //set font of pdf

		 
		 
		 JFrame frame = new JFrame(); //window popup //for debuggin
		 
		 ArrayList AnswerLocs = new ArrayList(); //Array of the answer locations
		 BasicDBObject QI_SNTOT = new BasicDBObject();
		 
		 int studentindex = 0;
		 int studetpageindex = 0; //controls the student page recreation

		 for(int x = 0; x < numofstudents; x++){
			 int indivpage = 0;
		     int remaining = numofquestions; //number of questions written = remaining
		     int pageindex = 0 + studetpageindex; //this is the number of pages plus the student page index so the test can be recreated for each student
		     int Questionshift = 0; //
		     while(remaining > 0){
		      
			     doc.addPage( new PDPage() ); //add blank page to doc
//			     System.out.println("pageindex  = " + pageindex);
			     PDPage currentpage = (PDPage)doc.getDocumentCatalog().getAllPages().get(pageindex);//get current page
			     WriteTitleToPDF( doc, currentpage, indivpage); //write test title on top of page
			     
//			     System.out.println("Created page " + pageindex);
//			     System.out.println("remaining =  " + remaining);
			     int i = 0;
			    //CREATE Current PDF Page Loop
					while(i < 7 || remaining < 0){ //remaining is here incase the number of quetions is less than 7
						int Qnum = i+Questionshift + 1;
//						String qrCodeText = "{\"Question\":\""+Integer.toString(Qnum) + "\", \"stud\":\"" + Integer.toString(x)+ "\"}" ; //QR_TEXT
						String qrCodeText = Integer.toString(x) + "_" +  Integer.toString(Qnum);
				        BufferedImage aQRImage = null; //initialize new bufferedimage, this will be the qrcode
				        aQRImage = createQRImage(qrCodeText, sizeofQRCode, "png"); //create buffered image of QRCode
	
				        PDXObjectImage PDQRImage = new PDJpeg(doc, aQRImage); //make image object for PDFbox
				        
				        
				        PDXObjectImage bubble = new PDJpeg(doc, bubblgeImage); //make image object for PDFbox
				        PDPageContentStream contentStream = new PDPageContentStream(doc, currentpage,true,false); //create write stream
				        contentStream.drawImage( PDQRImage,0, (650-105*i) ); //write Qrcode onto pdf
				        contentStream.setFont( Questionfont, 10 );//set font
				        WriteQuestionToPDF(contentStream, i,(i+Questionshift), QA,bubble);//write the Question, answers, and bubbles
				        
				        
//				        //v1 (non array, just a bunch of objects)
//				        BasicDBObject Answer = new BasicDBObject("Answer", 4);
//				        Answer.put("found", 0);
//				        QI_SNTOT.put(qrCodeText, Answer);

				        
				        
//				        //v2 (array of objects)
//				        BasicDBObject Answer = new BasicDBObject("Answer", 4);
//				        Answer.put("found", 0);
//				        BasicDBObject QI_SN = new BasicDBObject(qrCodeText, Answer); //the qrCodeText = 0_1 for question 0 and student 1
//				        QuestionObjects.put("Answers", QI_SN); //v2
//				        AnswerLocs.add(QI_SN); //v2		
			        
				        
				        //v3 (array with single object not object of object)
//				        System.out.println("Accessing: answer locations = " + QA[Qnum].Answerlocation);
//				        System.out.println("Current Question number = " + Qnum);
				        BasicDBObject Answer = new BasicDBObject("Answer", QA[Qnum - 1].Answerlocation); // in need to assert that this is equal and less than 3 but equal and greatr to 0
				        Answer.put("found", 0);
				        Answer.put("IDS",qrCodeText);
				        Answer.put("_id", new ObjectId());
				        Answer.put("selected", null);
				        Answer.put("correct", null);
				        AnswerLocs.add(Answer); //v3

				        
				        
//				        System.out.println("DONE writing question num = "+ (i + Questionshift));
				        contentStream.close();//close stream 
				        remaining--;
//				        System.out.println("remainign = " + remaining);
				        i++;
				        if(remaining == 0){
				        	break;
				        }
					}//end of second while loop     
				pageindex++; //increment next page
				Questionshift = Questionshift + 7;
//				System.out.println("questionshift = " + Questionshift); 
				indivpage++;//this controls the page number shown on bottom 
		     }//end of fir while loop for pages
		     studetpageindex = studetpageindex + pageindex;
		     studentindex++; //add to student index
		     
		 }//end of numofstudents for loop
		doc.save( "/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/Resources/CreatedPDF_Mongo_Random_withScore.pdf"); //save to my file system so i can see it
		//v1
//		QuestionObjects.put("Answers", QI_SNTOT); //v1 
		
		//v2
//		BasicDBObject ANSWERS = new BasicDBObject("Answers", AnswerLocs);
//		QuestionObjects.put("Answers", AnswerLocs);
		
		//v3
//		BasicDBObject ANSWERS = new BasicDBObject("Answers", AnswerLocs);
//		System.out.println("answers object array = " + ANSWERS);
		QuestionObjects.put("TestAnswerSheet", AnswerLocs);
		
//		System.out.println("QI_SN = " ANSWERS.toString() );	
//		for(BasicDBObject dbo : AnswerLocs){
////			System.out.println("QI_SN = " AnswerLocs.get(i).toString());
//		}
		
		
		
		
		
        //create byte outputstream 
		ByteArrayOutputStream f = new ByteArrayOutputStream(); 
		doc.save(f);//save pdf to f
		byte[] mar = f.toByteArray();//put f data in to byte array mar //mar should equal the pdf in byte[] form
		doc.close(); //close the pdf document, free up memory?
		
//		//save the pdf to the gridfs store 
		GridFS gfsPhoto = new GridFS(db, "fs");
		GridFSInputFile gfsFile = gfsPhoto.createFile(mar); //mar is the pdf in byte array form
		gfsFile.setContentType("binary/octet-stream");
		gfsFile.setFilename("CreatedPDF_Mongo_Random_withScore.pdf");
		gfsFile.save();

		
		//save back into our teacherschema json object
		ArrayList CreatedPDF = new ArrayList(); //create the array of data 
		CreatedPDF.add(gfsFile);//put gfsfile into the array
		
		
		
        QuestionObjects.put("CreatedPDF", CreatedPDF); //put it in our json object //this will overide the previous values
        coll.save(QuestionObjects); //save into mongodb
//        
        

        
        
		//grab the file id of the newly created fs.files collection and put it into the test schema
		
		
		

//		//now to check, grab the file and save to a file to i know it is working properly!
//		
//		GridFSDBFile PDF_FILE = gfsPhoto.findOne(new ObjectId("4fd7ef60c10a5b7250617240")); //search db for the pdf file //test object ID 
//		InputStream is = PDF_FILE.getInputStream();
//		
//		//use jpedal to read the inputstream and display file in jframe (WORKING AS IS)
//		PdfDecoder decode_pdf = new PdfDecoder(true);
//		try {
//			decode_pdf.openPdfFileFromInputStream(is,true); //open pdf file 
//		} catch (PdfException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} //file
//		BufferedImage img = null;
//		try {
//			img = decode_pdf.getPageAsImage(1);
//		} catch (PdfException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		  JFrame frame2 = new JFrame("jpedal buffered image");
//			Panel panel = new Panel();
//			frame2.getContentPane().add(new JLabel(new ImageIcon(img)));
//			frame2.pack();
////			frame.setLocationRelativeTo(null);
//			frame2.setVisible(true);
//		
		
		
		

	} //end of main

	
	

	
	private static void PossibleAnswersOrder(String[] WrongAnswers){ //not used
//		byStudent bystudent = new byStudent
		
//		AnswerSheet AS = new AnswerSheet();
		
		
//		List<Integer> Selections = new ArrayList<Integer>();
		
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		list.add(3);
		Collections.shuffle(list);
	
		
		for(int i = 0; i < list.size();i++){
			System.out.println("Order " + i + " = " + list.get(i));
		}
	
		
	}//end of PossibleAnswersOrder
	
	
	
	private static void WriteTitleToPDF(PDDocument doc, PDPage page, int pageindex) throws IOException{
		
		//chose font
//	     PDFont font = PDTrueTypeFont.loadTTF( doc, new File( "/Library/Fonts/Khmer Sangam MN.ttf" ) );
//	     PDFont font = PDTrueTypeFont.loadTTF( doc, new File( "/Library/Fonts/Microsoft/Franklin Gothic Book.ttf" ) );
	     PDFont font = PDType1Font.HELVETICA_BOLD; //set font of pdf
		
	     //CREATE TITLE OF TEST
	     String title = "Page title for this test!";
	     int marginTop = 10;
	     PDPageContentStream stream = new PDPageContentStream(doc, page,true,true);
	     stream.setFont( font, 16 );
	     int fontSize = 16; // Or whatever font size you want.
	     float titleWidth = font.getStringWidth(title) / 1000 * fontSize;
	     float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
	    
	     //draw the title of the page
	     stream.beginText();
	     stream.moveTextPositionByAmount((page.getMediaBox().getWidth() - titleWidth) / 2, page.getMediaBox().getHeight() - marginTop - titleHeight);
	     stream.drawString(title);
	     stream.endText();
	     stream.close();
	     
	     
	     
	   //draw the page number on the bottom right
	     //NOTE pageindex starts at zero hence i add 1
	     int pagenum = pageindex + 1;
	     PDFont pagenumfont = PDType1Font.HELVETICA; //set font of pdf
	     stream.setFont( pagenumfont, 8 );
	     stream.beginText();
	     stream.moveTextPositionByAmount(580,10);
	     stream.drawString("Page " + Integer.toString(pagenum));
	     stream.endText();
	     stream.close();
	     
	}//end of writetitletopdf
	
	
	
	
	
	
	
	
	private static void WriteQuestionToPDF(PDPageContentStream mycontentStream, int i, int Qi, Question[] QA, PDXObjectImage bubble) throws IOException{
		List<String> linestrings = ParseLine(QA[Qi].Question); 
		String Qnum = Integer.toString(Qi + 1);
		int linebuff = 0;
		int numindex = 0;
	     for(String textline : linestrings){
//	    	 System.out.println(line);
	    	 if(linestrings.size() > 1){
		 		mycontentStream.beginText();
		 		mycontentStream.moveTextPositionByAmount(105 , ((735)-(105*i-linebuff))); //position of the question text 
		 		if(numindex == 0){
		 			mycontentStream.drawString(Qnum + ". "); //the question number
		 		}
		 		mycontentStream.drawString(textline);
		 		mycontentStream.endText();
	 			linebuff = linebuff - 15 ;
	 			numindex++;
	 		  }//end of if
	    	 else{
		 		mycontentStream.beginText();
		 		mycontentStream.moveTextPositionByAmount(105 , ((725-linebuff)-(105*i-linebuff))); //position of the question text 
		 		mycontentStream.drawString(Qnum + ". ");
		 		mycontentStream.drawString(textline);
		 		mycontentStream.endText();
	    	 }
	     }//end of linestring for
	     
	     
	     //now write the possilbe answers AND correct answer
		int shiftint = 0;
		for(String s: QA[Qi].PossibleAnswers){ 
			mycontentStream.drawImage(bubble, 100, (700-18*shiftint - 105*i)); //position of the bubble
			mycontentStream.beginText();
			mycontentStream.moveTextPositionByAmount(125 , (705-18*shiftint - 105*i) );//position of the possible answer text        //var*shiftint : var i the distance between questions
			mycontentStream.drawString(ReturnQuestionLetter(shiftint) + " " + QA[Qi].PossibleAnswers[shiftint] );
			mycontentStream.endText();
			shiftint = shiftint + 1;
		}//end of possible answers for loop	
		
		
//		//write the correct answer
//		mycontentStream.drawImage(bubble, 100, (700-18*shiftint - 105*i)); //position of the bubble
//		mycontentStream.beginText();
//		mycontentStream.moveTextPositionByAmount(125 , (705-18*shiftint - 105*i) );//position of the possible answer text        //var*shiftint : var i the distance between questions
//		mycontentStream.drawString(ReturnQuestionLetter(shiftint) + " " + QA[Qi].Answer );
//		mycontentStream.endText();
		
		
		
	}//end of writequtesiotntopdf
	
	
	
	
	private static List<String> ParseLine(String Text){		
		//check for how long a line 
		String[] olines = new String[3];
		List<String> output = new ArrayList();
//		System.out.println("size of question " + Text.length());
		if(Text.length() > 74){//74 is the maximum character length to the wall for a line
			String[] words = Text.split(" ");
			String ostring = "";
			int index = 0;
			int iteration = 0;
			boolean leave = false;
			//System.out.println(words.length);
			for(String word: words){
//					System.out.println("comparison = " + (ostring.length() + word.length()));
					if((ostring.length() + word.length()) < 74 && (words.length != (iteration+1))){
						ostring = ostring + " " + word;					
					}//end of if
					else if (words.length == (iteration+1)){ //when escaping for loop
						olines[index] = ostring + " " + word;
						output.add(ostring);
//						System.out.println("out = '" + olines[index] + "' ::size = " + olines[index].length());
					}
					else{

						olines[index] = ostring.substring(1);
						output.add(ostring.substring(1));
//						System.out.println("out = '" + olines[index] + "' ::size = " + olines[index].length());
						index++;
						ostring = word;
					}//end of else
					iteration++;
			}//end of for loop
		}//end of if
		else {
//			olines[0] = Text;//means that the questions is on a single line
			output.add(Text);
		}
		return output;
	}//end of writeline
	
	
	private static String ReturnQuestionLetter(int myselect ){
		
		String OutString = null;
		switch (myselect){
			case 0: OutString = "A)";
					break;
			case 1: OutString = "B)";
					break;
			case 2: OutString = "C)";
					break;		
			case 3: OutString = "D)";
					break;
			default: OutString = "ERROR DO NOT USE THIS PAPER!!!! ";
					break;
		}//end of swtich
		return OutString;
	}//end of RETURNQUESTIONLETTER
	
	
	
	
	private static BufferedImage createQRImage(String qrCodeText, int size, String fileType) throws WriterException, IOException {
        // Create the ByteMatrix for the QR-Code that encodes the given String
        Hashtable hintMap = new Hashtable();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText,
                BarcodeFormat.QR_CODE, size, size, hintMap);
        // Make the BufferedImage that are to hold the QRCode
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();
 
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        // Paint and save the image using the ByteMatrix
        graphics.setColor(Color.BLACK);
 
        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
//        ImageIO.write(image, fileType, qrFile);
        return image;
    }//end of CreateQRImage

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}//end of class
