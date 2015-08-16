package memlog;

public abstract class PersistentResource {

    protected int length;

   public abstract char getResourceId() ;


    public abstract byte[] getBytes();

    public static PersistentResource fromBytes(byte[] bytes)
    {
        return null;
    }


    public int getLength(){
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }


}
