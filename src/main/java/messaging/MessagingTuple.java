package messaging;

import event.Message;
import messaging.socket.SocketUtil;

import java.nio.channels.SelectionKey;
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

    public void processMessage(SelectionKey key)
    {
        int ret = SocketUtil.read(channel);
        if (ret!=-1 )
            SocketUtil.write(channel,"Received message");
        else
        {
            key.cancel();

        }

    }




}
