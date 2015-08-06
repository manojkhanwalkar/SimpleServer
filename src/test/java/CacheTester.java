import cache.Cache;
import txn.Context;

import javax.transaction.UserTransaction;

/**
 * Created by mkhanwalkar on 8/5/15.
 */
public class CacheTester {

    public static void main(String[] args) throws Exception {

        Cache<String,String> cache = new Cache<>();

        UserTransaction ut = Context.getUserTransaction();

        ut.begin();

        cache.put("Hello" , "World");

        ut.rollback();

        System.out.println(cache);

    }
}