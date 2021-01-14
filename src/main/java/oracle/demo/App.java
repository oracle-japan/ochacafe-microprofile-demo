package oracle.demo;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

/**
 * It works without Application indeed, but intentionally kept here in order to add an OpenAPI annotation.
 */
@ApplicationScoped
@ApplicationPath("/")
@OpenAPIDefinition(info = @Info(title = "Helidon MP Demo", version = "2.2.0"))
public class App extends Application {

}
