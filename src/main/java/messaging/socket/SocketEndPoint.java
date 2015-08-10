package messaging.socket;

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
                            SocketChannel server = (SocketChannel) key.channel();
                            int ret = SocketUtil.read(server);
                            if (ret!=-1 )
                                SocketUtil.write(server,"Received message");
                            else
                            {
                                key.cancel();
                            }

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
