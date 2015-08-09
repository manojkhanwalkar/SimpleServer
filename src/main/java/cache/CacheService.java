package cache;


import server.Service;

import java.util.Map;

public class CacheService implements Service {

    String name ;

    Map<String,Cache> caches;

    public Map<String, Cache> getCaches() {
        return caches;
    }

    public void setCaches(Map<String, Cache> caches) {
        this.caches = caches;
    }

    public void addCache(String name , Cache cache)
    {
        caches.put(name,cache);
    }

    @Override
    public void init() {

        System.out.println(caches);

    }

    @Override
    public void setName(String s) {

        name = s;
    }

    @Override
    public String getName() {
        return name;
    }
}
