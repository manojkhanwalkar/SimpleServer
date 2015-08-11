package queue;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by mkhanwalkar on 4/21/15.
 */
public class PersistentCircularQueue {

    //TODO - throws buffer underflow exception , need to investigate .

    int head =0;
    int tail =0;
    char queueState;

    String name ;
    int size;
    ByteBuffer writeArea;
    ByteBuffer readArea;

    ByteBuffer indexPos ;
  //  ByteBuffer tailPos ;

    RandomAccessFile raf;
    FileChannel fc;

    static final char EMPTY = 0;
    static final char FULL = 1;
    static final char NOTEMPTY = 2;
    static final char NOTFULL = 3;

    static final byte EOR = 1;

    Producer producer ;
    Consumer consumer ;

    volatile boolean readyForWrite= false;
    volatile boolean readyForRead = false ;


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



            File fIndex = new File("/tmp/" + name + ".readwritepos");
            RandomAccessFile rafIndex = new RandomAccessFile(fIndex, "rw");
            FileChannel fcIndex = rafIndex.getChannel();
            indexPos = fcIndex.map(FileChannel.MapMode.READ_WRITE, 0, 10);
            head = indexPos.getInt();
            tail = indexPos.getInt();
            queueState = indexPos.getChar();
            indexPos.flip();


            System.out.println("Next write location is " + head);
            System.out.println("Queue state is " + queueState);


            readyForWrite=true;

            Thread.sleep(5000);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void initQueueForRead()
    {
        try {
            File f = new File("/tmp/" + name);
            raf = new RandomAccessFile(f, "rw");
            fc = raf.getChannel();
            readArea = fc.map(FileChannel.MapMode.READ_ONLY, 0, size);


            File fIndex = new File("/tmp/" + name + ".readwritepos");
            RandomAccessFile rafIndex = new RandomAccessFile(fIndex, "rw");
            FileChannel fcIndex = rafIndex.getChannel();
            indexPos = fcIndex.map(FileChannel.MapMode.READ_WRITE, 0, 10);
            head = indexPos.getInt();
            tail = indexPos.getInt();
            queueState = indexPos.getChar();
            indexPos.flip();


            System.out.println("Next read location is " + tail );
            System.out.println("Queue state is " + queueState);

            readyForRead = true ;

            Thread.sleep(5000);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public Producer getProducer()
    {
        return producer ;
    }

    public Consumer getConsumer()
    {
        return consumer;
    }

    synchronized boolean  put(byte[] bytes)
    {
        if (!readyForWrite)
            return false ;

        boolean success = true;

        indexPos.position(8);
        queueState = indexPos.getChar();


        if ( queueState==FULL)
            return false;


        if (head>=tail) {
            int remaining = writeArea.capacity() - (head + bytes.length + 4);
            if (remaining >= 0) {
                writeArea.position(head);
                writeArea.putInt(bytes.length);
                writeArea.put(bytes);
                head += bytes.length;
                head += 4;

                if (head == tail) {
                    queueState = FULL;
                }
                else
                {
                    queueState = NOTEMPTY;
                }
                indexPos.position(8);
                indexPos.putChar(queueState);

            } else // remaining is negative so a wrap around is required.
            {
                if (head!=writeArea.capacity()) {
                    writeArea.position(head);
                    writeArea.put(EOR);
                }
                head = 0;
                remaining = Math.abs(remaining);

                if (tail - head >= remaining) {
                    writeArea.position(head);
                    writeArea.putInt(bytes.length);
                    writeArea.put(bytes);
                    head += bytes.length;
                    head += 4;
                    if (head == tail) {
                        queueState = FULL;
                    }
                    else
                    {
                        queueState = NOTEMPTY;
                    }
                    indexPos.position(8);
                    indexPos.putChar(queueState);

                } else {
                    queueState = FULL;
                    indexPos.position(8);
                    indexPos.putChar(FULL);
                    success = false;
                   // return false;

                }

            }
        }
        else
        {
            if (head+bytes.length+4<=tail)
            {
                writeArea.position(head);
                writeArea.putInt(bytes.length);
                writeArea.put(bytes);
                head += bytes.length;
                head += 4;
                if (head == tail) {
                    queueState = FULL;
                }
                else
                {
                    queueState = NOTEMPTY;
                }
                indexPos.position(8);
                indexPos.putChar(queueState);

            }
            else
            {
                queueState = FULL;
                indexPos.position(8);
                indexPos.putChar(FULL);
               // return false;
                success=false;

            }

        }

        indexPos.position(0);
        indexPos.putInt(head);

        return success;

    }

    synchronized String read()
    {
        if (!readyForRead)
            return null ;

        indexPos.position(8);
        queueState = indexPos.getChar();

            if ( queueState == EMPTY)
                return null;

            String s = null;

            if (tail == readArea.capacity()|| readArea.get(tail)==EOR) {
                tail = 0;
                if (head==tail)
                {
                    queueState = EMPTY;
                    indexPos.position(8);
                    indexPos.putChar(EMPTY);
                    //return null;

                }
            }
            else {

                readArea.position(tail);
                int length = readArea.getInt();
                byte[] bytes = new byte[length];
                readArea.get(bytes);
                tail = tail + length + 4;
                if (head == tail) {
                    queueState = EMPTY;
                }
                else
                {
                    queueState = NOTFULL;
                }
                indexPos.position(8);
                indexPos.putChar(queueState);
                s= new String(bytes);

            }

        indexPos.position(4);
        indexPos.putInt(tail);



        return s;


    }

    public void close()
    {

    }

}
