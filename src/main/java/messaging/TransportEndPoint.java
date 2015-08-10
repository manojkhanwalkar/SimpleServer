package messaging;

public interface  TransportEndPoint {

    public void init();

    public void destroy();

// set an EF
    // take message and handover a wrapper to EF
    // wrapper contains Initiator which contains client channel to send message back to the sending client .

}
