package oracle.demo.jpa.ecid;

import java.sql.Connection;
import java.util.Optional;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import oracle.demo.logging.Mdc;

/**
 * pass ECID via database connection
 */
@ApplicationScoped
@Path("ecid")
public class EcidExampleResource implements EcidAware{

    private final Logger logger = Logger.getLogger(EcidExampleResource.class.getName());

    @PersistenceContext(unitName = "Demo")
    private EntityManager em;

    @GET @Path("insert") @Produces("text/plain") // JAX-RS
    @Transactional // JTA
    @Mdc // set Mdc
    @Ecid // set ECID when available
    public String insertCountry(@QueryParam("id") Integer id, @QueryParam("name") String name, @QueryParam("delay") Integer delay) {

        logger.info(String.format("Insert (id = %d, name = %s, delay=%d)", id, name, delay));

        em.createStoredProcedureQuery("DEMO.INSERT_COUNTRY")
            .registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN)
            .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN)
            .setParameter(1, id)
            .setParameter(2, name)
            .setParameter(3, Optional.ofNullable(delay).orElse(30))
            .execute();

        return "OK\n";
    }

    @GET @Path("update") @Produces("text/plain") // JAX-RS
    @Transactional // JTA
    @Mdc // set Mdc
    @Ecid // set ECID when available
    public String updateCountry(@QueryParam("id") Integer id, @QueryParam("name") String name, @QueryParam("delay") Integer delay) {

        logger.info(String.format("Update (id = %d, name = %s, delay=%d)", id, name, delay));

        em.createStoredProcedureQuery("DEMO.UPDATE_COUNTRY")
            .registerStoredProcedureParameter(1, Integer.class, ParameterMode.IN)
            .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(3, Integer.class, ParameterMode.IN)
            .setParameter(1, id)
            .setParameter(2, name)
            .setParameter(3, Optional.ofNullable(delay).orElse(30))
            .execute();

        return "OK\n";

    }

	@Override
	public Connection getSqlConnection() {
        return em.unwrap(Connection.class);
	}


}

