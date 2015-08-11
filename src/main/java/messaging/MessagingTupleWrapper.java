package messaging;

import event.Message;

/**
 * Created by mkhanwalkar on 8/10/15.
 */
public class MessagingTupleWrapper implements Message {

    MessagingTuple tuple ;

    public MessagingTupleWrapper(MessagingTuple tuple)
    {
        this.tuple = tuple;
    }

    @Override
    public void setObject(Object obj) {
        this.tuple = (MessagingTuple)obj;
    }

    @Override
    public Object getObject() {
        return tuple;
    }
}
