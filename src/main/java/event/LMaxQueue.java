package event;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by mkhanwalkar on 5/7/15.
 */
public class LMaxQueue<T> implements EventHandler<T> {

    Executor executor = Executors.newSingleThreadExecutor();
    RingBuffer<T> ringBuffer;

    int bufSize ;

    Processor processor ;

    EventFactory factory; // = new MyStringMessage();

    public LMaxQueue(int bufSize, Processor processor, EventFactory factory)
    {
        this.bufSize = bufSize;
        this.processor = processor;
        this.factory = factory;

        Disruptor<T> disruptor = new Disruptor<>(factory, bufSize, executor);

        disruptor.handleEventsWith(this);

        disruptor.start();

        ringBuffer = disruptor.getRingBuffer();


    }

    public void send(T t )
    {
        long sequence = ringBuffer.next();  // Grab the next sequence
        Message event = (Message)ringBuffer.get(sequence);

        event.setString(((Message)t).getString());

        ringBuffer.publish(sequence);
    }



    @Override
    public void onEvent(T t, long l, boolean b) throws Exception {

        processor.process(t);

    }
}


/*

public class Controller {

    // Executor that will be used to construct new threads for consumers
    Executor executor = Executors.newFixedThreadPool(2);


    Producer producer ;


    public Controller()
    {
        // The factory for the event
        MyStringFactory factory = new MyStringFactory();

        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 8;

        // Construct the Disruptor


        // Connect the handler
        disruptor.handleEventsWith(new Consumer("C1") , new Consumer("C2"));

        // Start the Disruptor, starts all threads running

        // Get the ring buffer from the Disruptor to be used for publishing.


        producer = new Producer(ringBuffer);


    }


    public void send(String s)
    {
        producer.send(s);
    }


}

 */