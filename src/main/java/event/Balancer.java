package event;

/**
 * Created by mkhanwalkar on 5/5/15.
 */
public interface Balancer {

    public int getQueueNum(Message message);

    int getNumberOfQueues();
}
