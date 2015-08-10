package event;

import txn.Context;

import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * Created by mkhanwalkar on 8/9/15.
 */
public class ProcessorWrapper<T> {

    Processor<T> processor ;

    public void setProcessor(Processor<T> processor) {
        this.processor = processor;
    }


    public void process(T message)
    {
        UserTransaction txn = Context.getUserTransaction();
        try {
            txn.begin();
            processor.process(message);
            txn.commit();
        }catch(Exception e)
        {
            e.printStackTrace();
            try {
                txn.rollback();
            } catch (SystemException e1) {
                e1.printStackTrace();
            }
        }

    }
}
