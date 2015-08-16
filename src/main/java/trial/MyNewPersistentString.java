package trial;

import memlog.PersistentResource;

/**
 * Created by mkhanwalkar on 8/15/15.
 */
public class MyNewPersistentString extends PersistentResource {

    String load ;

    static char resourceId;

    static
    {
        setResourceId((char)100);

    }

    public  final char getResourceId() {
        return resourceId;
    }

    public static final void setResourceId(char r) {
        resourceId = r;
    }


    public MyNewPersistentString(String str)
    {
        load = str;
        length = load.length();
//        setResourceId((char)100);
    }

    public MyNewPersistentString(byte[] contents) {
        load = new String(contents);
        length = load.length();
  //      setResourceId((char)100);
    }

    @Override
    public byte[] getBytes() {
        return load.getBytes();
    }

    public static PersistentResource fromBytes(byte[] bytes)
    {
        return null;
    }


    @Override
    public String toString() {
        return "MyNewPersistentString{" +
                "load='" + load + '\'' +
                '}';
    }
}
