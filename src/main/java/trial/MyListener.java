package trial;

import event.Message;
import messaging.rabbit.Listener;

/**
 * Created by mkhanwalkar on 8/12/15.
 */
public class MyListener implements Listener {

    public void onMessage(Message message)
    {
        System.out.println("In my listener " + message.toString()  + "  "  + Thread.currentThread());
    }



}
