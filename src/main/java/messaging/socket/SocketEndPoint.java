package messaging.socket;

import messaging.MessagingTuple;
import messaging.TransportEndPoint;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * Created by mkhanwalkar on 8/9/15.
 */
public class SocketEndPoint implements TransportEndPoint {

    int port ;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void init() {

        Thread t = new Thread(()->{

            try {
                Selector selector = Selector.open();

                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

                serverSocketChannel.socket().bind(new InetSocketAddress(port));

                serverSocketChannel.configureBlocking(false);

                SelectionKey selectKy = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, null);

                while(true) {
                    selector.select();

                    Set<SelectionKey> selectedKeys = selector.selectedKeys();

                    for (SelectionKey key : selectedKeys) {
                        if (key.isAcceptable()) {
                            SocketChannel server = serverSocketChannel.accept();
                            server.configureBlocking(false);
                            server.register(selector,SelectionKey.OP_READ,null);
                        }
                        if (key.isReadable())
                        {
                            MessagingTuple tuple ;
                            Object attachment = key.attachment();
                            if (attachment==null)
                            {
                                SocketChannel server = (SocketChannel) key.channel();
                                tuple = new MessagingTuple();
                                tuple.setChannel(server);
                                key.attach(tuple);
                            }
                            else
                            {
                                tuple = (MessagingTuple)attachment;
                            }

                            tuple.processMessage(key);


                        }
                        selectedKeys.remove(key);

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
