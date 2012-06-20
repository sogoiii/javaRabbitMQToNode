package TestingStuff;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.Iterator;

import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.im4java.core.IM4JavaException;
import org.jpedal.exception.PdfException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class Test_CursorsV1 {
	
	
	public static void main(String[] args) throws MongoException, JsonParseException, JsonMappingException, UnsupportedEncodingException, IOException{ //is - is the inputstream of the pdf file
		System.out.println("inside grader");
		
		 //required debugging code
		Mongo m = new Mongo();
		DB db = m.getDB("ecomm_database");
		DBCollection coll = db.getCollection("testschemas");
		ObjectMapper mapper = new ObjectMapper(); 
	
		
		String message = "4fd8d74675689700000000f3"; //test id, that i will have in the real version
		DBObject TestObject = coll.findOne(new BasicDBObject("_id", new ObjectId(message))); //the actual mongo query
        System.out.println("Test Object = " + TestObject);
        JsonNode rootNode = mapper.readValue(TestObject.toString().getBytes("UTF-8"), JsonNode.class);
        JsonNode Answers = rootNode.get("Answers");

        System.out.println("size of answers = " + Answers.size());
      for(int x = 0; x < Answers.size(); x++){
    	
    	int IDS = Answers.get(x).get("IDS").getIntValue(); //grab the question 
    	String QID = new String(Answers.get(x).get("IDS").getTextValue()); //grab the question 
    	System.out.println("IDS = " + QID );
    	
    	
    	
    	
    	
    	
//        BasicDBObject correct = new BasicDBObject("correct", 3);
//        correct.put("found", 0);
//        correct.put("selected", 1);
//        BasicDBObject QI_SN = new BasicDBObject("1", correct); //the qrCodeText = 0_1 for question 0 and student 1
//        Results.add(QI_SN);
    }//end of grade results
	
        
        
        
        
        
        
        
        //if answers is a bunch of obects aka v1 
//      JsonNode first = Answers.get("0_1");
//      System.out.println("answer = " + first.get("found").getIntValue());
//        Iterator ob = Answers.iterator();
//        
//        int i = 0;
//        while(ob.hasNext() == true){
//        	System.out.println(ob.next());
//        
//        }


        
        
        
        
        
        
	}//end of main

}//end of class
