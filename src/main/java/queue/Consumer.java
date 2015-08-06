package queue;

/**
 * Created by mkhanwalkar on 4/22/15.
 */
public class Consumer {

    PersistentCircularQueue queue;
    public Consumer(PersistentCircularQueue queue)
    {
        this.queue = queue;
    }

    public String read()
    {
        return queue.read();
    }
}
