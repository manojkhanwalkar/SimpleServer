package messaging.rabbit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import event.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by mkhanwalkar on 5/8/15.
 */
public class RabbitDelegate implements Delegate {

    String host = "localhost";
    int port = 5672;

    int numConnections = 5;

    ConnectionFactory factory;

    List<Connection> connections;

    BlockingQueue<Channel> channels ;   // Assume twice the channels as connections

    @Override
    public void init() {

        try {
            factory = new ConnectionFactory();
            factory.setHost(host);
            connections = new ArrayList<>(numConnections);
            for (int i=0;i<numConnections;i++) {
                Connection connection = factory.newConnection();
                connections.add(connection);
            }

            channels = new ArrayBlockingQueue<Channel>(numConnections*2);

            for (int i=0;i<numConnections*2 ; i++) {

                Channel channel = connections.get(i % numConnections).createChannel();
                channels.add(channel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void destroy() {
        try {

            List<Channel> chans = new ArrayList<>();

            channels.drainTo(chans);
            for (int i=0;i<chans.size();i++)
                chans.get(i).close();

            for (int i=0;i<connections.size();i++)
                 connections.get(i).close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //  channel.queueDeclare(QUEUE_NAME, false, false, false, null);  -- need this to send to the queue , else queue needs to exist.

    @Override
    public void send(Message message, String queue) {

        Channel channel = null;
        try {
             channel = channels.take();
             channel.basicPublish("", queue, null, message.getObject().toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally
        {
            if (channel!=null)
                try {
                    channels.put(channel);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }

    }



    public Receiver getReceiver(String name)
    {
        try {
            return new RabbitReceiver(channels.take(), name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null ;
    }

}



