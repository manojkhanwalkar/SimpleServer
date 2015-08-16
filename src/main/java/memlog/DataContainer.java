package memlog;


import trial.MyNewPersistentString;
import trial.MyPersistentString;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

        try {
            Method m = ResourceRegistry.getInstance().getResource(type);
            resource = (PersistentResource)m.invoke(null,contents);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "DataContainer{" +
                "resource=" + resource +
                '}';
    }
}
