package oracle.demo;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

/**
 * Simple Application that produces a greeting message.
 */
@ApplicationScoped
@ApplicationPath("/")
@OpenAPIDefinition(info = @Info(title = "Helidon MP Demo", version = "2.0"))
public class App extends Application {

}
