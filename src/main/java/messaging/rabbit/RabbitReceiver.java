package messaging.rabbit;

import com.rabbitmq.client.*;
import event.Message;
import trial.MyStringMessage;

import java.io.IOException;

/**
 * Created by mkhanwalkar on 5/8/15.
 */
public class RabbitReceiver implements Receiver {

    Channel channel ;
    String queue ;
    QueueingConsumer consumer;

    public RabbitReceiver(Channel channel, String queue)
    {
        this.channel = channel ;
        this.queue = queue;
     /*   try {
          //  consumer = new QueueingConsumer(channel);
            //channel.basicConsume(queue, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }



     public Message recv() {

         //while (true) {
         QueueingConsumer.Delivery delivery = null;
         try {
             delivery = consumer.nextDelivery();
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
         String message = new String(delivery.getBody());
         return new MyStringMessage(message);

     }

     public void setListener(Listener listner)  {
         try {
             channel.basicConsume(queue, false,
                     new DefaultConsumer(channel) {
                         @Override
                         public void handleDelivery(final String consumerTag,
                                                    final Envelope envelope,
                                                    final AMQP.BasicProperties properties,
                                                    final byte[] body) throws IOException {

                             final String readMessage = new String(body);
                             listner.onMessage(new MyStringMessage(readMessage));

                                 final long deliveryTag = envelope.getDeliveryTag();
                                 channel.basicAck(deliveryTag, false);

                         }

                     });
         } catch (IOException e) {
             e.printStackTrace();
         }
     }



}
