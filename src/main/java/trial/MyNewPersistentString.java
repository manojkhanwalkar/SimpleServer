package trial;

import memlog.PersistentResource;
import memlog.ResourceRegistry;

import java.lang.reflect.Method;

/**
 * Created by mkhanwalkar on 8/15/15.
 */
public class MyNewPersistentString extends PersistentResource {

    String load ;

    static char resourceId;

    static
    {
        try {
            setResourceId((char)100);
            Class c = Class.forName("trial.MyNewPersistentString");
            Method m = c.getMethod("fromBytes",byte[].class);

            ResourceRegistry.getInstance().registerResource(resourceId,m);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    public MyNewPersistentString() {

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
        return new MyNewPersistentString(bytes);
    }


    @Override
    public String toString() {
        return "MyNewPersistentString{" +
                "load='" + load + '\'' +
                '}';
    }
}
