package socket;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * Created by mkhanwalkar on 5/1/15.
 */
public class Server {

    public static void main(String[] args)  throws Exception {

        Selector selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        serverSocketChannel.socket().bind(new InetSocketAddress(9999));

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




    }




}
