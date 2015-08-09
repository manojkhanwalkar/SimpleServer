package trial;

import com.lmax.disruptor.EventFactory;
import event.Message;

/**
 * Created by mkhanwalkar on 5/5/15.
 */
public class MyStringMessage implements Message, EventFactory<MyStringMessage> {

    String s=null;

    public MyStringMessage()
    {

    }

    public MyStringMessage(String s)
    {
        this.s = s;
    }

    public void setString(String s)
    {
        this.s = s;
    }

    @Override
    public String getString() {
        return s;
    }

    public String toString()
    {
        return s;
    }

    @Override
    public MyStringMessage newInstance() {
        return new MyStringMessage();
    }

}