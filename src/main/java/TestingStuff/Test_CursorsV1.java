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

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
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
//        System.out.println("Test Object = " + TestObject);
        JsonNode rootNode = mapper.readValue(TestObject.toString().getBytes("UTF-8"), JsonNode.class);
        JsonNode Answers = rootNode.get("Answers");

//        System.out.println("size of answers = " + Answers.size());
//      for(int x = 0; x < Answers.size(); x++){
//    	
//    	int IDS = Answers.get(x).get("IDS").getIntValue(); //grab the question 
//    	String QID = new String(Answers.get(x).get("IDS").getTextValue()); //grab the question 
//    	System.out.println("IDS = " + QID );
//    	
//      }//end of grade results
//	
        
        
        //v1
//        BasicDBObject posop = new BasicDBObject();
////        posop.put("$inc", new BasicDBObject("Answers.0.found", 1));
//        posop.put("$set", new BasicDBObject("Answers.0.correct", 1));
//        posop.append("$set", new BasicDBObject("Answers.0.selected", 3));
////        posop.put("$set", new BasicDBObject("Answers.0.selected", 3));
        
        
        //v2
        BasicDBObject newvals = new BasicDBObject();
        newvals.put("Answers.0.correct", 4);
        newvals.put("Answers.0.selected", 4);
        BasicDBObject posop = new BasicDBObject("$set", newvals);
        
        
        
        System.out.println("inc query = " + posop.toString());
        coll.update(new BasicDBObject("_id", new ObjectId(message)), posop);
      
         
       BasicDBObject fields = new BasicDBObject();
//       fields.put("Answers",1); //tell mongo to grab key, this is still a setup
//       fields.put("IDS",  new BasicDBObject("$elemMatch", "0_1"));
       fields.put("Answers",  new BasicDBObject("$elemMatch", new BasicDBObject("IDS", "0_1")));
       System.out.println("query = " + fields.toString());
//       fields.put("$elemMatch", val)
       DBObject Arrayelem = coll.findOne(new BasicDBObject("_id", new ObjectId(message)));
       System.out.println("answers = " + Arrayelem);
//       DBCursor curs = coll.find(new BasicDBObject("_id", new ObjectId(message)));
//       while(curs.hasNext()) {
//    	   DBObject o = curs.next();
//
//    	   // shows the whole result document
//    	   System.out.println("array = " + o.toString());
//    	   System.out.println("obid = " + o.get("ClassName"));
//    	   BasicDBList lights = (BasicDBList) o.get("Answers");
//    	   
//    	   
//
//    	   // shows the lights array -- this is actually a collection of DBObjects
//    	   System.out.println( "BasicDBlist = " + lights.toString());
//
//    	   // optional: break it into a native java array
//    	   BasicDBObject[] lightArr = lights.toArray(new BasicDBObject[0]);
//    	   for(BasicDBObject dbObj : lightArr) {
//    	     // shows each item from the lights array
//    		   
//    		   
//    		   
//    	     System.out.println(dbObj.get("IDS"));
//    	   }
//    	 }
       
       
       
       
       
        
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
