package TestingStuff;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Map;

import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.edit.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.PDFText2HTML;

public class GrabQuestionsFromMongo_TEST {
	
	
	
	public static void main(String[] args) throws MongoException, JsonParseException, JsonMappingException, UnsupportedEncodingException, IOException, COSVisitorException {
		
		Mongo m = new Mongo();
		DB db = m.getDB("ecomm_database");
		DBCollection coll = db.getCollection("testschemas");
		
		
		
		ObjectMapper mapper = new ObjectMapper(); 
		
		
		String message = "4fcabbe1d41f228a4c000088"; //test id, that i will have in the real version
		BasicDBObject keys = new BasicDBObject(); //create search object, keys will be what is returned
        keys.put("Questions",1); //tell mongo to grab key, this is still a setup
        DBObject QuestionObjects = coll.findOne(new BasicDBObject("_id", new ObjectId(message)) , keys ); //the actual mongo query
        System.out.println("Questions and answers = " + QuestionObjects);
        
        //grab data from json object
        JsonNode rootNode = mapper.readValue(QuestionObjects.toString().getBytes("UTF-8"), JsonNode.class);
        JsonNode Questions = rootNode.get("Questions");
//        String Q0 = new String(Questions.get(0).get("_id").get("$oid").getTextValue());
//        String Q0_question = new String(Questions.get(0).get("Questionhtml").getTextValue()); //grab the question 
//        String Q0_PA1 = new String(Questions.get(0).get("CorrectAnswertext").getTextValue()); //grab correct answer
//        String Q0_WA1 = new String(Questions.get(0).get("WrongAnswers").get(0).get("WrongAnswertext").getTextValue()); //grab correct answer
//        String Q0_WA2 = new String(Questions.get(0).get("WrongAnswers").get(1).get("WrongAnswertext").getTextValue()); //grab correct answer
//        String Q0_WA3 = new String(Questions.get(0).get("WrongAnswers").get(2).get("WrongAnswertext").getTextValue()); //grab correct answer
////        String Q0_WA4 = new String(Questions.get(0).get("WrongAnswers").get(3).get("WrongAnswertext").getTextValue()); //grab correct answer
////        System.out.println("Question Html  = " + Q0_question);
//        String unesc = StringEscapeUtils.unescapeHtml(Q0_question); // re-enter real html code from escaped html code
//        System.out.println("Question Str   = " + unesc);
////        Q0_PA1 = StringEscapeUtils.unescapeHtml(Q0_PA1);
//        System.out.println("Question Correct Answer   = " + Q0_PA1);
////        Q0_WA1 = StringEscapeUtils.unescapeHtml(Q0_WA1);
//        System.out.println("Question Wrong Answer   = " + Q0_WA1);
////        Q0_WA2 = StringEscapeUtils.unescapeHtml(Q0_WA2);
//        System.out.println("Question Wrong Answer   = " + Q0_WA2);       
////        Q0_WA3 = StringEscapeUtils.unescapeHtml(Q0_WA3);
//        System.out.println("Question Wrong Answer   = " + Q0_WA3);       
////        Q0_WA4 = StringEscapeUtils.unescapeHtml(Q0_WA4);
////        System.out.println("Question Wrong Answer   = " + Q0_WA4);      
        
//        int Q_num = Questions.size();
//        int WA_num = Questions.get(0).get("WrongAnswers").size();
//        System.out.println("num of wrong answers = " + WA_num);
//        System.out.println("number of questions  = " + Q_num);
        
        
        Question[] QA = new Question[(Questions.size() +1)];	
        
        for(int i = 0; i < Questions.size(); i++ ){ //i is the question number
        	
        	
        	Question Q = new Question();
        	
        	String Quest = new String(Questions.get(i).get("Questionhtml").getTextValue()); //grab the question 
        	Q.setQuestion(StringEscapeUtils.unescapeHtml(Quest));
    		String CA = new String(Questions.get(i).get("CorrectAnswertext").getTextValue()); //grab correct answer
    		Q.setAnswer(CA);
        	
    		System.out.println("Question = " + Q.Question);
    		System.out.println("Answer = " + Q.Answer);
        	int WA_num = Questions.get(i).get("WrongAnswers").size();
        	String[] WAA = new String[WA_num];
        	for(int j = 0; j < WA_num; j++){    		
        		WAA[j] = new String(Questions.get(i).get("WrongAnswers").get(j).get("WrongAnswertext").getTextValue()); //grab wrong answer
        		System.out.println("Wrong Answer = " + WAA[j]);
        	}//possible answers for loop
        	Q.setPossibleAnswer(WAA); //pt wrong answer array in Possibel answer array of question i
        	QA[i] = Q;
        }//end of num questions for loop
        
        
        
        
        //missing step of remonving html code (i didnt do it because its currently unnecessary to move forward
//        String nohtml = unesc.replaceAll("<*>",  " ");
//        System.out.println("no html    = " + nohtml);
        
        
        
//        //create pdf
//        PDDocument doc = new PDDocument();
//        doc.addPage( new PDPage() );
//        PDPage currentpage = (PDPage)doc.getDocumentCatalog().getAllPages().get(0);//get current page
//        PDPageContentStream contentStream = new PDPageContentStream(doc, currentpage,true,true); //create write stream
//        PDFText2HTML s = new PDFText2HTML(unesc);
//        contentStream.setFont( PDType1Font.HELVETICA, 10 );//set font
//        
//        contentStream.beginText();
//	    contentStream.moveTextPositionByAmount(100, 700); // from bottom left (x,y)
//	    contentStream.drawString(unesc);
//	    contentStream.endText();
//	    contentStream.close();
//        doc.save("/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/Resources/CreatedQUESION.pdf");
        
        
        
        
	}//end of main

	
	
	
	
	
	
	 public static String forXML(String aText){
		    final StringBuilder result = new StringBuilder();
		    final StringCharacterIterator iterator = new StringCharacterIterator(aText);
		    char character =  iterator.current();
		    while (character != CharacterIterator.DONE ){
		      if (character == '<') {
		        result.append("&lt;");
		      }
		      else if (character == '>') {
		        result.append("&gt;");
		      }
		      else if (character == '\"') {
		        result.append("&quot;");
		      }
		      else if (character == '\'') {
		        result.append("&#039;");
		      }
		      else if (character == '&') {
		         result.append("&amp;");
		      }
		      else {
		        //the char is not a special one
		        //add it to the result as is
		        result.append(character);
		      }
		      character = iterator.next();
		    }
		    return result.toString();
		  }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}//end of class
