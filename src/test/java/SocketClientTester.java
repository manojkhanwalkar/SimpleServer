import messaging.socket.SocketUtil;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * Created by mkhanwalkar on 5/1/15.
 */

    // TODO - common messaging api over REST , Sockets and Rabbit
    //TODO - messaging handoff to event layer if configured in that fashion .
    // TODO - tie a persistence manager to cache via the txn layer


public class SocketClientTester {

    public static void main (String [] args)
            throws Exception {

        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 9999);
        SocketChannel client = SocketChannel.open(hostAddress);

        System.out.println("Client sending messages to server...");

        // Send messages to server

        String [] messages = new String [] {"Time goes fast.", "What now?", "Bye."};

        for (int i = 0; i < messages.length; i++) {

            SocketUtil.write(client, messages[i]);
            SocketUtil.read(client);


            Thread.sleep(3000);
        }

        client.close();
    }


}
