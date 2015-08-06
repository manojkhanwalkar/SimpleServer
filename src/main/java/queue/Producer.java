package queue;

/**
 * Created by mkhanwalkar on 4/22/15.
 */
public class Producer {

    PersistentCircularQueue queue;
    public Producer(PersistentCircularQueue queue)
    {
        this.queue = queue;
    }

    public boolean put(String message)
    {
       return  queue.put(message.getBytes());
    }

}
