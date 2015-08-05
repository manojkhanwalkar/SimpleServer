package txn;

import javax.transaction.*;
import java.util.IllegalFormatCodePointException;

/**
 * Created by mkhanwalkar on 4/30/15.
 */
public class TransactionManagerImpl implements TransactionManager {

    static ThreadLocal<Transaction> transactions = new ThreadLocal<>();


    @Override
    public void begin() throws NotSupportedException, SystemException {
        Transaction tran = new TransactionImpl();

        transactions.set(tran);

    }

    @Override
    public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
        Transaction tran = transactions.get();
        if (tran==null)
            throw new IllegalStateException();

        transactions.remove();
        if (tran.getStatus()==TxnStatus.Started.ordinal()) {
            tran.commit();
        }
        else if (tran.getStatus()==TxnStatus.Rolledback.ordinal())
        {
            throw new RollbackException();
        } else if (tran.getStatus()==TxnStatus.Committed.ordinal())
        {
            // do nothing as already committed .
        } else
        {
            throw new IllegalStateException();
        }

    }

    @Override
    public int getStatus() throws SystemException {
        Transaction tran = transactions.get();
        if (tran!=null)
              return tran.getStatus();
        else
              return TxnStatus.NotInTransaction.ordinal();

    }

    @Override
    public Transaction getTransaction() throws SystemException {
        Transaction tran = transactions.get();
        return tran;
    }

    @Override
    public void resume(Transaction transaction) throws InvalidTransactionException, IllegalStateException, SystemException {

    }

    @Override
    public void rollback() throws IllegalStateException, SecurityException, SystemException {
        Transaction tran = transactions.get();
        if (tran!=null) {
            tran.rollback();
            transactions.remove();
        }

    }

    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        Transaction tran = transactions.get();
        tran.setRollbackOnly();

    }

    @Override
    public void setTransactionTimeout(int i) throws SystemException {

    }

    @Override
    public Transaction suspend() throws SystemException {
        return null;
    }
}
