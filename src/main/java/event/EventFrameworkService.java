package event;


import cache.Cache;
import server.Service;

import java.util.Map;

public class EventFrameworkService implements Service {

    String name ;

    Map<String,EventFramework> frameworks;

    public Map<String, EventFramework> getFrameworks() {
        return frameworks;
    }

    public void setFrameworks(Map<String, EventFramework> frameworks) {
        this.frameworks = frameworks;
    }

    public void addFramework(String name , EventFramework framework)
    {
        frameworks.put(name,framework);
    }

    @Override
    public void init() {

        System.out.println(frameworks);

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
