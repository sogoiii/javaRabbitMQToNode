package RPC;

import java.io.IOException;
import java.net.UnknownHostException;

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

import org.bson.types.ObjectId;

public class Processor {
	
	
	static DBCollection coll = null;
	
    public static void main(String [] args) throws UnknownHostException, MongoException {
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
                         System.out.println("recieved = " + message);      //what this function has received    
                         
                         String idString = message;
                         BasicDBObject keys = new BasicDBObject();
                         keys.put("Class",1);
                         keys.put("TestName",1);
//                         keys.put("info.x",1);
                         
                         try{
                         DBObject found = coll.findOne(new BasicDBObject("_id", new ObjectId(idString)) , keys );
                         System.out.println(found);
                         }
                         catch(IllegalArgumentException e) {	 
                        	 System.out.println("Failed to find objectID = '" + message+ "'");
                         }
                         
                         
                         
                         
                         
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
