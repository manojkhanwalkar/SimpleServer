package memlog;


import trial.MyPersistentString;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DataContainer {


    PersistentResource resource ;

    public char getType()  // TODO = fix this later
    {
        return 1;
    }

//    byte[] contents;

    public byte[] getBytes()
    {
        return resource.getBytes();
    }

    public void setResource(PersistentResource resource) {
        this.resource = resource;
    }


    public int getLength()
    {
        return resource.getLength() ;
    }

    public void setContents(byte[] contents)
    {
        resource = new MyPersistentString(contents); // TODO - needs to take into account type of resource


    }

    @Override
    public String toString() {
        return "DataContainer{" +
                "resource=" + resource +
                '}';
    }
}
