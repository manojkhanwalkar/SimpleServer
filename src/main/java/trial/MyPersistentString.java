package trial;

import memlog.PersistentResource;
import memlog.ResourceRegistry;

import java.lang.reflect.Method;

/**
 * Created by mkhanwalkar on 8/15/15.
 */
public class MyPersistentString extends PersistentResource {

    String load ;

    static char resourceId;

    static
    {
        try {
            setResourceId((char)101);
            Class c = Class.forName("trial.MyPersistentString");
            Method m = c.getMethod("fromBytes",byte[].class);

            ResourceRegistry.getInstance().registerResource(resourceId,m);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    public MyPersistentString() {

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
        return new MyPersistentString(bytes);
    }

    @Override
    public String toString() {
        return "MyPersistentString{" +
                "load='" + load + '\'' +
                '}';
    }
}
