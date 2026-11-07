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

    /** Largest payload accepted from a peer; the length prefix is untrusted. Override with -Dsimpleserver.socket.maxMessageBytes. */
    public static final int MAX_MESSAGE_BYTES = Integer.getInteger("simpleserver.socket.maxMessageBytes", 1024 * 1024);

    /** How long a peer may take to deliver a complete frame before the connection is dropped. Override with -Dsimpleserver.socket.readTimeoutMillis. */
    public static final long READ_TIMEOUT_MILLIS = Long.getLong("simpleserver.socket.readTimeoutMillis", 10_000L);

    /**
     * Fills the buffer, or returns false if the peer closed the connection or did not
     * deliver the bytes before the deadline. Non-blocking channels return 0 when no data
     * is available, so without a deadline a slow peer would pin the calling thread.
     */
    private static boolean readFully(SocketChannel channel, ByteBuffer buffer, long deadline) throws IOException {
        while (buffer.hasRemaining()) {
            int ret = channel.read(buffer);
            if (ret == -1)
                return false;
            if (ret == 0) {
                if (System.currentTimeMillis() > deadline)
                    return false;
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        }
        return true;
    }

    public static int read(SocketChannel server)
    {
        synchronized (server) {
            try {
                long deadline = System.currentTimeMillis() + READ_TIMEOUT_MILLIS;

                ByteBuffer buffer = ByteBuffer.allocate(4);
                if (!readFully(server, buffer, deadline)) {
                    System.out.println("Client Socket closed or timed out");
                    return -1;
                }

                buffer.flip();
                int len = buffer.getInt();

                if (len < 0 || len > MAX_MESSAGE_BYTES) {
                    System.out.println("Rejected frame with invalid length " + len + " (max " + MAX_MESSAGE_BYTES + ")");
                    return -1;
                }

                buffer = ByteBuffer.allocate(len);

                if (!readFully(server, buffer, deadline)) {
                    System.out.println("Client Socket closed or timed out while reading payload");
                    return -1;
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
