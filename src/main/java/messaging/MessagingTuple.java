package messaging;

public class MessagingTuple {

    String name ;

    Initiator initiator ;

    Acceptor acceptor ;

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


}
