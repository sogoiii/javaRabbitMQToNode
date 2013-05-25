package TestingStuff;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvGetImage;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_features2d.cvSURFParams;
import static com.googlecode.javacv.cpp.opencv_features2d.cvSURFPoint;
import static com.googlecode.javacv.cpp.opencv_features2d.cvSURFPoint;
import static com.googlecode.javacv.cpp.opencv_features2d.CvSURFParams;
import static com.googlecode.javacv.cpp.opencv_features2d.CvSURFPoint;
import static com.googlecode.javacv.cpp.opencv_features2d.cvExtractSURF;
import static com.googlecode.javacv.cpp.opencv_features2d.SurfAdjuster;
import static com.googlecode.javacv.cpp.opencv_features2d.SurfDescriptorExtractor;
import static com.googlecode.javacv.cpp.opencv_features2d.SurfFeatureDetector;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;

import com.googlecode.javacv.cpp.opencv_core.IplImage;



public class identifyMarks {

	
	
	public static void main(String[] args) throws IOException, InterruptedException, PdfException{ //is - is the inputstream of the pdf file
	
		
	      PdfDecoder dImg1 = new PdfDecoder(true);
	      PdfDecoder dImg2 = new PdfDecoder(true);
	      try{
	    	  dImg1.openPdfFile("/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/Resources/box.pdf");
	    	  dImg2.openPdfFile("/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/Resources/box_in_scene.pdf");
	      }
	      catch(PdfException e) {
			    e.printStackTrace();//return back and do the rpc to the user ... use return and check returns?
	      }	      
	      
	      
    	BufferedImage bufImg1 = dImg1.getPageAsImage(1); //production?
    	IplImage IplImg1 = IplImage.createFrom(bufImg1);//convert subimage into iplimage
	      
    	BufferedImage bufImg2 = dImg2.getPageAsImage(1); //production?
    	IplImage IplImg2 = IplImage.createFrom(bufImg2);//convert subimage into iplimage
    	
	    CvMat img_1 = new CvMat();
	    cvGetMat(IplImg1, img_1, null, 0);
		
	    CvMat img_2 = new CvMat();
	    cvGetMat(IplImg2, img_2, null, 0);
    	
	    int minHessian = 400;

	    SurfFeatureDetector surf = new SurfFeatureDetector();
	    KeyPoint keypoints = new KeyPoint();
//	    surf.de
	    
	    
	      
//	    int numpages = dImg1.getPageCount(); //get page numbers for for loop
//	    System.out.println("num pages = " + numpages);
//	    for(int i = 0; i < numpages;i++){ //for every page
//	    	
//	    	BufferedImage PDF_img = dImg1.getPageAsImage(i+1); //production?
//	    	IplImage ipl_PDF_img = IplImage.createFrom(PDF_img);//convert subimage into iplimage
//	    	
//	    	
//	    	
//	    	
//		      int rows = PDF_img.getWidth();
//		      int cols = PDF_img.getHeight();
//
//////		      Mat newMat = new Mat(rows,cols,CV_16UC1);
//////		      CvMat newMat = new CvMat(rows, cols,CV_16UC1);
////		      IplImage tmpImage = cvCreateImage(ipl_PDF_img.cvSize(), IPL_DEPTH_8U, 1);
////		      cvGetImage(ipl_PDF_img, tmpImage);
////		      
//////		      CvMat searchImage = new CvMat();
//////	          cvGetMat(tmpImage, searchImage, null, 0);
//	          
//		      
//		      CvMat img_1 = new CvMat();
//		      cvGetMat(ipl_PDF_img, newMat, null, 0);
//		      
//		      
//		      
//
//	          
//	   
//	    	
//	    	
//			 System.out.println("for loop i = " + i);
//	    };//endo f for loop numpages
		
		
		
		System.out.println("The program will exit now");
		
	};//endof main
	
	

	







};//end of identifyMarks class
