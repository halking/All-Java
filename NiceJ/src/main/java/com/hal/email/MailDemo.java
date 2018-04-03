package com.hal.email;

import java.util.Date;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailDemo {
	public final static String MAIL = "@sina.com"; // 邮箱格式
	public final static String SEND_HOST = "smtp.exmail.qq.com"; // 邮箱发送服务器
	public final static String ACCEPT_HOST = "imap.exmail.qq.com"; // 邮箱服务器
	public final static String SEND_USER = "steven.huang@d1m.cn"; // 用户名
	public final static String SEND_PWD = "Hal3316..-+"; // 密码
	public final static String FROM_MAIL = SEND_USER ;// 发送方邮箱地址
	public final static String TO_MAIL = "steven.huang@richemont.com"; // 接收方邮箱地址
	public final static String TO_SINA_MAIL = "hal381763563@sina.com"; // 接收方邮箱地址
	public final static String CC_MAIL = SEND_USER ; // 抄送方邮箱地址
	public final static String BCC_MAIl = SEND_USER ; // 密送方邮箱地址

	public final static String ENCODE = "UTF-8";
	public static Date date = new Date();

	// 使用JavaMail发送文本文件
	public static void send() throws Exception {

		Properties prop = new Properties();
		prop.setProperty("mail.host", SEND_HOST);
		prop.setProperty("mail.transport.protocol", "smtp");
		prop.setProperty("mail.smtps.ssl.enable", "true");
		// prop.setProperty("mail.smtp.port", "25");
		prop.setProperty("mail.smtp.auth", "true");

		Session session = Session.getInstance(prop);

		Message message = new MimeMessage(session);
		message.setSubject("测试");
		message.setText("你好！");
		message.setSentDate(new Date());
		message.setFrom(new InternetAddress(FROM_MAIL));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(
				TO_MAIL));
		message.saveChanges();

		Transport transport = session.getTransport();
		transport.connect(SEND_HOST, FROM_MAIL, SEND_PWD);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
		System.out.println("Successfully  send mail to all user");
	}

	public static void receive() throws Exception {

		Properties properties = new Properties();
		Session session = Session.getInstance(properties);

		Store store = session.getStore("pop3");
		store.connect(ACCEPT_HOST, SEND_USER, SEND_PWD);
		Folder folder = store.getFolder("inbox");
		folder.open(Folder.READ_ONLY);

		Message[] msgs = folder.getMessages();
		for (int i = 0; i < msgs.length; i++) {
			System.out.println("邮件发送日期： " + msgs[i].getSentDate());
			System.out.println("邮件主题： " + msgs[i].getSubject());
			System.out.println("邮件内容： " + msgs[i].getContent());
		}
		folder.close(false);
		store.close();
	}

	// 发送文本格式邮件
	public static void sendTxtMail() throws MailException {
		JavaMailSenderImpl send = new JavaMailSenderImpl();
		Properties prop = new Properties();
		prop.setProperty("mail.transport.protocol", "smtp");
		prop.setProperty("mail.host", SEND_HOST);
		// prop.setProperty("mail.smtp.port", "25");
		prop.setProperty("mail.smtps.ssl.enable", "true");
		prop.setProperty("mail.smtp.auth", "true");
		// prop.setProperty("mail.smtp.user", SEND_USER);
		// prop.setProperty("mail.smtp.password", SEND_PWD);

		send.setUsername(SEND_USER);
		send.setPassword(SEND_PWD);
		send.setJavaMailProperties(prop);

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(FROM_MAIL);
		msg.setTo(TO_MAIL);
		msg.setReplyTo(FROM_MAIL);
		msg.setCc(CC_MAIL);
		msg.setBcc(BCC_MAIl);
		msg.setSentDate(date);
		msg.setSubject("发送的文本格式邮件");
		msg.setText("文本格式 测试成功！");
		send.send(msg);
		System.out.println("Successfully  send mail to the user");

	}

	// 发送Html格式邮件
	public static void sendHtmlMail() throws Exception {
		JavaMailSenderImpl send = new JavaMailSenderImpl();
		Properties prop = new Properties();
		prop.setProperty("mail.transport.protocol", "smtp");
		prop.setProperty("mail.host", SEND_HOST);
		prop.setProperty("mail.smtps.ssl.enable", "true");
		prop.setProperty("mail.smtp.auth", "true");

		send.setUsername(SEND_USER);
		send.setPassword(SEND_PWD);
		send.setJavaMailProperties(prop);

		MimeMessage msg = send.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(msg, ENCODE);
		helper.setFrom(FROM_MAIL);
		helper.setTo(TO_MAIL);
		helper.setReplyTo(FROM_MAIL);
		helper.setCc(CC_MAIL);
		helper.setBcc(BCC_MAIl);
		helper.setSentDate(date);
		helper.setSubject("发送的HTML格式邮件");
		String html = "<font size='5' color='red'>HTML格式测试成功！</font>";
		helper.setText(html, true); // 邮件内容，参数true表示是html代码
		send.send(msg);
		System.out.println("Successfully  send mail to the user");
	}

	// 发送带内嵌文件的HTML格式邮件
	public static void sendInlineMail() throws Exception {
		// spring提供的邮件实现类
		JavaMailSenderImpl send = new JavaMailSenderImpl();
		Properties prop = new Properties();
		prop.setProperty("mail.transport.protocol", "smtp"); // 设置邮件发送协议
		prop.setProperty("mail.host", SEND_HOST); // 邮件服务器地址
		prop.setProperty("mail.smtps.ssl.enable", "true"); // 邮件ssl验证
		prop.setProperty("mail.smtp.auth", "true"); // 邮件服务身份验证

		send.setUsername(SEND_USER); // 设置用户名
		send.setPassword(SEND_PWD); // 设置密码
		send.setJavaMailProperties(prop);

		MimeMessage msg = send.createMimeMessage();
		// 指定HTML编码，参数true表示为multipart
		MimeMessageHelper helper = new MimeMessageHelper(msg, true, ENCODE);
		helper.setFrom(FROM_MAIL); // 发送者邮箱
		helper.setTo(TO_MAIL); // 接收者邮箱
		helper.setReplyTo(FROM_MAIL); // 回复邮箱
		helper.setCc(CC_MAIL); // 抄送邮箱
		helper.setBcc(BCC_MAIl); // 密送邮箱
		helper.setSentDate(date); // 发送日期
		helper.setSubject("发送的带有内部文件的HTML格式邮件");
		String html = "<font size='5' color='red'>HTML格式测试成功！</font>"
				+ "<img src ='cid:demoimg'>"; // cid是一个固定前缀，demoimg是一个资源名称
		helper.setText(html, true); // 邮件内容，参数true表示是html代码
		ClassPathResource resource = new ClassPathResource("col.jpg"); // 加载项目路径下资源
		helper.addInline("demoimg", resource); // 将资源绑定到demoimg上
		send.send(msg); // 发送邮件
		System.out.println("Successfully  send mail to the user");
	}

	// 发送带附件的邮件
	public static void sendAttachmentMail() throws Exception {
		// spring提供的邮件实现类
		JavaMailSenderImpl send = new JavaMailSenderImpl();
		Properties prop = new Properties();
		prop.setProperty("mail.transport.protocol", "smtp"); // 设置邮件发送协议
		prop.setProperty("mail.host", SEND_HOST); // 邮件服务器地址
		prop.setProperty("mail.smtps.ssl.enable", "false"); // 邮件ssl验证
		prop.setProperty("mail.smtp.auth", "false"); // 邮件服务身份验证

		send.setUsername(SEND_USER); // 设置用户名
		send.setPassword(SEND_PWD); // 设置密码
		send.setJavaMailProperties(prop);

		MimeMessage msg = send.createMimeMessage();
		// 指定HTML编码，参数true表示为multipart
		MimeMessageHelper helper = new MimeMessageHelper(msg, true, ENCODE);
		helper.setFrom(FROM_MAIL); // 发送者邮箱
		helper.setTo(TO_MAIL); // 接收者邮箱
		helper.setReplyTo(FROM_MAIL); // 回复邮箱
		helper.setCc(CC_MAIL); // 抄送邮箱
		helper.setBcc(BCC_MAIl); // 密送邮箱
		helper.setSentDate(date); // 发送日期
		helper.setSubject("发送带有附件的邮件");
		String html = "<font size='5' color='red'>附件测试成功！</font>";
		helper.setText(html, true); // 邮件内容，参数true表示是html代码
		String demo = "default_excel.xlsx";
		ClassPathResource resource = new ClassPathResource(demo); // 加载项目路径下资源
		helper.addAttachment(MimeUtility.encodeWord(demo), resource); // 如果文件是中文名，需要转码。
		send.send(msg); // 发送邮件
		System.out.println("Successfully  send mail to the user");
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// MailDemo.send();
		// MailDemo.receive();
//		 sendTxtMail();
		// sendHtmlMail();
		// sendInlineMail();
		sendAttachmentMail();
	}
}
