package oracle.demo.jpa.ecid;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import io.helidon.logging.common.HelidonMdc;
import oracle.demo.logging.Mdc;

/**
 * Connection is available only in a transaction (Eclipselink)
 */
@Interceptor
@Priority(Interceptor.Priority.APPLICATION + 100)
@Ecid
public class EcidInterceptor {

    private final Logger logger = Logger.getLogger(EcidInterceptor.class.getName());

    @AroundInvoke
    public Object obj(InvocationContext ic)throws Exception{

        final Object obj = ic.getTarget();
        if(!(obj instanceof EcidAware)){
            logger.warning("No action performed - target must have EcidAware interface: " + obj.getClass().getName());
            return ic.proceed();
        }

        final Method method = ic.getMethod();
        final Ecid annotation = method.getAnnotation(Ecid.class);
        final boolean setOnly = annotation.setOnly();

        final Optional<String> ecid = HelidonMdc.get(Mdc.ECID);
        final Connection con = ((EcidAware)obj).getSqlConnection();
        logger.fine("ECID: " + ecid.orElse(null));
        logger.fine("Connection: " + con);
        logger.fine("setOnly: " + setOnly);

        if(ecid.isPresent() && Objects.nonNull(con)){
            con.setClientInfo(Ecid.OCSID_ECID, ecid.get());
        }
        try{
            return ic.proceed();
        }finally{
            if(!setOnly && ecid.isPresent() && Objects.nonNull(con)){
                con.setClientInfo(Ecid.OCSID_ECID, null);
            }
        }
    }
}