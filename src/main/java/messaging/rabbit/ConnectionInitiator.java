package messaging.rabbit;

import event.EventFramework;
import event.Message;
import messaging.TransportEndPoint;

import java.util.List;
import java.util.Map;

/**
 * Created by mkhanwalkar on 5/8/15.
 */
public class ConnectionInitiator implements TransportEndPoint {

    Delegate delegate;
    Map<String,Listener> listeners=null;

    List<String> queueNames;

    public List<String> getQueueNames() {
        return queueNames;
    }

    public void setQueueNames(List<String> queueNames) {
        this.queueNames = queueNames;
    }

    EventFramework eventFramework;

    public EventFramework getEventFramework() {
        return eventFramework;
    }

    public void setEventFramework(EventFramework eventFramework) {
        this.eventFramework = eventFramework;
    }

    public Map<String, Listener> getListeners() {
        return listeners;
    }

    public void setListeners(Map<String, Listener> listeners) {
        this.listeners = listeners;
    }

    public void init()
    {
        delegate.init();

        // TODO - another option is no application listener is supplied - but an EF is supplied . in which case send the message to the EF queue .


        if (listeners!=null)
        {
            for (Map.Entry<String,Listener> listener : listeners.entrySet()) {
                Receiver recv = getReceiver(listener.getKey());
                recv.setListener(listener.getValue());
            }
        }
        else if (queueNames!=null)
        {
            for (String name : queueNames) {
                Receiver recv = getReceiver(name);

                recv.setListener(new Listener() {
                    @Override
                    public void onMessage(Message message) {

                        eventFramework.sendMessage(message);
                    }
                });
            }


        }
        else
        {
            // no receivers , onle senders will work .
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
