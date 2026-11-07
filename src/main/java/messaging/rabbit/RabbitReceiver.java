package messaging.rabbit;

import com.rabbitmq.client.*;
import event.Message;
import trial.MyStringMessage;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by mkhanwalkar on 5/8/15.
 */
public class RabbitReceiver implements Receiver {

    Channel channel ;
    String queue ;
    private final BlockingQueue<byte[]> deliveries = new LinkedBlockingQueue<>();
    private boolean consuming;

    public RabbitReceiver(Channel channel, String queue)
    {
        this.channel = channel ;
        this.queue = queue;
    }

    /** Blocks until a message arrives. Returns null if the consumer cannot start or the thread is interrupted. */
    public Message recv() {
        try {
            startConsuming();
            return new MyStringMessage(new String(deliveries.take()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return null;
    }

    /** Subscribes on first use, so a receiver driven by setListener never competes with this consumer. */
    private synchronized void startConsuming() throws IOException {
        if (consuming) {
            return;
        }
        channel.basicConsume(queue, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) {
                deliveries.add(body);
            }
        });
        consuming = true;
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
