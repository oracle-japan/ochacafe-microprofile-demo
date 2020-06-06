package oracle.demo.tracing.interceptor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import io.opentracing.Span;

// https://github.com/opentracing/specification/blob/master/semantic_conventions.md

@Interceptor
@Priority(Interceptor.Priority.APPLICATION)
@Trace
public class TraceInterceptor {

    private final Logger logger = Logger.getLogger(TraceInterceptor.class.getName());

    @Inject
    io.opentracing.Tracer tracer;

    @AroundInvoke
    public Object obj(InvocationContext ic)throws Exception{

        final Method method = ic.getMethod();
        final String clazz = method.getDeclaringClass().getName();
        final String methodName = method.getName();

        String spanName = clazz + "." + methodName;
        boolean stackTrace = false;

        // Check TraceConfig
        final Trace trace = method.getAnnotation(Trace.class);
        final String value = trace.value();
        logger.fine("Trace prefix: " + value);
        if(null != value && 0 != value.length()){
            spanName = value + ":" + spanName;
        }
        stackTrace = trace.stackTrace();

        // check TraceTag
        final Map<String, String> tagMap = new HashMap<>();
        TraceTag[] tags = method.getAnnotationsByType(TraceTag.class);
        if(null != tags){
            for(TraceTag tag : tags){
                final String k = tag.key();
                final String v = tag.value();
                if(null == k) throw new IllegalArgumentException("TraceTag key is null.");
                if(null == v) throw new IllegalArgumentException("TraceTag value is null.");
                tagMap.put(k, v);
            }
        }

        final Span span = tracer.buildSpan(spanName)
        .asChildOf(tracer.activeSpan()).start();
        tagMap.forEach((k,v) -> span.setTag(k, v));

        Object result = null;
        try{
            result = ic.proceed();
            span.setTag("error", false).finish(); 
        }catch(Exception e){
            if(stackTrace){
                StringWriter sw = new StringWriter();
                try(PrintWriter pw = new PrintWriter(sw)){
                    e.printStackTrace(pw);
                }
                span.log(sw.toString());
            }else{
                span.log(e.getMessage());
            }
            span.setTag("error", true).finish();
            throw e; 
        }

        return result;
    }
}