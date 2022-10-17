package oracle.demo;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

/**
 * It works without Application indeed, but intentionally kept here in order to add an OpenAPI annotation.
 */
@ApplicationScoped
@ApplicationPath("/")
@OpenAPIDefinition(info = @Info(title = "Helidon MP Demo", version = "3.x"))
public class App extends Application {

}
