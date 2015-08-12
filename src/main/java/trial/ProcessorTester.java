package trial;

import event.Message;
import event.Processor;
import messaging.MessagingService;
import messaging.MessagingTuple;
import messaging.MessagingTupleWrapper;
import messaging.rabbit.ConnectionInitiator;
import server.Server;

import java.util.concurrent.atomic.AtomicInteger;

public class ProcessorTester<T> implements Processor<T> {

    static AtomicInteger count = new AtomicInteger(0);

    @Override
    public void process(T message) {

        System.out.println(message  + "  " + Thread.currentThread().getName());

        if (message instanceof MessagingTupleWrapper) {

            MessagingTupleWrapper wrapper = (MessagingTupleWrapper) message;

            MessagingTuple tuple = (MessagingTuple) wrapper.getObject();

            tuple.processMessage();

            tuple.enableKey();
        }
        else
        {
            // goes in a loop and breaks .
            if (count.getAndIncrement()< 1000) {
                MessagingService service = (MessagingService) Server.getService("MessagingService");
                ConnectionInitiator initiator = (ConnectionInitiator) service.getMessageEndPoint("Rabbit-1");
                initiator.send((Message) message, "hello");
            }

        }


    }
}
