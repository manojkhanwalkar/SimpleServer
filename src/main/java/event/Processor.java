package event;

/**
 * Created by mkhanwalkar on 5/5/15.
 */
public interface Processor<T> {

    public void process(T message);
}
