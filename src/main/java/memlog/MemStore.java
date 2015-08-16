package memlog;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by mkhanwalkar on 4/21/15.
 */
public class MemStore {

    String name ;
    int size;
    String dir;
    int fileNumber=0;

    ByteBuffer writeArea = null ;

    RandomAccessFile raf;
    FileChannel fc;


     MemStore(String dir , String name, int size)
    {
        this.name = name;
        this.size = size ;
        this.dir = dir;

    }

    ReadCallback callback;
    public void setReadCallback(ReadCallback callback)
    {
        this.callback = callback;
    }





    public  void unmap(Buffer buffer) {
        if(buffer.isDirect()) {
            try {
                if(!buffer.getClass().getName().equals("java.nio.DirectByteBuffer")) {
                    Field attField = buffer.getClass().getDeclaredField("att");
                    attField.setAccessible(true);
                    buffer = (Buffer) attField.get(buffer);
                }

                Method cleanerMethod = buffer.getClass().getMethod("cleaner");
                cleanerMethod.setAccessible(true);
                Object cleaner = cleanerMethod.invoke(buffer);
                Method cleanMethod = cleaner.getClass().getMethod("clean");
                cleanMethod.setAccessible(true);
                cleanMethod.invoke(cleaner);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void map()
    {
        try {
            File f = new File(dir + name + fileNumber++);
            raf = new RandomAccessFile(f, "rw");
            fc = raf.getChannel();
            writeArea = fc.map(FileChannel.MapMode.READ_WRITE, 0, size);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void read()
    {
        while(true) {
            try {
                File f = new File(dir + name + fileNumber);
                if (f.exists()) {
                    raf = new RandomAccessFile(f, "rw");
                    fc = raf.getChannel();
                    writeArea = fc.map(FileChannel.MapMode.READ_WRITE, 0, size);

                    while (writeArea.get(writeArea.position()) != TxnLog.EOF) {

                        byte b = writeArea.get();
                        if (b == TxnLog.BOR) {
                            int length = writeArea.getInt();

                            if (writeArea.get(writeArea.position() + length) == TxnLog.EOR) {
                                ByteBuffer buffer = writeArea.slice();
                                buffer.limit(length + 1);
                                TxnLog log = new TxnLog(buffer);

                                callback.notify(log.getDataContainers());


                            } else {
                                System.out.println("Error in the record ");
                                System.exit(1); // for now stop recovery
                            }

                        }

                    }

                    fileNumber++;
                } else {
                    if (writeArea != null) {
                        int position = writeArea.position();
                        writeArea.flip();
                        writeArea.position(position);
                    } else {
                        map();
                    }
                    break;

                }


            } catch (Exception e) {
                e.printStackTrace();
                break ;
            }

        }

    }




    synchronized public void put(TxnLog log)
    {
        ByteBuffer buffer = log.getLog();

        if (writeArea.remaining() < buffer.capacity()) {
            unmap(writeArea);
            map();
        }

        writeArea.put(buffer);
        writeArea.position(writeArea.position()-1); // to overwrite the EOF if next record is written.

    }



    public void close()
    {

    }

    public void init() {
        read();

        // map();

    }
}
