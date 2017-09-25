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
* 类名称：Worker5   
* 类描述：   
* 创建人：shl   
* 创建时间：2017年8月24日 下午3:09:15   
* 修改人：shl   
* 修改时间：2017年8月24日 下午3:09:15   
* 修改备注：   
* @version    
*    
*/
public class Worker5 {

	private final static String TASK_QUEUE_NAME = "hello";

	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException, ConsumerCancelledException, InterruptedException {
		
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();
	    channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);
	    System.out.println("Worker5 Waiting for messages...");
		
	    final Consumer consumer = new DefaultConsumer(channel) {
	    	  @Override
	    	  public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
	    	    String message = new String(body, "UTF-8");

	    	    System.out.println(" [x] worker5 Received '" + message + "'");
	    	    System.err.println("尝试断开--设置消息确认后将消息传给其他工人");
	    	      try {
					doWork(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
	    	      System.out.println(" [x] worker5 Done");
	    	      channel.basicAck(envelope.getDeliveryTag(), false);
	    	    }
	    	  }
	    	};
	    	boolean autoAck = false; // acknowledgment is covered below
	    	channel.basicConsume(TASK_QUEUE_NAME, autoAck, consumer);
      }
	private static void doWork(String task) throws InterruptedException {
	    for (char ch: task.toCharArray()) {
	        if (ch == '.') Thread.sleep(5000);
	    }
	}  
}
