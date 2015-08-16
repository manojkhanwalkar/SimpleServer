package memlog;

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

    Map<Character,String> resources = new HashMap<>();

    public void registerResource(int id , String className)
    {
        resources.put((char)id,className);
    }

    public PersistentResource getResource(int id)
    {
        PersistentResource resource = null ;
        try {
           resource =(PersistentResource) Class.forName(resources.get((char)id)).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resource ;
    }

}
