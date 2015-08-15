package memlog;


import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DataContainer {


    byte[] contents;

    public byte[] getBytes()
    {
        return contents;
    }

    public void setContents(String s)
    {
        this.contents = s.getBytes(Charset.forName("US-ASCII"));
        length = contents.length;

    }

    int length ;

    public int getLength()
    {
        return length ;
    }

    public void setContents(byte[] contents)
    {
        this.contents = contents;
       length = this.contents.length;

    }

    public String getContents()
    {
        return new String(contents);
    }

    public String toString()
    {
        return getContents();
    }

}
