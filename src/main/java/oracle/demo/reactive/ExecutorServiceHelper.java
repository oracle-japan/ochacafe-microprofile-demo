package oracle.demo.reactive;

import java.util.Objects;
import java.util.concurrent.ExecutorService;

import io.helidon.common.configurable.ThreadPoolSupplier;

public class ExecutorServiceHelper {
    private static ExecutorService es;

    public static synchronized ExecutorService getExecutorService(){
        if(Objects.isNull(es)){
            es = ThreadPoolSupplier.builder().threadNamePrefix("messaging-").build().get();
        }
        return es;
    }
    
}
