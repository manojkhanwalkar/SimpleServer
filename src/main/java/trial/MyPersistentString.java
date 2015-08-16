package trial;

import memlog.PersistentResource;

/**
 * Created by mkhanwalkar on 8/15/15.
 */
public class MyPersistentString extends PersistentResource {

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

    public MyPersistentString(String str)
    {
        load = str;
        length = load.length();
  //      setResourceId((char)101);
    }

    public MyPersistentString(byte[] contents) {
        load = new String(contents);
        length = load.length();
    //    setResourceId((char)101);
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
        return "MyPersistentString{" +
                "load='" + load + '\'' +
                '}';
    }
}
