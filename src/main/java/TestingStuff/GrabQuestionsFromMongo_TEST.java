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

public class GrabQuestionsFromMongo_TEST {
	
	
	
	public static void main(String[] args) throws MongoException, JsonParseException, JsonMappingException, UnsupportedEncodingException, IOException {
		
		Mongo m = new Mongo();
		DB db = m.getDB("ecomm_database");
		DBCollection coll = db.getCollection("testschemas");
		
		
		
		ObjectMapper mapper = new ObjectMapper(); 
		
		
		String message = "4fcabbe1d41f228a4c000088"; //test id, that i will have in the real version
		BasicDBObject keys = new BasicDBObject(); //create search object, keys will be what is returned
        keys.put("Questions",1); //tell mongo to grab key, this is still a setup
        DBObject QuestionObjects = coll.findOne(new BasicDBObject("_id", new ObjectId(message)) , keys ); //the actual mongo query
        System.out.println("Questions and answers = " + QuestionObjects);
        
        JsonNode rootNode = mapper.readValue(QuestionObjects.toString().getBytes("UTF-8"), JsonNode.class);
        JsonNode Questions = rootNode.get("Questions");
//        String Q0 = new String(Questions.get(0).get("_id").get("$oid").getTextValue());
        String Q0_question = new String(Questions.get(0).get("Questionhtml").getTextValue());
        System.out.println("Question Html  = " + Q0_question);
        
       String out = Q0_question.replaceAll("&lt;", "<");
//        String out1 = out.replace("&gt;", ">");
        System.out.println("Question Str   = " + Q0_question);
        
        String unesc = StringEscapeUtils.unescapeHtml(Q0_question);
     
        
//        StringUtils.replaceEach(Q0_question, new String[]{"&amp;", "&quot;", "&lt;", "&gt;"}, new String[]{"&", "\"", "<", ">"});
        System.out.println("Question Str   = " + unesc);
        
        
        
//        URLDecoder urldecode= new URLDecoder();
//        System.out.println(urldecode.decode(Q0_question,"/&lt;/ig"));
        
//        String out1 = forXML(Q0_question);
//        String out2 = forXML(out1);
//        System.out.println("Question Str   = " + out1);
//        System.out.println("Question Str   = " + out2);
		
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
