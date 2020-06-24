package com.biz.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Servlet implementation class JamesMailetConsumer
 */
public class JamesMailetConsumer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JamesMailetConsumer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		callConsumer();
	}
	
//    public void destroy(){
//    	System.out.println("I'M Melting..........................");
//    try {
//    	connection.close();
//    } catch (Exception e) {
//    	
//    	System.out.println(e.getMessage());
//    	e.printStackTrace();
//
//    }
//}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
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
									
//						System.out.println(textMessage.getText());
						JSONObject incomingJson = new JSONObject(textMessage.getText());
						String jsonString = "{\"MailTempName\":\"DirectSMTPTracking_mail\",\"templateName\":\"DirectSMTPTracking\",\"typeDataSource\":\"Enter Manually\",\"AttachtempalteType\":\"TemplateLibrary\",\"esignature\":\"false\",\"twofactor\":\"false\",\"esigntype\":\"\",\"group\":\"NoGroup\",\"lgtype\":\"null\",\"multipeDropDown\":[\"1\",\"3\"],\"Type\":\"Generation\"}";
						JSONObject mainJson = new JSONObject(jsonString);
						mainJson.put("data", new JSONArray().put(incomingJson));
//						mainJson.put("Email", incomingJson.getString("from"));
						mainJson.put("Email", "userkonark@gmail.com");
						System.out.println(mainJson);
						callPostService("https://bluealgo.com:8083/portal/servlet/service/dDependencyMailet", mainJson.toString());
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

    public static String callPostService(String urlStr, String fullJson) {
		StringBuilder sb = null;
		try {
			URL object = new URL(urlStr);
			if (urlStr.indexOf("https") != -1) {
				TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}

					public void checkClientTrusted(X509Certificate[] certs, String authType) {
					}

					public void checkServerTrusted(X509Certificate[] certs, String authType) {
					}

				} };

				try {
					SSLContext sc = SSLContext.getInstance("SSL");
					sc.init(null, trustAllCerts, new java.security.SecureRandom());
					HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				// Create all-trusting host name verifier
				HostnameVerifier allHostsValid = new HostnameVerifier() {
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
				};
				// Install the all-trusting host verifier
				HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
				/*
				 * end of the fix
				 */
			}

			HttpURLConnection con = (HttpURLConnection) object.openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setRequestMethod("POST");
			// System.out.println("fullJson :: "+fullJson);
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(fullJson);
			wr.flush();

			// display what returns the POST request

			sb = new StringBuilder();
			int HttpResult = con.getResponseCode();
			if (HttpResult == HttpURLConnection.HTTP_OK) {
				BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				// System.out.println("" + sb.toString());
			} else {
				System.out.println(con.getResponseMessage());
			}
		} catch (Exception e) {
			System.out.println("error :: " + e.getMessage());
			return e.getMessage();
		}
		return sb.toString();
	}


}
