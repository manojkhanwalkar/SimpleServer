package cache;

public class Tuple<K,V> {

    K keyType ;
    V keyValue ;
    public Tuple(K keyType , V keyValue)
    {
        this.keyType = keyType;
        this.keyValue = keyValue;

    }

    public K getKeyType() {
        return keyType;
    }

    public void setKeyType(K keyType) {
        this.keyType = keyType;
    }

    public V getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(V keyValue) {
        this.keyValue = keyValue;
    }
}
