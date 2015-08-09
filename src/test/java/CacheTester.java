import cache.Cache;
import cache.Tuple;
import txn.Context;

import javax.transaction.UserTransaction;
import java.util.ArrayList;
import java.util.List;

public class CacheTester {

    public static void main(String[] args) throws Exception {

        {

            Cache<String, String> cache = new Cache<>();

            UserTransaction ut = Context.getUserTransaction();

            ut.begin();

            cache.put("Hello", "World");

            ut.rollback();

            System.out.println(cache);

        }

        {
            List<String> keys = new ArrayList<>();
            keys.add("Key1");

            Cache<String, String> cache = new Cache<>(keys);

            UserTransaction ut = Context.getUserTransaction();

            ut.begin();

            Tuple<String,String> t = new Tuple<>("Key1" , "Trial");
            List<Tuple<String,String>> tuples = new ArrayList<>();
            tuples.add(t);

            cache.put("Hello", "World",tuples);

         //   ut.rollback();

            ut.commit();

            System.out.println(cache.getUsingSecKey("Key1","Trial"));

            System.out.println(cache);

        }


    }
}
