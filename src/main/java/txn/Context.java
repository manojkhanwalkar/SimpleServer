package txn;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

/**
 * Created by mkhanwalkar on 4/29/15.
 */
public class Context {

    static class Holder
    {
        static TransactionManager transactionManager = new TransactionManagerImpl();

    }


    public static UserTransaction getUserTransaction()
    {
        UserTransaction ut = new UserTransactionImpl(Holder.transactionManager);
        return ut;
    }

  /*  public static TransactionManager getTransactionManager()
    {
        return Holder.transactionManager;
    }*/
}
