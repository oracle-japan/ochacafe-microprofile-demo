package oracle.demo.jpa;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 * JPA demo which Helidon provides
 */
@Dependent
@Path("jpa/example")
public class JPAExampleResource {

    @PersistenceContext(unitName = "GreetingDS")
    private EntityManager em;

    @GET
    @Path("response/{salutation}")
    @Produces("text/plain")
    @Transactional
    public String getResponse(@PathParam("salutation") String salutation) {
        final Greeting greeting = this.em.find(Greeting.class, salutation);
        final String returnValue;
        if (greeting == null) {
            returnValue = null;
        } else {
            returnValue = greeting.getResponse();
        }
        return returnValue;
    }

}

