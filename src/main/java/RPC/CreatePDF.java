package RPC;

import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


import Workers.CreatePDFWorker;
import Workers.GradingWorker;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.AMQP;


import com.google.zxing.WriterException;
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
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.im4java.core.IM4JavaException;
import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDJpeg;
import org.apache.pdfbox.pdmodel.graphics.xobject.PDXObjectImage;



public class CreatePDF {
	
    static int calledtimes = 0;
	
	static DBCollection coll = null; //name of test collection
	static ObjectMapper mapper = null; //parsing of json objects
	static GridFS gfsPhoto = null;  // gridfs interfance
	static GridFS gfsTestPDF = null; //gridfs object
	static Mongo m = null; //mongo connection
	static CreatePDFWorker ActivePDFCreator = null; //grading instance
	
    public static void main(String [] args) throws MongoException, JsonProcessingException, IOException {
        System.out.println("GradePDF: initializing");
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
        m = new Mongo();

        // get handle to "mydb"
        DB db = m.getDB( "ecomm_database" );
    
        coll = db.getCollection("testschemas");
        // create a "TestPDF" namespace
     	gfsTestPDF = new GridFS(db, "fs");
        
        
       
     	ActivePDFCreator = new CreatePDFWorker(); //create grading instance 
        mapper = new ObjectMapper(); // can reuse, share globally
        System.out.println("connected to MongoDB");
        try {
            // Setup the queue, if it's not already declared this will
            // create it.
            // durable - true
            // exclusive - false
            // autoDelete - false
            // arguments - none
            channel.queueDeclare("createPDF", true, false, false, null);

            // Add a callback for when messages arrive at the queue
            // autoAck - false
            channel.basicConsume("createPDF", false,
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
                         
                         /*
                         	Working code is below NOTE: may want to do this inside a try catch and then finally so i send something back if anything happens here
                         */
                         long lStartTime = new Date().getTime(); //start time
                         
                         Map<String, Object> ReceivedMessage = mapper.readValue(message.getBytes("UTF-8"),new TypeReference<Map<String, Object>>() {});
//                       System.out.println(userInMap.get("testid"));

                         String idString = (String) ReceivedMessage.get("testid");
                         String pdfID = null;
                         try {
                        	 pdfID = ActivePDFCreator.Create(idString, m);
						} catch (COSVisitorException e) { //on any catch return to user, add text so that they get an error? so that they can tell us again later?
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (WriterException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} //create pdf with the message = testid
                         
                         
                        
                         long lEndTime = new Date().getTime(); //end time
                         long difference = lEndTime - lStartTime; //check different
                         System.out.println("Elapsed milliseconds: " + difference);
                         System.out.println("Done with computational code!");
                         /*
                      		Working code Done now return to User
                        */                        
                                              
                         AMQP.BasicProperties b = (new AMQP.BasicProperties.Builder())
                             .correlationId(correlationId)
                             .build();

//                         waiting(3000); //test wait for debugging purposes
                         
                         
                         calledtimes++;
                         //String senttonode = new String("{\"cool\":\""+ calledtimes+ "\"}" );
                         String senttonode = new String(pdfID);
                         channel.basicPublish("", responseQueue, b, pdfID.getBytes("UTF-8"));
                         channel.basicAck(deliveryTag, false);
                         System.out.println("still inside the rpc java code. ");
                     }
                 });
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Something went horribly wrong.");
            return;
        }
    }//end of RPC FUNCTION
    
    
    

	public static void waiting(int n){
		long t0, t1;
		t0 = System.currentTimeMillis();
		do{
			t1 = System.currentTimeMillis();
		}
		while(t1 -t0 < n);
	}//end of waiting
    
    

	
	
	



    
    
    
}//end of class gradePDF
