package TestingStuff;

import java.net.UnknownHostException;

import org.codehaus.jackson.map.ObjectMapper;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class TestQueries {

	/**
	 * @param args
	 * @throws MongoException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, MongoException {
		// TODO Auto-generated method stub
		
		 //required debugging code
		Mongo m = new Mongo();
		DB db = m.getDB("ecomm_database");
		DBCollection coll = db.getCollection("testschemas");
		ObjectMapper mapper = new ObjectMapper(); 
		String message = "4fda1af52f910cc6200000d3"; //test id, that i will have in the real version
		

	}

}
