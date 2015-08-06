import event.Processor;

/**
 * Created by mkhanwalkar on 5/5/15.
 */
public class ProcessorTester<T> implements Processor<T> {

    @Override
    public void process(T message) {

        System.out.println(message  + "  " + Thread.currentThread().getName());
    }
}
