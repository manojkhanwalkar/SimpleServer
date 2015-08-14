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

    public void put(String message)
    {

         queue.put(message.getBytes());
    }

}
