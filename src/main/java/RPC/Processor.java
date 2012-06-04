package RPC;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.text.html.HTML.Tag;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP;


import com.mongodb.Mongo;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DB;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;



public class Processor {
	
	
	static DBCollection coll = null; //name of test collection
	static ObjectMapper mapper = null; //parsing of json objects
	static GridFS gfsPhoto = null;  // gridfs interfance
	
    public static void main(String [] args) throws MongoException, JsonProcessingException, IOException {
        System.out.println("Processor: initializing");
        // Our main AMQP connection, we'll open
        // a channel per thread later with a threadpool.
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setHost("localhost");
        factory.setPort(5672);

        // Let's connect and setup our basic queues.
        System.out.println("connecting to AMQP server...");
        Connection conn = null;
        final Channel channel;

        try {
            conn = factory.newConnection();
            channel = conn.createChannel();
        } catch (IOException e) {
            System.out.println("failed to create channel or connect to server");
            e.printStackTrace();
            return;
        }

        System.out.println("connected to rabbit MQ.");
        
        
        
     // connect to the local database server
        Mongo m = new Mongo();

        // get handle to "mydb"
        DB db = m.getDB( "ecomm_database" );
    
        coll = db.getCollection("testschemas");
        // create a "TestPDF" namespace
     	GridFS gfsTestPDF = new GridFS(db, "TestPDF");
        
     // make a document and insert it
//        BasicDBObject doc = new BasicDBObject();
//
//        doc.put("name", "MongoDB");
//        doc.put("type", "database");
//        doc.put("count", 1);
//        
//        BasicDBObject info = new BasicDBObject();
//
//        info.put("x", 203);
//        info.put("y", 102);
//
//        doc.put("info", info);
//
//        coll.insert(doc);
//        DBObject myDoc = coll.findOne();
//        //System.out.println(myDoc);
        
        
        
       
        
        mapper = new ObjectMapper(); // can reuse, share globally
        System.out.println("connected to MongoDB");
        try {
            // Setup the queue, if it's not already declared this will
            // create it.
            // durable - true
            // exclusive - false
            // autoDelete - false
            // arguments - none
            channel.queueDeclare("image", true, false, false, null);

            // Add a callback for when messages arrive at the queue
            // autoAck - false
            channel.basicConsume("image", false,
                 new DefaultConsumer(channel) {
                     @Override
                     public void handleDelivery(String consumerTag,
                                                Envelope envelope,
                                                AMQP.BasicProperties properties,
                                                byte[] body)
                         throws IOException
                     {
                         String routingKey = envelope.getRoutingKey();
                         String contentType = properties.getContentType();
                         String correlationId = properties.getCorrelationId();
                         String responseQueue = properties.getReplyTo();
                         long deliveryTag = envelope.getDeliveryTag();

                         System.out.println("content type = " + contentType);
                         
                         String message = new String(body);
                         System.out.println("message received");
                         System.out.println("correlationId: " +
                             correlationId +
                             " responseQueue: " +
                             responseQueue);
//                         System.out.println("recieved = " + message);      //what this function has received    
                         
                         
                         
                         
                         //Converting the input into a java json object to pull information
                         Map<String, Object> userInMap = mapper.readValue(message.getBytes("UTF-8"),new TypeReference<Map<String, Object>>() {});
//                         System.out.println(userInMap.get("testid"));

                         String idString = (String) userInMap.get("testid");
                         
                         BasicDBObject keys = new BasicDBObject();
                         keys.put("ClassName",1);
                         keys.put("TestName",1); //to reach into variables you can do -> keys.put("info.x",1);
                         keys.put("PDFTest._id", 1);//grab id of pdf file
                         
                         //check databse to see if the test exists!
                         try{
	                         DBObject testObject = coll.findOne(new BasicDBObject("_id", new ObjectId(idString)) , keys );
	                         System.out.println("Will grade test = " + testObject);
	                         Map<String, Object> resultMap = mapper.readValue(testObject.toString().getBytes("UTF-8"),new TypeReference<Map<String, Object>>() {});
	                         
//	                          JsonNode jsonNode = mapper.readValue(testObject.toString().getBytes("UTF-8"), JsonNode.class);
//	                          JsonNode userCards = jsonNode.path("PDFTest");
//	                          Map<String, Object> pdftest  = mapper.readValue(userCards.toString().getBytes("UTF-8"), new TypeReference<Map<String, Object>>() {});
	                          //System.out.println("value = " + pdftest.get("id").toString());
	                         
	                         
//	                         List list = mapper.readValue(testObject.toString().getBytes("UTF-8"), List.class);
//	                         for(int i = 0; i < list.size(); i++){
//	                        	 System.out.println("value = " + list);
//	                         }
	                         
	                         
	                       
//	  
//	                         ArrayList<String> list = (ArrayList<String>) resultMap.get("PDFTest");
//	                         for(int i = 0; i < list.size(); i++){
//	                        	 System.out.println("value = " + list);
//	                         }
	                        // System.out.println("file object id = " + IDMap.get("_id"));
	                         
	                         
	                         
	                         JsonNode rootNode = mapper.readValue(testObject.toString().getBytes("UTF-8"), JsonNode.class);
	                         JsonNode PDF = rootNode.get("PDFTest");
	                         System.out.println("result 3  = " + PDF.get(0).get("_id").get("$oid").getTextValue());
	                         
	                         
	                         
	                         
	                         
	                         
	                         
	                         
	                         
	                         System.out.println("file object id = " + resultMap.get("PDFTest"));
                         }
                         catch(IllegalArgumentException e) {	 
                        	 System.out.println("Failed to find objectID = '" + message+ "'");
                         }
                         
                         
                         
                         
                         //Since test exists, grab the TestPDF from Gridfs
                        // GridFSDBFile imageForOutput = gfsTestPDF.findOne();
                         
                         
                         
                         AMQP.BasicProperties b = (new AMQP.BasicProperties.Builder())
                             .correlationId(correlationId)
                             .build();

                         waiting(3000);
                         calledtimes++;
                         //String senttonode = new String("{\"cool\":\""+ calledtimes+ "\"}" );
                         String senttonode = new String("fromjava");
                         channel.basicPublish("", responseQueue, b,senttonode.getBytes("UTF-8"));
                         channel.basicAck(deliveryTag, false);
                         System.out.println("still inside the rpc java code. ");
                     }
                 });
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Something went horribly wrong.");
            return;
        }
    }
    
    static int calledtimes = 0;
public static void waiting(int n){
	long t0, t1;
	t0 = System.currentTimeMillis();
	do{
		t1 = System.currentTimeMillis();
	}
	while(t1 -t0 < n);
}//end of waiting
    
    
    
    
    
}//end of class processor
