import queue.Consumer;
import queue.PersistentCircularQueue;
import queue.Producer;

/**
 * Created by mkhanwalkar on 8/14/15.
 */
public class PersistentQueueTester {

    public static void main(String[] args) throws Exception {
        PersistentCircularQueue queue = new PersistentCircularQueue("test", 2000);

        queue.initQueueForWrite();

        Thread writer = new Thread(new Runnable() {
            @Override
            public void run() {

                Producer producer = queue.getProducer();
                int count=0;
                while(true) {
                    producer.put("Hello Mem Map world again " + count);
                   // System.out.println(count++);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        writer.start();

        Thread reader = new Thread(() -> {

            Consumer consumer = queue.getConsumer();
            int count =0;
            while(true) {
                String s = consumer.read();
                //System.out.println(s);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });


        reader.start();

        writer.join();
        reader.join();
        queue.close();



    }
}
