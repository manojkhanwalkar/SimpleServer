package messaging.rabbit;

import event.Message;

/**
 * Created by mkhanwalkar on 5/8/15.
 */
public interface Receiver {

    public Message recv();

    public void setListener(Listener listener);

}
