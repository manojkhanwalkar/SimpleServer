package messaging.rabbit;

import event.Message;

/**
 * Created by mkhanwalkar on 5/8/15.
 */
public class ConnectionInitiator {

    Delegate delegate;

    public void init()
    {
        delegate.init();
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
