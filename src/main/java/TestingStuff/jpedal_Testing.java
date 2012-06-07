package TestingStuff;

import java.awt.Graphics;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.im4java.core.IM4JavaException;
import org.jpedal.PdfDecoder;
import org.jpedal.examples.PageCount;
import org.jpedal.exception.PdfException;
import org.jpedal.fonts.FontMappings;





public class jpedal_Testing {
	
	
	public static void main(String[] args) throws IOException {
		
		/**instance of PdfDecoder to convert PDF into image*/
		PdfDecoder decode_pdf = new PdfDecoder(true);

		/**set mappings for non-embedded fonts to use*/
		FontMappings.setFontReplacements();
		


		/**open the PDF file - can also be a URL or a byte array*/
		try {
			JFrame frame = new JFrame("jpedal buffered image");
		    	decode_pdf.openPdfFile("/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/src/main/java/RPC/OutputWithBubbles.pdf"); //file
				//decode_pdf.openPdfFile("C:/myPDF.pdf", "password"); //encrypted file
				//decode_pdf.openPdfArray(bytes); //bytes is byte[] array with PDF
				//decode_pdf.openPdfFileFromURL("http://www.mysite.com/myPDF.pdf",false);
		    	
		    	/**get page 1 as an image*/
		    	//page range if you want to extract all pages with a loop
		    	//int start = 1,  end = decode_pdf.getPageCount();
		    	BufferedImage img = decode_pdf.getPageAsImage(2);
		    	System.out.println("image type = " + img.getType());
		    	System.out.println("size of pdf = " + decode_pdf.getPDFHeight());
			
			/**close the pdf file*/
			decode_pdf.closePdfFile();
			
			
			BufferedImage grayimage = new BufferedImage(decode_pdf.getPDFWidth(), decode_pdf.getPDFHeight(), BufferedImage.TYPE_BYTE_GRAY);  
			Graphics g = grayimage.getGraphics();  
			g.drawImage(img, 0, 0, null);  
			
			
			
			System.out.println("Meg used="+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/(1000*1000)+"M");
			
			
			g.dispose();
			
			File fileToSave = new File("/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/src/main/java/RPC/jpedalimage2.jpg");
			ImageIO.write(img, "jpg", fileToSave);
			System.out.println("Meg used="+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/(1000*1000)+"M");
			Panel panel = new Panel();
			frame.getContentPane().add(new JLabel(new ImageIcon(img)));
			frame.pack();
//			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			System.out.println("Meg used="+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/(1000*1000)+"M");
			System.out.println("finnished reading image");
		} catch (PdfException e) {
		    e.printStackTrace();
		}
}//end of main
		
	 /**user dir in which program can write*/
	  private String user_dir = System.getProperty( "/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/src/main/java/RPC" );
	  
	  /**sample file which can be setup - substitute your own. */
	  private static String test_file = "/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/src/main/java/RPC/OutputWithBubbles.pdf";


	  
	  //////////////////////////////////////////////////////////////////////////
//	  /**example method to open a file and return the number of pages*/
//	  public PageCount( String file_name ) 
//	  {
//	    String separator = System.getProperty( "file.separator" );
//	    
//	    //check output dir has separator
//	    if( user_dir.endsWith( separator ) == false )
//	      user_dir = user_dir + separator;
//	    
//	    /**
//	     * set up PdfDecoder object telling
//	     * it whether to display messages
//	     * and where to find its lookup tables
//	     */
//	    PdfDecoder decode_pdf = null;
//	    
//	    //PdfDecoder returns a PdfException if there is a problem
//	    try
//	    {
//	      decode_pdf = new PdfDecoder( false ); //false as no GUI display needed
//	      
//	      /**
//	       * open the file (and read metadata including pages in  file)
//	       */
//	      System.out.println( "Opening file :" + file_name );
//	      decode_pdf.openPdfFile( file_name );
//	      
//	      /**get page number*/
//	      System.out.println( "Page count=" + decode_pdf.getPageCount() );
//
//	      /**close the pdf file*/
//	      decode_pdf.closePdfFile();
//	      
//	    }
//	    catch( Exception e )
//	    {
//	      System.err.println( "5.Exception " + e + " in pdf code" );
//	      
//	    }
//	    
//	    
//	  }


	
	
	
}//end of jpedal_testing class
