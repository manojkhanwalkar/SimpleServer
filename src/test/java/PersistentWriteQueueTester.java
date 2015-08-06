import queue.PersistentCircularQueue;
import queue.Producer;

public class PersistentWriteQueueTester {

    public static void main(String[] args) throws Exception {
        PersistentCircularQueue queue = new PersistentCircularQueue("test", 24000870);
        queue.initQueueForWrite();

        Thread writer = new Thread(new Runnable() {
            @Override
            public void run() {

              Producer producer = queue.getProducer();
                int count=0;
              while(true) {
                  boolean success = producer.put("Hello Mem Map world again nknnjnkjnknkjnkjjn");
                  try {
                      Thread.sleep(10);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
                  if (!success) {
                      try {
                          System.out.println("Producer waiting as queue full " + count);
                          Thread.sleep(1000);
                      } catch (InterruptedException e) {
                          e.printStackTrace();
                      }
                      continue;
                  }

                  count++;
              }

            }
        });

        writer.start();



        writer.join();
        queue.close();


    }
}
