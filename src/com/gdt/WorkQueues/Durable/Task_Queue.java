package com.gdt.WorkQueues.Durable;

import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * 项目名称：rabbitmqDemo 类名称：NewTask 类描述： 创建人：shl 创建时间：2017年8月24日 下午1:44:50 修改人：shl
 * 修改时间：2017年8月24日 下午1:44:50 修改备注：
 * 
 * @version
 * 
 */
public class Task_Queue {
	private final static String TASK_QUEUE_NAME = "task_queue";

	public static void main(String[] args) throws java.io.IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("127.0.0.1");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		Boolean durable = true;
		channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);

		String[] argv = { "Hello", "World", "shl" };
		String message = getMessage(argv);
		for (int i = 0; i < 30; i++) {

			channel.basicPublish("", "task_queue", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());// 耐久性设置
			System.out.println("Sent:" + message);

		}
		channel.close();
		connection.close();
	}

	private static String getMessage(String[] strings) {
		if (strings.length < 1)
			return "Hello World!";
		return joinStrings(strings, ".");
	}

	private static String joinStrings(String[] strings, String delimiter) {
		int length = strings.length;
		if (length == 0)
			return "";
		StringBuilder words = new StringBuilder(strings[0]);
		for (int i = 1; i < length; i++) {
			words.append(delimiter).append(strings[i]);
		}
		return words.toString();
	}
}
