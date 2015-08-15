package memlog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mkhanwalkar on 4/30/15.
 */
public class StoreService {

    static StoreService service = new StoreService();

    public static StoreService getInstance()
    {
        return service;
    }

    private StoreService() {}

    private Map<String,MemStore> stores = new HashMap<>();

    public void addStore(String name , String dirName , String fileName , int size)
    {
        MemStore store = new MemStore(dirName,fileName,size);
        stores.put(name,store);
    }

    public void init()
    {

        stores.values().stream().forEach(store -> {

            store.setReadCallback(containers -> {

                System.out.println(containers.toString());
            });

            store.init();

        });
    }

    public MemStore getStore(String name)
    {
        return stores.get(name);
    }


}
