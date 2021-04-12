package oracle.demo.logging;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import io.helidon.logging.common.HelidonMdc;
import io.opentracing.Span;

/**
 * Set/unset Mdc value during invocation of target method 
 */
@Interceptor
//@Priority(Interceptor.Priority.APPLICATION - 100)
@Priority(0)
@Mdc
public class MdcInterceptor {

    private final Logger logger = Logger.getLogger(MdcInterceptor.class.getName());

    @Inject
    private io.opentracing.Tracer tracer;


    @AroundInvoke
    public Object obj(InvocationContext ic) throws Exception{

        //System.out.println(" ===== MdcInterceptor =====");
        final Method method = ic.getMethod();
        final Mdc mdc = method.getAnnotation(Mdc.class);
        final String key = mdc.key();
        logger.fine("Mdc key: " + mdc);
        final Optional<String> value = HelidonMdc.get(key);

        // set mdc only when the value doesn't exist
        String uuid = null;
        if(value.isEmpty()){
            // get open tracing id if available
            final Span span = tracer.activeSpan();
            final String id = Objects.nonNull(span) ? span.context().toTraceId() : UUID.randomUUID().toString();
            HelidonMdc.set(key, id);
            logger.fine(String.format("Set Mdc: %s=%s", key, uuid));
        }
        try{
            return ic.proceed();
        }finally{
            if(value.isEmpty()){
                HelidonMdc.remove(key);
                logger.fine(String.format("Clear Mdc: %s=%s", key, uuid));
            }
        }
    }
}