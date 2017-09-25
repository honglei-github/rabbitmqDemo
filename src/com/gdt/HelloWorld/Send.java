package com.gdt.HelloWorld;

import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

/**
 * 项目名称：rabbitmqTest 类名称：Send 类描述： 创建人：shl 创建时间：2017年8月23日 上午11:34:22 修改人：shl
 * 修改时间：2017年8月23日 上午11:34:22 修改备注：
 * 
 * @version
 * 
 */
public class Send {

	private final static String QUEUE_NAME = "hello";

	public static void main(String[] args) throws java.io.IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1");
		factory.setPort(5672);
		factory.setUsername("admin");
		factory.setPassword("admin");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		String message = "Hello Wrold!";
		channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
		System.out.println("Sent:" + message);
		channel.close();
		connection.close();
	}

}
