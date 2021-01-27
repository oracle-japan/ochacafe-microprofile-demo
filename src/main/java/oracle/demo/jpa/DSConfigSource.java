package oracle.demo.jpa;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.microprofile.config.spi.ConfigSource;

import io.helidon.config.Config;

/**
 * Dynamically configure "DemoDataSource" from "DEMO_DATASOURCE"
 */
public class DSConfigSource implements ConfigSource {

    private static final Logger logger = Logger.getLogger(DSConfigSource.class.getName());

    private final Map<String, String> p = new HashMap<>();

    public DSConfigSource(){
        final Config config = Config.create();
        final String dsName = config.get("DEMO_DATASOURCE").asString().orElse("H2DataSource");
        logger.info("Configuring DemoDataSource by copying '" + dsName + "'");

        config.get("javax.sql.DataSource." + dsName).traverse()
            .filter(node -> node.isLeaf())
            .forEach(node -> {
                final String key = node.key().toString().replace("javax.sql.DataSource." + dsName, "javax.sql.DataSource.DemoDataSource");
                node.asString().ifPresent(value -> p.put(key, value));
            });

        final StringBuilder sb = new StringBuilder("DataSource properties:\n");
        p.keySet().stream().sorted().forEach(x -> sb.append(String.format("%s: %s%n", x, p.get(x))));
        logger.fine(sb.toString());
    }

    @Override
    public int getOrdinal() {
        return 120; // more than 100
    }

    @Override
    public Set<String> getPropertyNames() {
        return p.keySet();
    }

    @Override
    public Map<String, String> getProperties() {
        return p;
    }

    @Override
    public String getValue(String key) {
        return p.get(key);
    }

    @Override
    public String getName() {
        return "DSConfig";
    }

    public static void main(String[] args){ // for debug
        new DSConfigSource();
    }

}

