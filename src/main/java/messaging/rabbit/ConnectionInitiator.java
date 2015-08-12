package messaging.rabbit;

import event.Message;
import messaging.TransportEndPoint;

import java.util.Map;

/**
 * Created by mkhanwalkar on 5/8/15.
 */
public class ConnectionInitiator implements TransportEndPoint {

    Delegate delegate;
    Map<String,Listener> listeners=null;

    public Map<String, Listener> getListeners() {
        return listeners;
    }

    public void setListeners(Map<String, Listener> listeners) {
        this.listeners = listeners;
    }

    public void init()
    {
        delegate.init();

        //TODO - take a listener property later , as well as queue name as a tuple . extend to taking a list of queuname/listeners
        // TODO - another option is no application listener is supplied - but an EF is supplied . in which case send the message to the EF queue .


        if (listeners!=null)
        {
            for (Map.Entry<String,Listener> listener : listeners.entrySet()) {
                Receiver recv = getReceiver(listener.getKey());
                recv.setListener(listener.getValue());
            }
        }




    }

    public Receiver getReceiver(String name)
    {
        return delegate.getReceiver(name);
    }

    public void destroy()
    {
        delegate.destroy();
    }

    public void send(Message message, String queue)
    {
        delegate.send(message,queue);
    }

    public void setDelegate(Delegate delegate)
    {
        this.delegate = delegate;
    }


}
