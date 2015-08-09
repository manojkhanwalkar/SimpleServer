package cache;

import txn.Context;
import txn.TransactionImpl;
import txn.TransactionalResource;

import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class Cache<K,V> implements TransactionalResource {

    ConcurrentMap<K, V> map = new ConcurrentHashMap<>();
    ThreadLocal<Map<K, V>> localMap  = new ThreadLocal<>();
    ConcurrentMap<K,Long> locks = new ConcurrentHashMap<>();

    Map<String,ConcurrentMap<K, V>> secKeyMap ; //= new HashMap<>();
    ThreadLocal<Map<K,Map<K, V>>> localSecMap  = new ThreadLocal<>();

    Map<String,ConcurrentMap<K, Set<V>>> secNUKeyMap ; //= new HashMap<>();
    ThreadLocal<Map<K,Map<K, Set<V>>>> localSecNUMap  = new ThreadLocal<>();


//TODO - generated cache code will be far more efficient for multiple keys .

    //TODO - create a secondary unique key functionality in the cache .

    public Cache()
    {


    }

    public Cache(List<String> secondaryKeys, List<String> secNonUniqueKeys)
    {
         secKeyMap = new HashMap<>();
        secondaryKeys.stream().forEach(key->{

            secKeyMap.put(key,new ConcurrentHashMap<>());
        });

        secNUKeyMap = new HashMap<>();
        secNonUniqueKeys.stream().forEach(key->{

            secNUKeyMap.put(key,new ConcurrentHashMap<>());
        });

    }




    public V getUsingSecKey(K keyType,K key)
    {

        Map<K,Map<K, V>> txnMap = localSecMap.get();
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

    public Set<V> getUsingSecNUKey(K keyType,K key)
    {

        Map<K,Map<K, Set<V>>> txnMap = localSecNUMap.get();
        if (txnMap!=null)
        {
            Map<K, Set<V>> txnMap1 = txnMap.get(keyType);
            Set<V> kv = txnMap1.get(key);
            if (kv!=null)
                return kv;
        }

        ConcurrentMap<K, Set<V>> tmp = secNUKeyMap.get(keyType);
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


    public void put(K key , V obj, List<Tuple<K,K>> secKeyInfo) throws KeyLockedException
    {
        try {
            TransactionImpl transaction = (TransactionImpl) Context.getTransactionManager().getTransaction();
            transaction.enlistResource(this);
        } catch (SystemException e) {
            e.printStackTrace();
        } catch (RollbackException e) {
            e.printStackTrace();
        }

        // this logic makes it re-entrant for the current thread .
        // assume lock only on the primary key .
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

        // Add the sec unique keys
        secKeyInfo.stream().forEach(t->{
            K keyType = t.getKeyType();
            K keyValue = t.getKeyValue();
      //      ThreadLocal<Map<String,Map<K, V>>> localSecMap  = new ThreadLocal<>();
            Map<K,Map<K, V>> tmp = localSecMap.get();
            if (tmp==null)
            {
                tmp = new HashMap<K, Map<K, V>>();
                localSecMap.set(tmp);
            }

            Map<K, V> tmp1 = tmp.get(keyType);
            if (tmp1==null)
            {
                tmp1= new HashMap<K, V>();
                tmp.put(keyType,tmp1);
            }
            tmp1.put(keyValue,obj);


        });


    }


    public void rollback() {
        Map<K, V> txnMap = localMap.get();
        if (txnMap != null) {
            txnMap.clear();
        }
        Map<K,Map<K, V>> tmp = localSecMap.get();
        if (tmp!=null)
            tmp.clear();
        Map<K,Map<K, Set<V>>> tmp1 = localSecNUMap.get();
        if (tmp1!=null)
            tmp1.clear();


    }


    public void commit()
    {
        {
            Map<K, Map<K, V>> tmp = localSecMap.get();
            if (tmp != null) {
                for (Map.Entry<K, Map<K, V>> locentry : tmp.entrySet()) {
                    Map<K, V> entry = secKeyMap.get(locentry.getKey());
                    if (entry != null) {
                        for (Map.Entry<K, V> locentry1 : locentry.getValue().entrySet())  // should have only one entry
                        {

                            entry.put(locentry1.getKey(), locentry1.getValue());
                        }
                    }
                }

                tmp.clear();

            }
        }

        Map<K,Map<K, Set<V>>> tmp = localSecNUMap.get();
        if (tmp!=null)
        {
            for (Map.Entry<K,Map<K, Set<V>>> locentry : tmp.entrySet())
            {
                Map<K, Set<V>> entry = secNUKeyMap.get(locentry.getKey());
                if (entry!=null)
                {
                    for (Map.Entry<K, Set<V>> locentry1 : locentry.getValue().entrySet())
                    {
                        Set<V> entry1 = entry.get(locentry1.getKey());
                        if (entry1==null) {
                            entry.put(locentry1.getKey(), locentry1.getValue());
                        }
                        else
                        {
                            entry1.addAll(locentry1.getValue());
                        }
                    }
                }
            }

            tmp.clear();

        }


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
                ", secKeyMap=" + secKeyMap +
                ", secNUKeyMap=" + secNUKeyMap +
                '}';
    }
/*
  public void put(K key , V obj, List<Tuple<K,K>> secKeyInfo) throws KeyLockedException
    {
        try {
            TransactionImpl transaction = (TransactionImpl) Context.getTransactionManager().getTransaction();
            transaction.enlistResource(this);
        } catch (SystemException e) {
            e.printStackTrace();
        } catch (RollbackException e) {
            e.printStackTrace();
        }

        // this logic makes it re-entrant for the current thread .
        // assume lock only on the primary key .
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

        // Add the sec keys
        secKeyInfo.stream().forEach(t->{
            K keyType = t.getKeyType();
            K keyValue = t.getKeyValue();
      //      ThreadLocal<Map<String,Map<K, V>>> localSecMap  = new ThreadLocal<>();
            Map<K,Map<K, V>> tmp = localSecMap.get();
            if (tmp==null)
            {
                tmp = new HashMap<K, Map<K, V>>();
                localSecMap.set(tmp);
            }

            Map<K, V> tmp1 = tmp.get(keyType);
            if (tmp1==null)
            {
                tmp1= new HashMap<K, V>();
                tmp.put(keyType,tmp1);
            }
            tmp1.put(keyValue,obj);


        });


    }

 */


}
