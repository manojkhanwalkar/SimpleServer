package memlog;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mkhanwalkar on 8/15/15.
 */
public class ResourceRegistry {

    static ResourceRegistry registry = new ResourceRegistry();

    public static ResourceRegistry getInstance()
    {
        return registry ;
    }

    Map<Character,Method> resources = new HashMap<>();

    public void registerResource(int id , Method method)
    {
        resources.put((char)id,method);
    }

    public Method getResource(int id)
    {
        return resources.get((char)id);
    }

}
