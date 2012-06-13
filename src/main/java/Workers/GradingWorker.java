package Workers;


import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvCircle;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSeqElem;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
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
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.Stream2BufferedImage;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFTextStripper;


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
import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.CvPoint3D32f;
import com.googlecode.javacv.cpp.opencv_core.CvScalar;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.gridfs.GridFSDBFile;

public class GradingWorker {

	//static DBCollection coll = null; //name of test collection
	static ObjectMapper mapper = null;
	
	
	
	
	public void Grader(InputStream is) throws IOException, InterruptedException, IM4JavaException, PdfException{ //is - is the inputstream of the pdf file
		System.out.println("inside grader");
		
		 //workign with jpedal, will read from inputstream
	      PdfDecoder decode_pdf = new PdfDecoder(true);
	      try{
	      decode_pdf.openPdfFileFromInputStream(is,true); //file
//	      BufferedImage img = decode_pdf.getPageAsImage(1);
//	      decode_pdf.closePdfFile();
//	      File fileToSave = new File("/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/src/main/java/RPC/jpedalRPCTEST1.jpg");
//		  ImageIO.write(img, "jpg", fileToSave);
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
		
//		   JFrame frame = new JFrame(); //window popup 
		    for(int i = 1; i <= numpages;i++){ //for every page
		    	
//		    	File PDF_file = new File("/Users/angellopozo/Documents/TestImages/PDF_CRICLEV2.pdf");
		    	//convert page to PDF
//				 BufferedImage PDF_img = ConvertPageToImage(PDF_file,i);
		    	 BufferedImage PDF_img = decode_pdf.getPageAsImage(i);
				  
				  
				//START creating luminance source
					 LuminanceSource lumSource = new BufferedImageLuminanceSource(PDF_img);
					 BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(lumSource));		
					 
					 Reader reader = new QRCodeReader(); //create qr reader
					 GenericMultipleBarcodeReader multireader = new GenericMultipleBarcodeReader(reader); 
				  
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
					 int index = 0;//debug line to save images
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
						    System.out.println("barcode location: " + x1 +" "+ y1 +" "+ x2 +" "+ y2 + " " + x3 +" "+ y3);
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
							
							
							//CanvasFrame smoothed = new CanvasFrame("gray image");
							//smoothed.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
							//smoothed.showImage(ipl_subBubble_gray);
							
		
							CvSeq seq = cvHoughCircles(ipl_subBubble_gray, circles, CV_HOUGH_GRADIENT,
	                                				1, 50, 
	                                				80, 20, 
	                                				32,  (int)(ipl_subBubble_gray.height()/(7)));
							Integer[][] FilledBubbles = new Integer[4][4]; //arry holds the #of pixels seen and the y dimention of subimage
							Vector<CvPoint> centers = new Vector<CvPoint>(4);//the 4 can be seq.total()
							for(int j=0; j<seq.total(); j++){ //draw a circle around each circle found
						        CvPoint3D32f xyr = new CvPoint3D32f(cvGetSeqElem(seq, j));
						        CvPoint center = new CvPoint(Math.round(xyr.x()), Math.round(xyr.y()));
						        int radius = Math.round(xyr.z());
						        cvCircle(ipl_subBubble_large, center, 3, CvScalar.GREEN, -1, 8, 0);//center of circle
						        cvCircle(ipl_subBubble_large, center, radius, CvScalar.BLUE, 3, 8, 0);//outer circle
						        FilledBubbles[j][0] = FindBubbleSelected(center, radius, ipl_subBubble_gray);
						        FilledBubbles[j][1] = Math.round(center.x());
						        FilledBubbles[j][2] = Math.round(center.y());
						        FilledBubbles[j][3] = Math.round(radius);
						        //System.out.println("Filled bubble Count = "+ FilledBubbles[j]);
							}//end of look for circles for
							
							
							
//							System.out.println("filled bubbles size = " + FilledBubbles[0].length);
//							System.out.println("filled bubbles size = " + FilledBubbles.length);
					        FilledBubbles = SortbyYdimention(FilledBubbles); 		     //note to self, check for nulls because that woud be an issue....   
//					        
//					        for(Integer[] tp : FilledBubbles){
//					        	System.out.println("Filled bubble Count = "+ tp[0] + " loc = "+ tp[1]);
//					        }
//					        
//					        int maxIndex = ReturnIndexOfmax(FilledBubbles);
//					        
//					        CvPoint slectedcenter = new CvPoint(FilledBubbles[maxIndex][1].intValue(),FilledBubbles[maxIndex][2].intValue());
//					        cvCircle(ipl_subBubble_large,slectedcenter,FilledBubbles[maxIndex][3].intValue(), CvScalar.RED, 3,8,0);
//					        	
//					  
//							
//							
//							
//							String subimagename = new String("subimage_"+i+"_"+index+".jpg");
//							index++;
//							cvSaveImage(subimagename,ipl_subBubble_large);
//							// create image window named "My Image"
//							String que = new String("_for_"+ result.getText());
//						    final CanvasFrame canvas = new CanvasFrame("Bubbles_Found"+que);
//						 // request closing of the application when the image window is closed
//						    canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
//						 // show image on window
//						    canvas.showImage(ipl_subBubble_large);
							
							
						    
					
					}//end of for results loop
				//end drawing boxes around each QR CODE
					
//					//START code to display in JFRAME
//					if(i == 0){
//			       frame.getContentPane().setLayout(new FlowLayout());
//			       frame.getContentPane().add(new JLabel(new ImageIcon(outimage)));
//			       frame.pack();
//			       frame.setVisible(true);
//					}
//					else {
//						
//						frame.getContentPane().add(new JLabel(new ImageIcon(outimage)));
//				        frame.pack();
//				        frame.setVisible(true);
//						
//					}  
//					//END code to display in JFRAME
					
		
		    }//end of for loop of pages
		    
		
		
		
		
		
	}//end of Grader
		
//	
//	public static BufferedImage ConvertPageToImage(PDDocument doc, int i){
//		 IMOperation op = new IMOperation();
//		  op.addImage(afile.getAbsolutePath()+"["+i+"]"); //look at myscript.sh
//		  op.colorspace("RGB");
//		  op.type("TrueColor");
//		  op.addImage("jpg:-"); //place holder for output file		 
//		 
//		  ConvertCmd convert = new ConvertCmd();
//		  Stream2BufferedImage s2b = new Stream2BufferedImage();
//		  convert.setOutputConsumer(s2b);
//		  convert.run(op);
//		  convert.createScript("myscript.sh",op);//debug`` line
//		  BufferedImage aPDF_img = s2b.getImage(); //PDF_img is the pdf_image
//		 
//		 
//		
//		return aPDF_img;
//	}
	
	
	
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
	
	
	
	 public static int FindBubbleSelected(CvPoint cent, int r, IplImage bubbles){
		int FilledArea = 0;
		int xl = cent.x() - r;//top left x
		int yl = cent.y() - r;//top left y
		int xr = cent.x() + r;//bottom right x
		int yr = cent.y() + r;//bottom right y
		
		CvScalar c = null;
		//search area around bubble and sum pixels found
		for(int i = xl; i < xr; i++){
			for(int j = yl; j < yr; j++){
				c = cvGet2D(bubbles, j,i);
				if(c.val(0) < 100){ //since image is gray only 0 needs to be looked at
					FilledArea++;
				}//end of pixel intensity 
			}//end of y loop
		}//end of x loop
		
	
		return FilledArea;
	}//end of FindBubbleSelected
	
	
	
	 public static int ReturnIndexOfmax(Integer[][] ar){
			int max = ar[0][0];
			int index = 0;
			int count = 0;
			for(Integer[] inter : ar){
				if(inter[0]>max){
					max = inter[0];
					index = count;
				}
				count++;
			}
			
			System.out.println("Max found in this array = "+index);
			
			return index;
		}//end of ReturnIndexOfMax
		
	 
	 
		public static Integer[][] SortbyYdimention(Integer[][] ar){
			System.out.println("ar size = " + ar.length);
			for(int i = 0; i < ar.length; i++ ){
				System.out.println("ar[i] = " + ar[i][0]);
			}
			
			Arrays.sort(ar, new Comparator<Integer[]>() {
	            public int compare(Integer[] entry1, final Integer[] entry2) {
	                Integer time1 = entry1[2];
	                Integer time2 = entry2[2];
	                System.out.println("(time1, time2) = (" + time1 + "," + time2 +")");
	                return time1.compareTo(time2);
	            }//end of new comparator
	        });
			return ar;
		}//end of my sort
	
	
	
	
	
	public void test(String input){
		System.out.println("inside grader received = " + input);
	}//end of test
}//end of GradingWorker
