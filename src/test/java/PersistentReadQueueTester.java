import queue.Consumer;
import queue.PersistentCircularQueue;

public class PersistentReadQueueTester {

    public static void main(String[] args) throws Exception {
        PersistentCircularQueue queue = new PersistentCircularQueue("test", 24000870);
        queue.initQueueForRead();

        Thread reader = new Thread(() -> {

                Consumer consumer = queue.getConsumer();
            int count = 0;
            while(true) {
                    String s = consumer.read();
                if (s==null)
                    {
                        System.out.println("Consumer waiting as queue empty" + count);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                count++;
                  //  System.out.println(s  + "  " + count++);
                }

        });


        reader.start();


        reader.join();
        queue.close();


    }
}
