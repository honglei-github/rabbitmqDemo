package com.gdt.WorkQueues.MessageConfirm;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

/**   
* 项目名称：rabbitmqDemo   
* 类名称：Worker   
* 类描述：   
* 创建人：shl   
* 创建时间：2017年8月24日 下午2:06:46   
* 修改人：shl   
* 修改时间：2017年8月24日 下午2:06:46   
* 修改备注：   
* @version    
*/
/**
 * 项目名称：rabbitmqDemo 类名称：Worker4 类描述： 创建人：shl 创建时间：2017年8月24日 下午3:09:09 修改人：shl
 * 修改时间：2017年8月24日 下午3:09:09 修改备注：
 * 
 * @version 消息确认，如果其中一个消费者没有消息确认，偶尔发生死亡的情况，则rabbitmq没有接受到
 *          应答，将把刚才发出的消息发个别的工人，防止消息丢失。
 */
public class Worker4 {

	private final static String TASK_QUEUE_NAME = "hello";

	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException,
			ConsumerCancelledException, InterruptedException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
		System.out.println("Worker4 Waiting for messages...");

		final Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] worker4 Received '" + message + "'");
				System.err.println("尝试断开--设置消息确认后将消息传给其他工人");
				try {
					doWork(message);

				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					System.out.println(" [x] worker4 Done");
					channel.basicAck(envelope.getDeliveryTag(), false); // 消息确认
																		// 如果未收到消息切断开链接
																		// 将由其他工人使用
				}
			}
		};
		boolean autoAck = false;
		channel.basicConsume(TASK_QUEUE_NAME, autoAck, consumer);
	}

	private static void doWork(String task) throws InterruptedException {
		for (char ch : task.toCharArray()) {
			if (ch == '.')
				Thread.sleep(5000);
		}
	}
}
