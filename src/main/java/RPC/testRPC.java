package RPC;

import java.io.IOException;
import java.net.UnknownHostException;

import org.codehaus.jackson.map.ObjectMapper;

import Workers.CreatePDFWorker;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.gridfs.GridFS;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public final class testRPC {

    static int calledtimes = 0;
	
	static DBCollection coll = null; //name of test collection
	static ObjectMapper mapper = null; //parsing of json objects
	static GridFS gfsPhoto = null;  // gridfs interfance
	static GridFS gfsTestPDF = null; //gridfs object
	static Mongo m = null; //mongo connection
	static CreatePDFWorker ActivePDFCreator = null; //grading instance
	
	/**
	 * @param args
	 * @throws MongoException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, MongoException {
		// TODO Auto-generated method stub

		
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
		
		
		
		
		
     	System.out.println("connected to MongoDB");
        try {
            // Setup the queue, if it's not already declared this will
            // create it.
            // durable - true
            // exclusive - false
            // autoDelete - false
            // arguments - none
            channel.queueDeclare("test_RPC", true, false, false, null);

            // Add a callback for when messages arrive at the queue
            // autoAck - false
            channel.basicConsume("test_RPC", false,
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
                         
                
                         AMQP.BasicProperties b = (new AMQP.BasicProperties.Builder())
                             .correlationId(correlationId)
                             .build();

                         
                         String pdfID = "testPDFID_notvalidYet";
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
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	};//end of main

};//end of testRPC
