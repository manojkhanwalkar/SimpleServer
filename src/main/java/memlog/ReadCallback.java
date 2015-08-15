package memlog;

import java.util.List;

/**
 * Created by mkhanwalkar on 4/28/15.
 */
public interface ReadCallback {

    void notify(List<DataContainer> containers);
}
