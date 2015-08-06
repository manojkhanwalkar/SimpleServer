package event;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by mkhanwalkar on 5/5/15.
 */
public class RoundRobinLoadBalancer implements Balancer {

    final int totQueues ;

    AtomicInteger next = new AtomicInteger(0);
    public RoundRobinLoadBalancer(int totQueues)
    {
        this. totQueues = totQueues;
    }

    @Override
    public int getQueueNum(Message message) {

        int tmp = next.getAndIncrement();

        return tmp%totQueues;
    }

    @Override
    public int getNumberOfQueues() {
        return totQueues;
    }
}
