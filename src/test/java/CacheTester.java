import cache.Cache;
import cache.Tuple;
import txn.Context;

import javax.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.List;

public class CacheTester {

    public static void main(String[] args) throws Exception {

/*        {

            Cache<String, String> cache = new Cache<>();

            UserTransaction ut = Context.getUserTransaction();

            ut.begin();

            cache.put("Hello", "World");

            ut.rollback();

            System.out.println(cache);

        } */

        {
            List<String> keys = new ArrayList<>();
            keys.add("Key1");

            List<String> nukeys = new ArrayList<>();
            nukeys.add("NUKey1");

            Cache<String, String> cache = new Cache<>(keys,nukeys);

            UserTransaction ut = Context.getUserTransaction();

            ut.begin();

            Tuple<String,String> t = new Tuple<>("Key1" , "Trial");
            List<Tuple<String,String>> tuples = new ArrayList<>();
            tuples.add(t);

            Tuple<String,String> t1 = new Tuple<>("NUKey1" , "NUTrial");
            List<Tuple<String,String>> tuples1 = new ArrayList<>();
            tuples1.add(t1);

            cache.put("Hello", "World",tuples, tuples1);

         //   ut.rollback();

            ut.commit();

            System.out.println(cache.getUsingSecKey("Key1", "Trial"));
            System.out.println(cache.getUsingSecNUKey("NUKey1", "NUTrial"));

            System.out.println(cache);

        }


    }
}
