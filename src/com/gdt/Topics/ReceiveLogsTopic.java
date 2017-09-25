package com.gdt.Topics;

import com.rabbitmq.client.*;
import java.io.IOException;
/**   
* 项目名称：rabbitmqDemo   
* 类名称：ReceiveLogsTopic   
* 类描述：   
* 创建人：shl   
* 创建时间：2017年8月25日 下午3:46:58   
* 修改人：shl   
* 修改时间：2017年8月25日 下午3:46:58   
* 修改备注：   
* @version    
*    
*/
public class ReceiveLogsTopic {

  private static final String EXCHANGE_NAME = "topic_logs";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
    String queueName = channel.queueDeclare().getQueue();
   // String[] args={"*.info"};
      String[] args={"*.infoo"};
   // String[] args={"anonymous.*"};
    if (args.length < 1) {
      System.err.println("Usage: ReceiveLogsTopic [binding_key]...");
      System.exit(1);
    }

    for (String bindingKey : args) {
      channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
    }

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope,
                                 AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
      }
    };
    channel.basicConsume(queueName, true, consumer);
  }
}

