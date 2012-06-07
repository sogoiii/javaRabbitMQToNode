package TestingStuff;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.Stream2BufferedImage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import com.sun.pdfview.decode.
public class PDD_ListPages {

	
	
	
	public static void main(String[] args) throws Exception {
		
		File PDF_file = new File("/Users/angellopozo/Documents/workspace/JavaCV_DecodeBaseV1/Quiz_template_V2.pdf");
		PDDocument doc = PDDocument.load(PDF_file);
		List<PDPage> pages = doc.getDocumentCatalog().getAllPages();
		
		System.out.println("number of pages PDD_LISTPAGES = " + pages.size());
		for (int i = 0; i < pages.size(); i++) {
			 PDPage singlePage = pages.get(i);
			 //BufferedImage buffImage = singlePage.convertToImage();
			// ImageIO.write(buffImage, "jpg", new File(PDF_file.getAbsolutePath() + "_" + i +".jpg"));
		}
		
		
		//convert page to PDF
//		BufferedImage PDF_img = ConvertPageToImage(PDF_file,i);
	
	 
	 
	}//end of main 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 public static BufferedImage ConvertPageToImage(File afile,int i) throws IOException, InterruptedException, IM4JavaException{
			
		 IMOperation op = new IMOperation();		  
		  op.addImage(afile.getAbsolutePath()+"["+i+"]"); //look at myscript.sh
		  op.colorspace("RGB");
		  op.type("TrueColor");
		  op.addImage("jpg:-"); //place holder for output file

		 // op.alpha("off"); //this command is slow and not needed anymore
		//  op.interpolate("spline");
		//  op.colorspace("RGB");
		  ConvertCmd convert = new ConvertCmd();
		  Stream2BufferedImage s2b = new Stream2BufferedImage();
		  convert.setOutputConsumer(s2b);
		  convert.run(op);
		  convert.createScript("myscript.sh",op);//debug`` line
		  BufferedImage aPDF_img = s2b.getImage(); //PDF_img is the pdf_image
		  ImageIO.write(aPDF_img,"PNG",new File("Page_"+ (i+1) +"of_PDF.png"));//write whole pdf page to png
		return aPDF_img;
	}//end of ConvertPagetoImage`
	 
	 
	 
	 
	 
}//end of PDD_ListPages
