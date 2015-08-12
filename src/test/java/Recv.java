import event.Message;
import messaging.rabbit.ConnectionInitiator;
import messaging.rabbit.Listener;
import messaging.rabbit.RabbitDelegate;
import messaging.rabbit.Receiver;

public class Recv {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv)
            throws java.io.IOException,
            InterruptedException {


        ConnectionInitiator initiator = new ConnectionInitiator();
        initiator.setDelegate(new RabbitDelegate());

        initiator.init();

        Receiver recv = initiator.getReceiver(QUEUE_NAME);

        recv.setListener(new Listener() {
            @Override
            public void onMessage(Message message) {
                System.out.println(message);
            }
        });


//        Thread.sleep(10000);

   /*     while(true)
        {
            Message message = recv.recv();

            System.out.println(message);
        }*/

 /*       QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(QUEUE_NAME, true, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String message = new String(delivery.getBody());
            System.out.println(" [x] Received '" + message + "'");
        }*/


    }

}
