package oracle.demo.jpa;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.eclipse.microprofile.config.spi.ConfigSource;

import io.helidon.config.mp.MpConfigSources;
import io.helidon.config.yaml.mp.YamlConfigSourceProvider;
import io.helidon.config.yaml.mp.YamlMpConfigSource;

/**
 * Dynamically configure "DemoDataSource" from "DEMO_DATASOURCE"
 */
public class DSConfigSource implements ConfigSource {

    private static final Logger logger = Logger.getLogger(DSConfigSource.class.getName());

    private final Map<String, String> p = new HashMap<>();

    public DSConfigSource(){

        final Map<String, String> tempMap = new HashMap<>();

        YamlConfigSourceProvider providor = new YamlConfigSourceProvider();
        Iterable<ConfigSource> i = providor.getConfigSources(DSConfigSource.class.getClassLoader());
        i.forEach(s -> {
            //logger.info("source: " + s.getName());
            //s.getProperties().forEach((k,v) -> logger.info(k + ": " + v));
            s.getProperties().forEach((k,v) -> tempMap.put(k, v));
        });


        final String dsName = tempMap.get("DEMO_DATASOURCE");
        Objects.requireNonNull(dsName);
        logger.info("Configuring DemoDataSource by copying '" + dsName + "'");

        tempMap.entrySet().forEach((e -> {
            final String key = e.getKey();
            if(key.startsWith("javax.sql.DataSource." + dsName)){
                final String newKey = key.replace(dsName, "DemoDataSource");
                p.put(newKey, e.getValue());
            } 
        }));

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

