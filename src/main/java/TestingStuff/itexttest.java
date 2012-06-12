package TestingStuff;



import java.io.FileOutputStream;
import java.io.IOException;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;




public class itexttest {
	public static final String RESULT = "/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/Resources/itext1.pdf";

	public static void main(String[] args) throws DocumentException, IOException {

		new itexttest().createPdf(RESULT);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}//end of main
	
	
	public void createPdf(String filename)
			throws DocumentException, IOException {
		        // step 1
		        Document document = new Document();
		        // step 2
		        PdfWriter.getInstance(document, new FileOutputStream(filename));
		        // step 3
		        document.open();
		        // step 4
		        document.add(new Paragraph("Hello World!"));
		        // step 5
		        document.close();
		    }
}//end of class
