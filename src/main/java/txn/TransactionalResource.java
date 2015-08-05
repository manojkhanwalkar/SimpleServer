package txn;

//import memlog.DataContainer;

import java.util.List;

/**
 * Created by mkhanwalkar on 4/29/15.
 */
public interface TransactionalResource {

    public void commit();
    public void rollback();
  //  public List<DataContainer> getData();


}
