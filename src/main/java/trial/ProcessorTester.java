package trial;

import event.Processor;
import messaging.MessagingTuple;
import messaging.MessagingTupleWrapper;

public class ProcessorTester<T> implements Processor<T> {

    @Override
    public void process(T message) {

        System.out.println(message  + "  " + Thread.currentThread().getName());

        if (message instanceof MessagingTupleWrapper) {

            MessagingTupleWrapper wrapper = (MessagingTupleWrapper) message;

            MessagingTuple tuple = (MessagingTuple) wrapper.getObject();

            tuple.processMessage();

            tuple.enableKey();
        }


    }
}
