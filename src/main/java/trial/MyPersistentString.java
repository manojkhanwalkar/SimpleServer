package trial;

import memlog.PersistentResource;

/**
 * Created by mkhanwalkar on 8/15/15.
 */
public class MyPersistentString extends PersistentResource {

    String load ;

    public MyPersistentString(String str)
    {
        load = str;
        length = load.length();
    }

    public MyPersistentString(byte[] contents) {
        load = new String(contents);
        length = load.length();
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
