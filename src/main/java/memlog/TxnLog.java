package memlog;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mkhanwalkar on 4/28/15.
 */
public class TxnLog {

    static final byte EOR = 1;
    static final byte BOR = 2;
    static final byte EOF = 3;



    int length;
    List<DataContainer> dataContainers= new ArrayList<>();

    public TxnLog()
    {

    }

    public List<DataContainer> getDataContainers()
    {
        return dataContainers;
    }

    // this is to be used for reading . BOR , TotLength is already read .
    public TxnLog(ByteBuffer buffer)
    {
        while (buffer.remaining()>0)
        {
            int length = buffer.getInt();
            byte[] b = new byte[length];
            char type = buffer.getChar();
            buffer.get(b);
            DataContainer container = new DataContainer();
            container.setType(type);
            container.setContents(b);
            dataContainers.add(container);
            if (buffer.get(buffer.position())==EOR)
                break;
        }
    }

    public void add(DataContainer dataContainer)
    {
        dataContainers.add(dataContainer);
        length=length+dataContainer.getLength()+4+2;
    }


    /* Length calculation

        BOR , Tot Length , for each container - payload and length , EOR and EOF . EOF gets overwritten if another record is written after it.
    */

    public ByteBuffer getLog()
    {

        ByteBuffer buffer = ByteBuffer.allocate(7+length);
        buffer.put(BOR);
        buffer.putInt(length);
        for (DataContainer container : dataContainers)
        {
            buffer.putInt(container.getLength());
            char c = container.getType();
            buffer.putChar(c);
            buffer.put(container.getBytes());
        }
        buffer.put(EOR);
        buffer.put(EOF);
        buffer.flip();
        return buffer ;
    }

    public String toString()
    {
        return dataContainers.toString();
    }



}
