

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
 
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class JamesConsumerNormal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//callConsumer();
//		System.out.println(DateFormater("2018-09-01 11:00:00","dd-MMM-yyyy HH:mm:ss"));
		String currDateFormat = determineDateFormat("2018/09/12");
		System.out.println(currDateFormat);
		System.out.println(DateFormater(currDateFormat,"2018/09/12","YYYY-MM-DD"));
//		System.out.println(determineDateFormat("2018-09-01 11:00:00"));
	}
	
	public static String DateFormater(String currentDateFormat,String valueStr, String newDateformat ) {
		String formattedDate="";
		try {
		Date dateNewField=new SimpleDateFormat(currentDateFormat).parse(valueStr);
		formattedDate = new SimpleDateFormat(newDateformat).format(dateNewField); //get format from sling and map here
		}catch(Exception e) {
		e.printStackTrace();}

		return formattedDate;
		}
	
	public static String determineDateFormat(String dateString) {
	    for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
	        if (dateString.toLowerCase().matches(regexp)) {
	            return DATE_FORMAT_REGEXPS.get(regexp);
	        }
	    }
	    return null; // Unknown format.
	}
	
	private static final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {{
	    put("^\\d{8}$", "yyyyMMdd");
	    put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
	    put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
	    put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
	    put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
	    put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
	    put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
	    put("^\\d{12}$", "yyyyMMddHHmm");
	    put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
	    put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
	    put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
	    put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
	    put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
	    put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
	    put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
	    put("^\\d{14}$", "yyyyMMddHHmmss");
	    put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
	    put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
	    put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
	    put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
	    put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
	    put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
	    put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");
	}};
	
	//URL of the JMS server. DEFAULT_BROKER_URL will just mean that JMS server is on localhost
    private static String url = "tcp://bluealgo.com:61616";
     
    // default broker URL is : tcp://localhost:61616"
    private static String subject = "JAMES_QUEUE"; // Queue Na
	
    private static String activeMQPassword = "admin";
    
    private static String activeMQUsername = "admin";
    
    public static void callConsumer(){
		try{
			
			 ConnectionFactory connectionFactory  = new ActiveMQConnectionFactory(activeMQUsername,activeMQPassword,url);
			 Connection connection = connectionFactory.createConnection();
			 connection.start();
			
			 Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
			//queueName = queueName.toUpperCase();
			Destination destination = session.createQueue(subject);
			MessageConsumer consumer = session.createConsumer(destination);
			//MessageProducer producer = session.createProducer(null);  // no default queue
			

			// Listen for arriving messages 
			MessageListener listener = new MessageListener() { 

			public void onMessage(Message message) {
				try {

				if (message instanceof TextMessage) {
					TextMessage textMessage = (TextMessage) message;
									
						System.out.println(textMessage.getText());
					}
			    }
				 catch (Exception e) {
						e.printStackTrace();
						System.out.print("error message :: "+e.getMessage());
					}
			} 
			};
			consumer.setMessageListener(listener);
		
			}catch(Exception e){
				System.out.print("error :: "+e.getMessage());
			}
	}
	


}
