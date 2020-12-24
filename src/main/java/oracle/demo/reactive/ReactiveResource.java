package oracle.demo.reactive;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.FlowAdapters;
import org.reactivestreams.Publisher;

import io.helidon.common.configurable.ThreadPoolSupplier;
import oracle.demo.jpa.Country;
import oracle.demo.jpa.CountryDAO;

/**
 * Reactive way to update database via JPA
 *
 */
@ApplicationScoped
@Path("/reactive/country")
public class ReactiveResource {

    private final Logger logger = Logger.getLogger(ReactiveResource.class.getName());

    private final Jsonb jsonb = JsonbBuilder.create();

    private final ExecutorService es = ThreadPoolSupplier.builder().threadNamePrefix("messaging-").build().get();
    private final SubmissionPublisher<DaoEvent> publisher = new SubmissionPublisher<>(es, Flow.defaultBufferSize());

    @Inject private CountryDAO dao;

    public enum Operation {INSERT, UPDATE, DELETE}

    public class DaoEvent{
        public UUID uuid;
        public Operation operation;
        public Country[] countries;
        public DaoEvent(Operation op, Country[] countries){
            this.operation = op;
            this.countries = countries;
            this.uuid = UUID.randomUUID();
        }
    }

    @Outgoing("country-dao")
    public Publisher<DaoEvent> preparePublisher() {
        return ReactiveStreams.fromPublisher(FlowAdapters.toPublisher(publisher)).buildRs();
    }

    @Incoming("country-dao")
    public void consume(DaoEvent event) {
        try{
            switch(event.operation){
                case INSERT:
                    dao.insertCountries(event.countries);
                    break;
                case UPDATE:
                    Arrays.stream(event.countries).forEach(c -> dao.updateCountry(c.getCountryId(), c.getCountryName()));
                    break;
                case DELETE:
                    Arrays.stream(event.countries).forEach(c -> dao.deleteCountry(c.getCountryId()));
                    break;
            }
            logger.info(String.format("Complete: %s", jsonb.toJson(event)));
        }catch(Exception e){
            logger.log(Level.WARNING, String.format("Error[%s]: %s", e.getMessage(), jsonb.toJson(event)), e);
        }
    }

    @POST
    @Path("/")
    public void insertCountries(Country[] countries) {
        submit(new DaoEvent(Operation.INSERT, countries));
    }

    @PUT
    @Path("/")
    public void updateCountry(Country[] countries) {
        submit(new DaoEvent(Operation.UPDATE, countries));
    }

    @PUT
    @Path("/{countryId}")
    public void updateCountryById(@PathParam("countryId") int countryId, @FormParam("name") String countryName) {
        submit(new DaoEvent(Operation.UPDATE, new Country[]{new Country(countryId, countryName)}));
    }

    @DELETE
    @Path("/{countryId}")
    public void deleteCountryById(@PathParam("countryId") int countryId) {
        submit(new DaoEvent(Operation.DELETE, new Country[]{new Country(countryId, null)}));
    }

    private void submit(DaoEvent event){
        logger.info(String.format("Event: %s", jsonb.toJson(event))); // record an event first
        publisher.submit(event); // then submit an event
    }

}

