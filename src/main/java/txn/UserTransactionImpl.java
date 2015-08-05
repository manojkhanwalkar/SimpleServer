package txn;

import javax.transaction.*;

/**
 * Created by mkhanwalkar on 4/29/15.
 */
public class UserTransactionImpl implements UserTransaction {

    TransactionManager manager ;

    public UserTransactionImpl(TransactionManager manager)
    {
        this.manager = manager;
    }

    @Override
    public void begin() throws NotSupportedException, SystemException {

        manager.begin();
    }

    @Override
    public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {
        manager.commit();
    }

    @Override
    public void rollback() throws IllegalStateException, SecurityException, SystemException {
        manager.rollback();

    }

    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {
        manager.setRollbackOnly();

    }

    @Override
    public int getStatus() throws SystemException {
            return manager.getStatus();
    }

    @Override
    public void setTransactionTimeout(int i) throws SystemException {

    }
}
