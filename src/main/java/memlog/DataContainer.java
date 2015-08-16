package memlog;


import trial.MyNewPersistentString;
import trial.MyPersistentString;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DataContainer {


    PersistentResource resource ;

    char type ;

    public void setType(char type)
    {
        this.type = type;
    }

    public char getType()  // TODO = fix this later
    {
        return (char) resource.getResourceId();
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
        switch(type)
        {
            case 101:
                resource = new MyPersistentString(contents); // TODO - needs to take into account type of resource
                break ;
            case 100:
                resource = new MyNewPersistentString(contents);
                break ;
            default :
                System.out.println("Error");

        }


    }

    @Override
    public String toString() {
        return "DataContainer{" +
                "resource=" + resource +
                '}';
    }
}
