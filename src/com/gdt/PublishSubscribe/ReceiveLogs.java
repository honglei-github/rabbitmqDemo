package com.gdt.PublishSubscribe;

import com.rabbitmq.client.*;
import java.io.IOException;

/**
 * 项目名称：rabbitmqDemo 类名称：ReceiveLogs 类描述： 创建人：shl 创建时间：2017年8月25日 上午10:01:00
 * 修改人：shl 修改时间：2017年8月25日 上午10:01:00 修改备注：
 * 
 * @version
 * 
 */
public class ReceiveLogs {
	private static final String EXCHANGE_NAME = "logs";

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		String queueName = channel.queueDeclare().getQueue();// 创建随机队列名称
		channel.queueBind(queueName, EXCHANGE_NAME, "");// 进行绑定
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
			}
		};
		channel.basicConsume(queueName, true, consumer);
	}
}