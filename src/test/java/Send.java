import messaging.rabbit.ConnectionInitiator;
import messaging.rabbit.RabbitDelegate;

/**
 * Created by mkhanwalkar on 5/8/15.
 */
public class Send {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv)
            throws java.io.IOException {


      //  channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World!";

        ConnectionInitiator initiator = new ConnectionInitiator();
        initiator.setDelegate(new RabbitDelegate());

        initiator.init();

        initiator.send(new MyStringMessage(message),QUEUE_NAME);

        initiator.destroy();


    }

}
