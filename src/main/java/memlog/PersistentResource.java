package memlog;

public abstract class PersistentResource {

    private char resourceId;
    protected int length;

    public final char getResourceId() {
        return resourceId;
    }

    public final void setResourceId(char resourceId) {
        this.resourceId = resourceId;
    }

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

    @Override
    public String toString() {
        return "PersistentResource{" +
                "resourceId=" + resourceId +
                ", length=" + length +
                '}';
    }
}
