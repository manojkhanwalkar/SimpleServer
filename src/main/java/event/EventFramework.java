package event;

import com.lmax.disruptor.EventFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class EventFramework {

    Mode mode;
    int numQueues;
    Balancer balancer;
    Processor processor;
    final SynchronousQueue<Message> queue = new SynchronousQueue<Message>();

    List<BlockingQueue<Message>> queues ;
    List<LMaxQueue<Message>> lqueues;

    List<Thread> threads;
   // List<Executor> executors ;

    final int capacity = 8;

     Thread handoffWorker =null;


    EventFactory factory;

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setBalancer(Balancer balancer) {
        this.balancer = balancer;
        this.numQueues = balancer.getNumberOfQueues();
    }

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    public void setFactory(EventFactory factory) {
        this.factory = factory;
    }

    public EventFramework()
    {
            init();

    }


    public EventFramework(Mode mode, Balancer balancer, Processor processor, EventFactory factory) {
        this.mode = mode;
        this.numQueues = balancer.getNumberOfQueues();
        this.balancer = balancer;
        this.processor = processor;
        this.factory = factory;
        init();
    }

    public void init()
    {

       // EnumSet
        if (mode == Mode.Handoff)
        {
            handoffWorker = new Thread(() -> {
                try {
                    while(true) {
                        Message message = queue.take();
                        processor.process(message);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            handoffWorker.setDaemon(true);

            handoffWorker.start();
        }
        else
        {
            handoffWorker=null;
        }

        if (mode== Mode.Async)
        {
            queues = new ArrayList<>();
            threads = new ArrayList<>();
            for (int i=0;i<numQueues;i++)
            {
                ArrayBlockingQueue<Message> q = new ArrayBlockingQueue<Message>(capacity);
                queues.add(q);
                Thread t = new Thread(()-> {
                    try {
                        while(true) {
                            Message message = q.take();
                            processor.process(message);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                threads.add(t);
                t.setDaemon(true);
                t.start();
            }
        }

        if (mode== Mode.LMAX)
        {
            lqueues = new ArrayList<>();
          //  executors = new ArrayList<>();
            for (int i=0;i<numQueues;i++)
            {
                LMaxQueue<Message> lq = new LMaxQueue<Message>(capacity,processor,factory);
                lqueues.add(lq);

            }

        }


    }

    public void sendMessage(Message message)
    {
        switch(mode)
        {
            case Sync:
                processor.process(message);
                break ;
            case Handoff:
                try {
                    queue.put(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case Async:
                try {

                    int num =  balancer.getQueueNum(message);
                    queues.get(num).put(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                break ;

            case LMAX:

                int num = balancer.getQueueNum(message);
                lqueues.get(num).send(message);
                break;

        }

    }



}
