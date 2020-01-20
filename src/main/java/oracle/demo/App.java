package oracle.demo;

import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import io.helidon.common.CollectionsHelper;
import oracle.demo.country.CountryNotFoundExceptionMapper;
import oracle.demo.cowweb.CowwebResource;
import oracle.demo.echo.EchoResource;
import oracle.demo.ft.FaultToleranceResource;
import oracle.demo.greeting.GreetResource;
import oracle.demo.health.HealthCheckResource;
import oracle.demo.jpa.JPAExampleResource;
import oracle.demo.metrics.MetricsResource;
import oracle.demo.restclient.MovieReviewServiceResource;
import oracle.demo.restclient.MovieReviewServiceRestClientResource;
import oracle.demo.security.SecurityResource;
import oracle.demo.tracing.TracingResource;

/**
 * Simple Application that produces a greeting message.
 */
@ApplicationScoped
@ApplicationPath("/")
public class App extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return CollectionsHelper.setOf(
            GreetResource.class, 
            EchoResource.class,
            oracle.demo.country.CountryResource.class,
            oracle.demo.jpa.CountryResource.class,
            CountryNotFoundExceptionMapper.class,
            CowwebResource.class,
            TracingResource.class,
            HealthCheckResource.class,
            MetricsResource.class,
            FaultToleranceResource.class,
            MovieReviewServiceResource.class,
            MovieReviewServiceRestClientResource.class,
            SecurityResource.class,
            JPAExampleResource.class,
            oracle.demo.grpc.javaobj.GrpcResource.class,
            oracle.demo.grpc.protobuf.GrpcResource.class
        );
    }
}
