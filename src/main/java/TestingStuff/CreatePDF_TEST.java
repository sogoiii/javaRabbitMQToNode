package TestingStuff;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;









public class CreatePDF_TEST {
	
	static int sizeofQRCode = 100; //this is teh size of the QR code 

	
	String testrepo = "/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/Resources/";
	
	public static void main(String[] args) throws WriterException, IOException, COSVisitorException {
		
		//questions These will come from the database in probably in BSON if mongoDB, JSON if CouchDB
		int numofquestions = 7; 
		
		Question Q1 = new Question();
		Q1.setQuestion("Who was the First President of the United States?");
		Q1.setAnswer("George Washington");
		String[] Q1PA = {"Thomas Jefferson","Madison", "Benjamin Franklin"};
		Q1.setPossibleAnswer(Q1PA);
		
		Question Q2 = new Question();
		Q2.setQuestion("Who was the 16th President of the United States?");
		Q2.setAnswer("Abraham Lincoln");
		String[] Q2PA = {"Anderw Johnson","Franklin Pierce", "John Tayler"};
		Q2.setPossibleAnswer(Q2PA);
		
		Question Q3 = new Question();
		Q3.setQuestion("The alternative approach is to create a new BufferedImage and and draw a sc");
		Q3.setAnswer("Abraham Lincoln");
		String[] Q3PA = {"Anderw Johnson","Franklin Pierce", "John Tayler"};
		Q3.setPossibleAnswer(Q3PA);
		
		Question Q4 = new Question();
		Q4.setQuestion("you can convert it to a BufferedImage by copying its raster to a new BufferedImage. Serach for 'convert image to bufferedimage' ");
		Q4.setAnswer("Abraham Lincoln");
		String[] Q4PA = {"Anderw Johnson","Franklin Pierce", "John Tayler"};
		Q4.setPossibleAnswer(Q4PA);
		
		Question Q5 = new Question();
		Q5.setQuestion("Who was the 16th President of the United States?");
		Q5.setAnswer("Abraham Lincoln");
		String[] Q5PA = {"Anderw Johnson","Franklin Pierce", "John Tayler"};
		Q5.setPossibleAnswer(Q5PA);
		
		
		Question Q6 = new Question();
		Q6.setQuestion("Who was the 16th President of the United States?");
		Q6.setAnswer("Abraham Lincoln");
		String[] Q6PA = {"Anderw Johnson","Franklin Pierce", "John Tayler"};
		Q6.setPossibleAnswer(Q6PA);
		
		Question Q7 = new Question();
		Q7.setQuestion("Who was the 16th President of the United States?");
		Q7.setAnswer("Abraham Lincoln");
		String[] Q7PA = {"Anderw Johnson","Franklin Pierce", "John Tayler"};
		Q7.setPossibleAnswer(Q7PA);
		
		Question[] QA = new Question[7];		
		QA[0] = Q1;
		QA[1] = Q2;
		QA[2] = Q3;
		QA[3] = Q4;
		QA[4] = Q5;
		QA[5] = Q6;
		QA[6] = Q7;
		
		numofquestions = QA.length;
		System.out.println("num of q = " + numofquestions);
		
		 BufferedImage bubblgeImage = ImageIO.read(new File("/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/Resources/LargeBubble2.jpg"));
		
//		 PDDocument doc = PDDocument.load(new File("/Users/angellopozo/Documents/workspace/CreatePDF_TestV1/mycreation2.pdf"));
		 PDDocument doc = new PDDocument();
		 PDPage blankPage = new PDPage(); //intialize blank page
		 doc.addPage( blankPage ); //add blank page to doc
		 
	     PDPage page = (PDPage)doc.getDocumentCatalog().getAllPages().get(0);//get first page //at least start here!
	   
	     PDFont font = PDType1Font.HELVETICA_BOLD; //set font of pdf
	     
	     
	     String title = "page title for this test!";
	     int marginTop = 15;
	     PDPageContentStream stream = new PDPageContentStream(doc, page,true,true);
//	     PDFont font = PDType1Font.HELVETICA_BOLD; // Or whatever font you want.
	     stream.setFont( font, 12 );

	     int fontSize = 16; // Or whatever font size you want.
	     float titleWidth = font.getStringWidth(title) / 1000 * fontSize;
	     float titleHeight = font.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * fontSize;
	     
	     float var1 = (page.getMediaBox().getWidth() - titleWidth) / 2;
	     float var2 = page.getMediaBox().getHeight() - marginTop - titleHeight;
//	     System.out.println("comp = "+ var1 + " and " + var2);
	     stream.beginText();
	     stream.moveTextPositionByAmount((page.getMediaBox().getWidth() - titleWidth) / 2, page.getMediaBox().getHeight() - marginTop - titleHeight);
	     stream.drawString(title);
	     stream.endText();
	     stream.close();
	     
//	     System.out.println("size of string text = " + title.length());
//	     System.out.println("sub string 1 = " + title.substring(0, 8));
//	     System.out.println("sub string 1 = " + title.substring(8, 14));
//	     
//	     String[] words = title.split(" ");  
//	     for (String word : words){
//	    	 System.out.println(word + "has lenght = " + word.length()); 
//	     }
	     
	     
	     String testline = "You can convert it to a BufferedImage by copying its raster to a new BufferedImage. Serach for 'convert image to bufferedimage";
	     List<String> linestrings = WriteLine(testline);
	     for(String word : linestrings){
	    	 System.out.println(word);
	     }
	     
//	     String linestring[] = WriteLine(testline);
//	     for(int i = 0; i < linestring.length;i++){
//	    	 System.out.println(linestring[i]);
//	     }
	     
//	     System.out.println("linestring = " + linestring[0]);
//	     int entry = linestring[0].length();
//	     System.out.println("Grab string from: (" + entry + " to "+ testline.length() + ")");
//	     String remain = testline.substring(entry,testline.length());
//	     System.out.println("remaining string = " + remain);
	     
//		for(int i = 0; i < numofquestions; i++){
//			String qrCodeText = "Question "+i;
//	        String filePath = "/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/Resources/Quest"+i+".png";
//	        
//	        String fileType = "png";
//	        File qrFile = new File(filePath);
//	        BufferedImage aQRImage = null;
//	        aQRImage = createQRImage(qrFile, qrCodeText, sizeofQRCode, fileType);
//	        System.out.println("DONE with "+i);
//	        
//	        PDXObjectImage QRImage = new PDJpeg(doc, aQRImage);
//	        PDXObjectImage bubble = new PDJpeg(doc, bubblgeImage);
//	        PDPageContentStream contentStream = new PDPageContentStream(doc, page,true,true);
//	        
//	        contentStream.drawImage( QRImage, 30, (620-120*i) );
//	        contentStream.setFont( font, 12 );
//
//	        WriteQuestionToPDF(contentStream, i, QA,bubble);
//	        
//	        
//	        contentStream.close();
//		}//end of for loop
	
		//contentStream.close();
		doc.save( "/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/Resources/CreatedPDF_TEST.pdf");
		doc.close();
		
		
		
		
	
	}//end of main
	
	
	
	
	private static void WriteQuestionToPDF(PDPageContentStream mycontentStream, int i, Question[] QA, PDXObjectImage bubble) throws IOException{
		mycontentStream.beginText();
		mycontentStream.moveTextPositionByAmount(150 , (715-120*i) );        
		mycontentStream.drawString(QA[i].Question );
		mycontentStream.endText();
		System.out.println("size of question " + QA[i].Question.length());
		 
		 
		
		 
		 
		int shiftint = 0;
		for(String s: QA[i].PossibleAnswers){ 
			mycontentStream.drawImage(bubble, 150, (680-20*shiftint - 120*i));
			mycontentStream.beginText();
			mycontentStream.moveTextPositionByAmount(170 , (685-20*shiftint - 120*i) );        
			mycontentStream.drawString(ReturnQuestionLetter(shiftint) + " " + QA[i].PossibleAnswers[shiftint] );
			mycontentStream.endText();
			shiftint = shiftint + 1;
		}
		
	}//end of Write Question to PDF
	
	
	
	private static List<String> WriteLine(String Text){		
		//check for how long a line 
		String[] olines = new String[3];
		List<String> output = new ArrayList();
		System.out.println("size of question " + Text.length());
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
			case 0: OutString = "A) ";
					break;
			case 1: OutString = "B) ";
					break;
			case 2: OutString = "C) ";
					break;		
			case 3: OutString = "D) ";
					break;
			default: OutString = "ERROR DO NOT USE THIS PAPER!!!! ";
					break;
		}//end of swtich
		return OutString;
	}//end of RETURNQUESTIONLETTER
	
	
	
	private static BufferedImage createQRImage(File qrFile, String qrCodeText, int size, String fileType) throws WriterException, IOException { //size = sizeofQRCode
        // Create the ByteMatrix for the QR-Code that encodes the given String
        Hashtable hintMap = new Hashtable();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText,
                BarcodeFormat.QR_CODE, size, size, hintMap);
        // Make the BufferedImage that are to hold the QRCode
        int matrixWidth = byteMatrix.getWidth();
        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth,
                BufferedImage.TYPE_INT_RGB);
        image.createGraphics();
 
        Graphics2D graphics = (Graphics2D) image.getGraphics();
//        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
        // Paint and save the image using the ByteMatrix
        graphics.setColor(Color.BLACK);
//        graphics.scale(.8, .8);
 
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
