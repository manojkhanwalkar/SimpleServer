import event.EventFramework;
import event.Mode;
import event.RoundRobinLoadBalancer;

/**
 * Created by mkhanwalkar on 5/5/15.
 */
public class EventTester {

    public static void main(String[] args) throws Exception {

       EventFramework ef = new EventFramework(Mode.LMAX,new RoundRobinLoadBalancer(4),new ProcessorTester(), new MyStringMessage());

        for (int i=0;i<10;i++) {
            ef.sendMessage(new MyStringMessage("Hello Event World"));

        }

        Thread.sleep(1000);

    /*    Predicate<Integer>  mint = x-> x>5;

        System.out.println(mint.test(5) );*/



    }


}
