package TestingStuff;

import java.io.File;
import java.util.TreeMap;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.MappingJsonFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;

public class JacksonParsing {
	
	public static void main(String[] args) throws Exception {
		//v1
//		JsonFactory Jf = new MappingJsonFactory();
//	    JsonParser jp = Jf.createJsonParser(new File("/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/src/main/java/RPC/object2.json"));
//	    JsonNode nameNode = jp.readValueAsTree();
//		System.out.println("field2: " + nameNode.get("PDFTest").get("_id").get("$oid").getTextValue());
		
		
		
		
	 ObjectMapper m = new ObjectMapper();
//	 // can either use mapper.readTree(JsonParser), or bind to JsonNode
	 JsonNode rootNode = m.readValue(new File("/Users/angellopozo/Dropbox/My Code/java/MainRabbitMongo/src/main/java/RPC/object2.json"), JsonNode.class);
	 JsonNode testvar = rootNode.get("Testvar");
	 JsonNode array = rootNode.get("array");
	 JsonNode PDF = rootNode.get("PDFTest");
//	 System.out.println("Result = " + rootNode.get("field1").getTextValue());
	 System.out.println("result 1  = " + array.get(1).get("name"));
	 System.out.println("result 2  = " + testvar.get(0).get("field1"));
	 System.out.println("result 3  = " + PDF.get(0).get("_id").get("$oid").getTextValue());
	 
//	 String lastName = nameNode.path("last").getTextValue();
//	 if ("xmler".equalsIgnoreCase(lastName)) {
//	   ((ObjectNode)nameNode).put("last", "Jsoner");
//	 }
	 
	    
	    
	}//end of main
	

}//end of jackson parsing
