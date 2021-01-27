package oracle.demo.logging;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.logging.Logger;

import javax.enterprise.context.Dependent;

import io.helidon.common.configurable.ThreadPoolSupplier;

@Dependent
public class Sub{

    private final Logger logger = Logger.getLogger(Sub.class.getName());

    private final ExecutorService es = ThreadPoolSupplier.builder().threadNamePrefix("sub-").build().get();

    @Mdc
    public String get() {
        logger.info("Sub#get() called");
        final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            logger.info("Thread started");
            return "OK\n";
        }, es);
        final String result = future.join();
        logger.info("Thread ended");
        return result;
    }

}