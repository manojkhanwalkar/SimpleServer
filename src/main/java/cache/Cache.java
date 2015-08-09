package cache;

import txn.Context;
import txn.TransactionImpl;
import txn.TransactionalResource;

import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class Cache<K,V> implements TransactionalResource {

    ConcurrentMap<K, V> map = new ConcurrentHashMap<>();
    ThreadLocal<Map<K, V>> localMap  = new ThreadLocal<>();
    ConcurrentMap<K,Long> locks = new ConcurrentHashMap<>();

    Map<String,ConcurrentMap<K, V>> secKeyMap ; //= new HashMap<>();
    ThreadLocal<Map<String,Map<K, V>>> localSecMap  = new ThreadLocal<>();

//TODO - generated cache code will be far more efficient for multiple keys .

    //TODO - create a secondary unique key functionality in the cache .

    public Cache()
    {


    }

    public Cache(List<String> secondaryKeys)
    {
         secKeyMap = new HashMap<>();
        secondaryKeys.stream().forEach(key->{

            secKeyMap.put(key,new ConcurrentHashMap<>());
        });
    }




    public V getUsingSecKey(String keyType,K key)
    {

        Map<String,Map<K, V>> txnMap = localSecMap.get();
        if (txnMap!=null)
        {
            Map<K, V> txnMap1 = txnMap.get(keyType);
             V kv = txnMap1.get(key);
             if (kv!=null)
                 return kv;
        }

        ConcurrentMap<K, V> tmp = secKeyMap.get(keyType);
        if (tmp!=null)
        {
            return tmp.get(key) ;
        }
        else
            return null;




    }

    public V get(K key)
    {
        Map<K, V> txnMap = localMap.get();
        if (txnMap!=null)
        {
            V kv = txnMap.get(key);
            if (kv!=null)
                return kv;
        }

        return map.get(key) ;
    }



    public void put(K key , V obj) throws KeyLockedException
    {
        try {
            TransactionImpl transaction = (TransactionImpl) Context.getTransactionManager().getTransaction();
            transaction.enlistResource(this);
        } catch (SystemException e) {
            e.printStackTrace();
        } catch (RollbackException e) {
            e.printStackTrace();
        }

       // Key k = obj.getKey();

        // this logic makes it re-entrant for the current thread .
        long currVal = Thread.currentThread().getId();

        Long val = locks.putIfAbsent(key,new Long(currVal));

        if (val!=null && currVal!=val)
        {
            throw new KeyLockedException(key.toString());
        }

            // map.put(k,obj);
       Map<K, V> txnMap = localMap.get();
        if (txnMap==null)
        {
            txnMap = new HashMap<>();
            localMap.set(txnMap);
        }

        txnMap.put(key,obj);


    }

    public void rollback() {
        Map<K, V> txnMap = localMap.get();
        if (txnMap != null) {
            txnMap.clear();
        }
    }

    public void commit()
    {
        Map<K,V> txnMap = localMap.get();
        if (txnMap!=null)
        {
            for (Map.Entry<K, V> entry : txnMap.entrySet())
            {
                map.put(entry.getKey(), entry.getValue());
                locks.remove(entry.getKey());
            }
            txnMap.clear();
        }

    }

    @Override
    public String toString() {
        return "Cache{" +
                "map=" + map +
                '}';
    }

    /*  public List<DataContainer> getData()
    {
        List<DataContainer> containers = new ArrayList<>();
        Map<Key, KeyedValue> txnMap = localMap.get();
        if (txnMap!=null)
        {
            for (Map.Entry<Key, KeyedValue> entry : txnMap.entrySet())
            {
                DataContainer container = new DataContainer();
                container.setContents(entry.getValue().getBytes());
                containers.add(container);
            }
        }

        return containers;


    } */




}
