import memlog.DataContainer;
import memlog.MemStore;
import memlog.StoreService;
import memlog.TxnLog;
import trial.MyNewPersistentString;
import trial.MyPersistentString;

/**
 * Created by mkhanwalkar on 4/28/15.
 */
public class MemLogTester {
    public static void main(String[] args) {

        // required to register the resource

        MyPersistentString mps = new MyPersistentString();
        MyNewPersistentString mnps = new MyNewPersistentString();

        StoreService service = StoreService.getInstance();

        service.addStore("test","/tmp/" , "memtest" , 1000 );

        service.init();

    /*    MemStore store = new MemStore();
        store.setReadCallback(containers -> {

            System.out.println(containers.toString());
        });

        store.init(); */

        MemStore store = service.getStore("test");



        for (int i=0;i<10; i++) {
            TxnLog log = new TxnLog();

            DataContainer container = new DataContainer();
            container.setResource(new MyPersistentString("Hello Mem Log World " + i));

            log.add(container);

            DataContainer container1 = new DataContainer();
            container1.setResource(new MyNewPersistentString("Hello Mem Log World AGAIN " + i));

            log.add(container1);


            store.put(log);

        }


    }

}
