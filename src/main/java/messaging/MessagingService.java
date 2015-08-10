package messaging;

import server.Service;

import java.util.HashMap;
import java.util.Map;

public class MessagingService  implements Service {

    String name ;

    Map<String,TransportEndPoint>  messageEndPoints ;

    public Map<String, TransportEndPoint> getMessageEndPoints() {
        return messageEndPoints;
    }

    public void setMessageEndPoints(Map<String, TransportEndPoint> messageEndPoints) {
        this.messageEndPoints = messageEndPoints;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void init() {
        messageEndPoints.values().stream().forEach(endPoint->{

            endPoint.init();

        });
    }

    @Override
    public void destroy() {
        messageEndPoints.values().stream().forEach(endPoint->{

            endPoint.destroy();

        });

    }
}
