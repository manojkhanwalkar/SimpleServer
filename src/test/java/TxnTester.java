import txn.Context;

import javax.transaction.UserTransaction;

/**
 * Created by mkhanwalkar on 8/5/15.
 */
public class TxnTester {

    public static void main(String[] args) throws Exception {

        UserTransaction ut =  Context.getUserTransaction();
        System.out.println(ut.getStatus());
        ut.begin();
        System.out.println(ut.getStatus());
       // ut.rollback();
        System.out.println(ut.getStatus());
        ut.commit();
        System.out.println(ut.getStatus());

    }
}
