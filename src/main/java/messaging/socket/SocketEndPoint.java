package messaging.socket;

import event.EventFramework;
import event.Message;
import messaging.MessagingTuple;
import messaging.MessagingTupleWrapper;
import messaging.TransportEndPoint;
import trial.MyStringMessage;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by mkhanwalkar on 8/9/15.
 */
public class SocketEndPoint implements TransportEndPoint {

    int port ;

    // Interface to listen on. Defaults to loopback: the socket protocol has no authentication,
    // so exposing it on other interfaces must be an explicit choice (e.g. "0.0.0.0").
    String host = "127.0.0.1";

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    EventFramework eventFramework;

    public EventFramework getEventFramework() {
        return eventFramework;
    }

    public void setEventFramework(EventFramework eventFramework) {
        this.eventFramework = eventFramework;
    }

    @Override
    public void init() {

        Thread t = new Thread(()->{

            try {
                Selector selector = Selector.open();

                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

                serverSocketChannel.socket().bind(new InetSocketAddress(host, port));

                serverSocketChannel.configureBlocking(false);

                SelectionKey selectKy = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, null);

                while(true) {
                    selector.select();

                    Set<SelectionKey> selectedKeys = selector.selectedKeys();

                    Iterator<SelectionKey> it = selectedKeys.iterator();
                    while (it.hasNext()) {
                      SelectionKey key = it.next();
                      it.remove();   // remove via the iterator; Set.remove during for-each throws ConcurrentModificationException
                      try {
                        if (!key.isValid()) {
                            continue;   // cancelled by a worker after the peer disconnected
                        }
                        if (key.isAcceptable()) {
                            SocketChannel server = serverSocketChannel.accept();
                            server.configureBlocking(false);
                            server.register(selector,SelectionKey.OP_READ,null);
                        }
                        if (key.isReadable())
                        {
                            MessagingTuple tuple ;
                            MessagingTupleWrapper wrapper ;
                            Object attachment = key.attachment();
                            if (attachment==null)
                            {
                                SocketChannel server = (SocketChannel) key.channel();
                                tuple = new MessagingTuple();
                                tuple.setChannel(server);
                                tuple.setSelector(selector);
                                tuple.setKey(key);
                                wrapper = new MessagingTupleWrapper(tuple);

                                key.attach(wrapper);
                            }
                            else
                            {
                                wrapper = (MessagingTupleWrapper)attachment;
                            }


                            key.interestOps(key.interestOps()^SelectionKey.OP_READ);
                            //tuple.setKey(key);
                            eventFramework.sendMessage(wrapper);

                        }
                      } catch (RuntimeException e) {
                        // one misbehaving connection must not stop the listener thread
                        e.printStackTrace();
                      }

                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        });

        t.start();

    }

    @Override
    public void destroy() {

    }
}
