package oracle.demo.jpa;

import java.util.logging.Logger;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.weld.exceptions.IllegalArgumentException;

@Dependent
public class EntityManagerUtil {

    private static final Logger logger = Logger.getLogger(EntityManagerUtil.class.getName());

    static {
        // in case of Oracle, disable fan to avoid waiting for fan initialization timeout
        System.setProperty("oracle.jdbc.fanEnabled", "false");
    }

    @PersistenceContext(unitName = "H2")
    private EntityManager emH2;

    @PersistenceContext(unitName = "Oracle")
    private EntityManager emOracle;

    @PersistenceContext(unitName = "MySQL")
    private EntityManager emMySQL;

    private final String persistenceUnit;

    @Inject
    public EntityManagerUtil(@ConfigProperty(name = "demo.persistence-unit", defaultValue = "H2") String persistenceUnit){
        this.persistenceUnit = persistenceUnit;
        logger.info("persistenceUnit: " + persistenceUnit);
    }

    @Produces
    @DemoDS
    public EntityManager getEntityManger(){
        EntityManager em = null;
        switch(persistenceUnit){
            case "H2":
                em = emH2;
                break;
            case "Oracle":
                em = emOracle;
                break;
            case "MySQL":
                em = emMySQL;
                break;
            default:
                throw new IllegalArgumentException("Unknown persiteceUnit: " + persistenceUnit);
        }
        return em;
    }

}
