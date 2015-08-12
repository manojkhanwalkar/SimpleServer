package messaging.rabbit;

import event.Message;

/**
 * Created by mkhanwalkar on 5/8/15.
 */
public interface Listener {

    default public void onMessage(Message message)
    {
        System.out.println(message.toString()  + "  "  + Thread.currentThread());
    }



}
