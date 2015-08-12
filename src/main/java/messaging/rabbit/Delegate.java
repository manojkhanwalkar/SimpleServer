package messaging.rabbit;

import event.Message;

/**
 * Created by mkhanwalkar on 5/8/15.
 */
public interface Delegate {

    public void init();

    public void destroy();

    public void send(Message message, String queue);

    public Receiver getReceiver(String name);

}
