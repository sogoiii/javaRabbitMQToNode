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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

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
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;









public class CreatePDF_TEST {
	
	static int sizeofQRCode = 100; //this is the size of the QR code 

	
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
		String[] Q2PA = {"Anderw Johnson","Franklin Pierce", "John Tayler", "whowhatnow?"};
		Q2.setPossibleAnswer(Q2PA);
		
		Question Q3 = new Question();
		Q3.setQuestion("Got response code 502 with body Bad response. The server or forwarder response doesn't look like HTTP.");
		Q3.setAnswer("Abraham Lincoln");
		String[] Q3PA = {"Anderw Johnson","Franklin Pierce", "John Tayler"};
		Q3.setPossibleAnswer(Q3PA);
		
		Question Q4 = new Question();
		Q4.setQuestion("you can convert it to a BufferedImage by copying its raster to a new BufferedImage. Serach for 'convert image to bufferedimage' ");
		Q4.setAnswer("Abraham Lincoln");
		String[] Q4PA = {"Anderw Johnson","Music video by Coldplay performing Paradise. (C) 2011 EMI Records Ltd This label copy information is the subject of copyright protection.", "John Tayler"};
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
		
		Question Q8 = new Question();
		Q8.setQuestion("Who was the 16th President of the United States?");
		Q8.setAnswer("Abraham Lincoln");
		String[] Q8PA = {"Anderw Johnson","Franklin Pierce", "John Tayler"};
		Q8.setPossibleAnswer(Q8PA);
		
		Question Q9 = new Question();
		Q9.setQuestion("Who was the 16th President of the United States?");
		Q9.setAnswer("Abraham Lincoln");
		String[] Q9PA = {"Anderw Johnson","Franklin Pierce", "John Tayler"};
		Q9.setPossibleAnswer(Q9PA);
		
		Question Q10 = new Question();
		Q10.setQuestion("Who was the 16th President of the United States?");
		Q10.setAnswer("Abraham Lincoln");
		String[] Q10PA = {"Anderw Johnson","Franklin Pierce", "John Tayler"};
		Q10.setPossibleAnswer(Q10PA);
		
		
		Question[] QA = new Question[10];		
		QA[0] = Q1;
		QA[1] = Q2;
		QA[2] = Q3;
		QA[3] = Q4;
		QA[4] = Q5;
		QA[5] = Q6;
		QA[6] = Q7;
		QA[7] = Q8;
		QA[8] = Q9;
		QA[9] = Q10;
		
		numofquestions = QA.length;
		System.out.println("num of q = " + numofquestions);
		PDFont Questionfont = PDType1Font.HELVETICA; //set font of pdf
		
		 BufferedImage bubblgeImage = ImageIO.read(new File("/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/Resources/LargeBubble2.jpg"));
		
//		 PDDocument doc = PDDocument.load(new File("/Users/angellopozo/Documents/workspace/CreatePDF_TestV1/mycreation2.pdf"));
		 PDDocument doc = new PDDocument();
//		 doc.addPage( new PDPage() ); //add blank page to doc
		 
//	     PDPage page = (PDPage)doc.getDocumentCatalog().getAllPages().get(0);//get first page //at least start here!
	   
		 
		 
		 
		 
		 JFrame frame = new JFrame(); //window popup 
	     int remaining = numofquestions; //number of questions written = remaining
	     int pageindex = 0; //this is the number of pages
	     int Questionshift = 0; //
	     while(remaining > 0){
	     
		     doc.addPage( new PDPage() ); //add blank page to doc
		     System.out.println("pageindex  = " + pageindex);
		     PDPage currentpage = (PDPage)doc.getDocumentCatalog().getAllPages().get(pageindex);//get current page
		     WriteTitleToPDF( doc, currentpage); //write test title on top of page
		     
		     System.out.println("Created page " + pageindex);
		     System.out.println("remaining =  " + remaining);
		     int i = 0;
		    //CREATE Current PDF Page Loop
				while(i < 7){
					String qrCodeText = "Question "+i;
			        BufferedImage aQRImage = null; //initialize new bufferedimage, this will be the qrcode
			        aQRImage = createQRImage(qrCodeText, sizeofQRCode, "png"); //create buffered image of QRCode

			        PDXObjectImage PDQRImage = new PDJpeg(doc, aQRImage); //make image object for PDFbox
			        BufferedImage testtest = PDQRImage.getRGBImage();
			        
				       frame.getContentPane().setLayout(new FlowLayout());
				       frame.getContentPane().add(new JLabel(new ImageIcon(testtest)));
				       frame.pack();
				       frame.setVisible(true);
			        
			        
			        PDXObjectImage bubble = new PDJpeg(doc, bubblgeImage); //make image object for PDFbox
			        PDPageContentStream contentStream = new PDPageContentStream(doc, currentpage,true,false); //create write stream
			        contentStream.drawImage( PDQRImage,0, (650-105*i) ); //write Qrcode onto pdf
			        contentStream.setFont( Questionfont, 10 );//set font
			        WriteQuestionToPDF(contentStream, i, QA,bubble);//write the Question, answers, and bubbles
			        
			        System.out.println("DONE writing question num = "+i);
			        contentStream.close();//close stream 
			        remaining--;
			        i++;
			        if(remaining == 0){
			        	break;
			        }
				}//end of second while loop     
			pageindex++; //increment next page
			Questionshift = Questionshift + 7;
			System.out.println("questionshift = " + Questionshift); 
			
	     }//end of fir while loop for pages

		doc.save( "/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/Resources/CreatedPDF_TEST_Franklin_Gothic_Book_CTV1_16.pdf");
		
		
		// connect to the local database server
        Mongo m = new Mongo();
        // get handle to "mydb"
        DB db = m.getDB( "TESTGRIDFS" );

		

        //create byte outputstream 
		ByteArrayOutputStream f = new ByteArrayOutputStream(); 
		//OutputStream fout = new FileOutputStream("/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/Resources/CreatedPDF_TEST_Franklin_Gothic_Book_CTV1_16_FOUT.pdf");
		doc.save(f);//save pdf to f
		byte[] mar = f.toByteArray();//put f data in to byte array mar //mar should equal the pdf in byte[] form
		//f.writeTo(fout); //save to fout
		doc.close();
	
		
		//save the pdf to the gridfs store 
		GridFS gfsPhoto = new GridFS(db, "CreatePDFTEST");
		GridFSInputFile gfsFile = gfsPhoto.createFile(mar); //mar is the pdf in byte array form
		gfsFile.setContentType("binary/octet-stream");
		gfsFile.setFilename("Created PDF File in my test java function 2");
		gfsFile.save();

		//now to check, grab the file and save to a file to i know it is working properly!
		
		GridFSDBFile PDF_FILE = gfsPhoto.findOne(new ObjectId("4fd3ade5c10a331fd5c66301")); //search db for the pdf file //test object ID 
		InputStream is = PDF_FILE.getInputStream();
		
		//use jpedal to read the inputstream and display file in jframe (WORKING AS IS)
//		PdfDecoder decode_pdf = new PdfDecoder(true);
//		try {
//			decode_pdf.openPdfFileFromInputStream(is,true); //open pdf file 
//		} catch (PdfException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} //file
//		BufferedImage img = null;
//		try {
//			img = decode_pdf.getPageAsImage(2);
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
		
		
		
		//use pddocument to read from inputstream and save to a file (its as if it only loads a portion of the pdf file kinda like the node code that only loaded a partial image...sigh
		PDDocument fromDBdoc = new PDDocument();
		fromDBdoc.load(is,true);
		fromDBdoc.save("/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/Resources/CreatedPDF_TEST_Franklin_Gothic_Book_CTV1_16_FROMDB.pdf");
		
	
	}//end of main
	
	
	
	
	
	
	
	
	
	
	private static void WriteTitleToPDF(PDDocument doc, PDPage page) throws IOException{
		
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
	    
	     stream.beginText();
	     stream.moveTextPositionByAmount((page.getMediaBox().getWidth() - titleWidth) / 2, page.getMediaBox().getHeight() - marginTop - titleHeight);
	     stream.drawString(title);
	     stream.endText();
	     stream.close();
		
	}//end of writetitletopdf
	
	
	
	
	
	
	
	
	private static void WriteQuestionToPDF(PDPageContentStream mycontentStream, int i, Question[] QA, PDXObjectImage bubble) throws IOException{
		List<String> linestrings = WriteLine(QA[i].Question);
		int linebuff = 0;
	     for(String textline : linestrings){
//	    	 System.out.println(line);
	    	 if(linestrings.size() > 1){
		 		mycontentStream.beginText();
		 		mycontentStream.moveTextPositionByAmount(105 , ((735)-(105*i-linebuff))); //position of the question text 
		 		mycontentStream.drawString(textline);
		 		mycontentStream.endText();
	 			linebuff = linebuff - 15 ;
	 		  }//end of if
	    	 else{
			 		mycontentStream.beginText();
			 		mycontentStream.moveTextPositionByAmount(105 , ((725-linebuff)-(105*i-linebuff))); //position of the question text 
			 		mycontentStream.drawString(textline);
			 		mycontentStream.endText();
	    	 }
	     }//end of linestring for
	     
		int shiftint = 0;
		for(String s: QA[i].PossibleAnswers){ 
			mycontentStream.drawImage(bubble, 100, (700-18*shiftint - 105*i)); //position of the bubble
			mycontentStream.beginText();
			mycontentStream.moveTextPositionByAmount(125 , (705-18*shiftint - 105*i) );//position of the possible answer text        //var*shiftint : var i the distance between questions
			mycontentStream.drawString(ReturnQuestionLetter(shiftint) + " " + QA[i].PossibleAnswers[shiftint] );
			mycontentStream.endText();
			shiftint = shiftint + 1;
		}//end of possible answers for loop	
		
		
	}//end of writequtesiotntopdf
	
	
	
	
	private static List<String> WriteLine(String Text){		
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
	
	
	
//	private static BufferedImage createQRImage(File qrFile, String qrCodeText, int size, String fileType) throws WriterException, IOException {
//	        // Create the ByteMatrix for the QR-Code that encodes the given String
//	        Hashtable hintMap = new Hashtable();
//	        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
//	        QRCodeWriter qrCodeWriter = new QRCodeWriter();
//	        BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText,
//	                BarcodeFormat.QR_CODE, size, size, hintMap);
//	        // Make the BufferedImage that are to hold the QRCode
//	        int matrixWidth = byteMatrix.getWidth();
//	        BufferedImage image = new BufferedImage(matrixWidth, matrixWidth, BufferedImage.TYPE_INT_RGB);
//	        image.createGraphics();
//	 
//	        Graphics2D graphics = (Graphics2D) image.getGraphics();
//	        graphics.setColor(Color.WHITE);
//	        graphics.fillRect(0, 0, matrixWidth, matrixWidth);
//	        // Paint and save the image using the ByteMatrix
//	        graphics.setColor(Color.BLACK);
//	 
//	        for (int i = 0; i < matrixWidth; i++) {
//	            for (int j = 0; j < matrixWidth; j++) {
//	                if (byteMatrix.get(i, j)) {
//	                    graphics.fillRect(i, j, 1, 1);
//	                }
//	            }
//	        }
//	        ImageIO.write(image, fileType, qrFile);
//	        return image;
//	    }//end of CreateQRImage
	
	
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
