package messaging;

import event.Message;
import messaging.socket.SocketUtil;

import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class MessagingTuple {



    String name ;

    Initiator initiator ;

    Acceptor acceptor ;

    SocketChannel channel ;

    public SocketChannel getChannel() {
        return channel;
    }

    public void setChannel(SocketChannel channel) {
        this.channel = channel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Initiator getInitiator() {
        return initiator;
    }

    public void setInitiator(Initiator initiator) {
        this.initiator = initiator;
    }

    public Acceptor getAcceptor() {
        return acceptor;
    }

    public void setAcceptor(Acceptor acceptor) {
        this.acceptor = acceptor;
    }



    public void processMessage()
    {
        int ret = SocketUtil.read(channel);
        if (ret!=-1 )
            SocketUtil.write(channel,"Received message");
      else
        {
            key.cancel();

        }

    }


    SelectionKey key ;
    public void setKey(SelectionKey key) {

        this.key = key;

    }

    Selector selector ;
    public void setSelector(Selector selector) {

        this.selector = selector ;

    }

    public void enableKey() {

//        key.interestOps(SelectionKey.OP_READ);

        if (key.isValid()) {
            key.interestOps(key.interestOps() ^ SelectionKey.OP_READ);
            selector.wakeup();
        }

    }
}
