package txn;

/*import memlog.DataContainer;
import memlog.StoreService;
import memlog.TxnLog; */

import javax.transaction.*;
import javax.transaction.xa.XAResource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by mkhanwalkar on 4/30/15.
 */
public class TransactionImpl implements Transaction {

    Set<TransactionalResource> resources = new HashSet<>();

    @Override
    public void commit() throws RollbackException, HeuristicMixedException, HeuristicRollbackException, SecurityException, IllegalStateException, SystemException {

     //   TxnLog log = new TxnLog();

     /*   for (TransactionalResource resource : resources)
        {
            List<DataContainer> containers = resource.getData();

            for (DataContainer container : containers)
            {
                log.add(container);
            }

        }*/


     //   StoreService.getInstance().getStore().put(log);

        for (TransactionalResource resource : resources)
        {
            resource.commit();
        }

        resources.clear();

    }

    @Override
    public boolean delistResource(XAResource xaResource, int i) throws IllegalStateException, SystemException {
        return false;
    }

    @Override
    public boolean enlistResource(XAResource xaResource) throws RollbackException, IllegalStateException, SystemException {
        return false;
    }


    public boolean delistResource(TransactionalResource resource) throws IllegalStateException, SystemException {
        resources.remove(resource);
        return true;
    }


    public boolean enlistResource(TransactionalResource resource) throws RollbackException, IllegalStateException, SystemException {
        resources.add(resource);
        return true;
    }

    TxnStatus status = TxnStatus.Started;


    @Override
    public int getStatus() throws SystemException {
        return status.ordinal();
    }

    public void setStatus(TxnStatus status)
    {
        this.status = status ;
    }

    @Override
    public void registerSynchronization(Synchronization synchronization) throws RollbackException, IllegalStateException, SystemException {

    }

    @Override
    public void rollback() throws IllegalStateException, SystemException {
        this.status = TxnStatus.Rolledback;
    }

    @Override
    public void setRollbackOnly() throws IllegalStateException, SystemException {

        this.status = TxnStatus.Rolledback;

    }
}
