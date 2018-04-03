package com.hal.jms;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.mapper.Mapper.Null;

public class JmsChat implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger(JmsChat.class);
	public static final String TOPIC = "HUANG";
	public static final String USERNAME = "HUANG";
	public static final String FACTORY = "tcp://localhost:61616";
	private TopicConnection connection;
	private TopicSession pubSession;
	private TopicPublisher publisher;
	private String username;

	public JmsChat(String factory, String topicName, String username) {

		InitialContext ctx = null;
		try {
			ctx = new InitialContext();
			TopicConnectionFactory connectionFactory = (TopicConnectionFactory) ctx.lookup(factory);
			TopicConnection connection = connectionFactory.createTopicConnection();
			TopicSession pubsession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
			TopicSession subsession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

			Topic topic = (Topic) ctx.lookup(topicName);
			TopicPublisher publisher = pubSession.createPublisher(topic);
			TopicSubscriber subscriber = subsession.createSubscriber(topic);
			subscriber.setMessageListener(this);

			this.connection = connection;
			this.pubSession = pubsession;
			this.publisher = publisher;
			this.username = username;

			connection.start();
		} catch (NamingException e) {
			// TODO: handle exception
		} catch (JMSException me) {
			// TODO: handle exception
		}
	}

	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try {
			TextMessage ame = (TextMessage) message;
			System.out.println("msg text:" + ame.getText());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void sendMessage(String text) {
		try {
			TextMessage message = pubSession.createTextMessage();
			message.setText(username + ":" + text);
			publisher.publish(message);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() throws JMSException {
		connection.close();
	}

	public static void main(String[] args) throws Exception {
		/*
		 * if (args.length!=3) {
		 * System.out.println("input paramter is mismatch!"); }
		 */
		JmsChat chat = new JmsChat(FACTORY, TOPIC, USERNAME);
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				String s = reader.readLine();
				if (s.equalsIgnoreCase("exist")) {
					chat.close();
				} else {
					chat.sendMessage(s);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
}
