package messaging.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by mkhanwalkar on 5/1/15.
 */
public class SocketUtil {

    public static void write(SocketChannel client , String s)
    {

        synchronized (client) {

            try {
                byte[] message = new String(s).getBytes();
                int len = message.length;
                ByteBuffer lenBuff = ByteBuffer.allocate(4);
                lenBuff.putInt(len);
                lenBuff.flip();
                client.write(lenBuff);
                ByteBuffer buffer = ByteBuffer.wrap(message);
                client.write(buffer);

                buffer.clear();
            } catch (IOException e) {
                e.printStackTrace();

            }
        }

    }

    public static int read(SocketChannel server)
    {
        synchronized (server) {
            try {
                ByteBuffer buffer = ByteBuffer.allocate(4);
                int toRead = 4;
                while (toRead > 0) {

                    int ret = server.read(buffer);
                    if (ret == -1) {
                        System.out.println("Client Socket closed ");
                        return ret;
                    }

                    toRead = toRead - ret;
                }

                buffer.flip();
                int len = buffer.getInt();

                buffer = ByteBuffer.allocate(len);

                while (len > 0) {

                    int ret = server.read(buffer);
                    if (ret == -1)
                        throw new RuntimeException("Socket closed while reading payload");

                    len = len - ret;
                }

                buffer.flip();
                String output = new String(buffer.array()).trim();

                System.out.println("Message  " + output);

            } catch (IOException e) {

                e.printStackTrace();
                return -1;
            }

            return 0; // ok
        }


    }


}
