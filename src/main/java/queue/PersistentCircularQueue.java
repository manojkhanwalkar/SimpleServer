package queue;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by mkhanwalkar on 4/21/15.
 */
public class PersistentCircularQueue {

    //TODO - throws buffer underflow exception , need to investigate .

    volatile int head =0;
    volatile int tail =0;

    String name ;
    int size;
    ByteBuffer writeArea;
    ByteBuffer readArea;

    ByteBuffer indexPos ;
  //  ByteBuffer tailPos ;

    RandomAccessFile raf;
    FileChannel fc;

    FileChannel fcIndex;

    static final byte EOR = 1;

    Producer producer ;
    Consumer consumer ;



    public PersistentCircularQueue(String name, int size)
    {
        this.name = name;
        this.size = size ;

        producer = new Producer(this);
        consumer = new Consumer(this);

    }


    public void initQueueForWrite()
    {
        try {
            File f = new File("/tmp/" + name);
             raf = new RandomAccessFile(f, "rw");
             fc = raf.getChannel();
            writeArea = fc.map(FileChannel.MapMode.READ_WRITE, 0, size);
            readArea = writeArea;


            File fIndex = new File("/tmp/" + name + ".readwritepos");
            RandomAccessFile rafIndex = new RandomAccessFile(fIndex, "rw");
            fcIndex = rafIndex.getChannel();
            indexPos = fcIndex.map(FileChannel.MapMode.READ_WRITE, 0, 8);
            head = indexPos.getInt();
            tail = indexPos.getInt();
           // queueState = indexPos.getChar();
            indexPos.flip();


            System.out.println("Next write location is " + head);
            System.out.println("Next read location is " + tail);


            Thread.sleep(5000);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


 /*   public void initQueueForRead()
    {
        try {
            File f = new File("/tmp/" + name);
            raf = new RandomAccessFile(f, "rw");
            fc = raf.getChannel();
            readArea = fc.map(FileChannel.MapMode.READ_ONLY, 0, size);


            File fIndex = new File("/tmp/" + name + ".readwritepos");
            RandomAccessFile rafIndex = new RandomAccessFile(fIndex, "rw");
             fcIndex = rafIndex.getChannel();
            indexPos = fcIndex.map(FileChannel.MapMode.READ_WRITE, 0, 10);
            head = indexPos.getInt();
            tail = indexPos.getInt();
            indexPos.flip();


            System.out.println("Next read location is " + tail );


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
*/


    public Producer getProducer()
    {
        return producer ;
    }

    public Consumer getConsumer()
    {
        return consumer;
    }

    private void write(byte[] bytes)
    {
        writeArea.position(head);
        writeArea.putInt(bytes.length);
        writeArea.put(bytes);
        head += bytes.length;
        head += 4;

    }

    private int numToWrite(byte[] bytes)
    {
        return bytes.length+4;
    }

  synchronized void  put(byte[] bytes)
    {
        System.out.println("Current head posn" + head );
        if (head>tail || (head==0 && tail==0)) {

            if (writeArea.capacity() - (head + numToWrite(bytes)) > 0) {
                write(bytes);
            } else // remaining is negative so a wrap around is required.
            {
                    writeArea.position(head);
                    writeArea.put(EOR);
                    System.out.println("EOR written at " + head);

                while (tail <= numToWrite(bytes)) {
                    try {
                        System.out.println("WAITING 1 !!!! " + head);
                        wait(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                head = 0;
                write(bytes);
         }
        }
        else
        {

            if (head+numToWrite(bytes)<tail)
            {
                write(bytes);
            }
            else
            {
                if (writeArea.capacity() - (head + numToWrite(bytes)) > 0) // wait for tail to move ahead or wrap
                {
                    while (head < tail &&  head+numToWrite(bytes) >= tail )
                    {
                        try {
                            System.out.println("WAITING 2!!!! " + head );
                            wait(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                        write(bytes);
                }
                else // not enough space to write after tail has moved also . have to wait for tail to wrap and then read
                {
                    writeArea.position(head);
                    writeArea.put(EOR);

                    while (tail < numToWrite(bytes)) {
                        try {
                            System.out.println("WAITING 3 !!!! " + head);
                            wait(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    head = 0;
                    write(bytes);
                }
            }

        }

        System.out.println("head posn end of function" + head );

        indexPos.position(0);
        indexPos.putInt(head);
        notifyAll();


    }


    synchronized String read()
    {
        System.out.println("Current read posn " + tail);

        while (tail==head)
        {
            try {
                wait(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (tail == readArea.capacity()|| readArea.get(tail)==EOR) {
            tail = 0;
        }

        if (tail==0 && head ==0)
        {
            notifyAll();
            return null;   // empty queue
        }

            String s = null;


                readArea.position(tail);
                int length = readArea.getInt();
                byte[] bytes = new byte[length];
                readArea.get(bytes);
                tail = tail + length + 4;
                s= new String(bytes);

        System.out.println(" read posn at end " + tail);

        indexPos.position(4);
        indexPos.putInt(tail);

        notifyAll();

        return s;


    }

    public void close()
    {
        try {
            fc.force(true);
            fc.close();
            fcIndex.force(true);
            fc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
