package oracle.demo.reactive;

import java.util.Arrays;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
@Produces(MediaType.APPLICATION_JSON)
public class ReactiveResource {

    private final Logger logger = Logger.getLogger(ReactiveResource.class.getName());

    private final ExecutorService es = ThreadPoolSupplier.builder().threadNamePrefix("messaging-").build().get();
    private final SubmissionPublisher<DaoEvent> publisher = new SubmissionPublisher<>(es, Flow.defaultBufferSize());

    @Inject private CountryDAO dao;

    private enum Operation {INSERT, UPDATE, DELETE}

    private class DaoEvent{
        public Operation op;
        public Country[] countries;
        public DaoEvent(Operation op, Country[] countries){
            this.op = op;
            this.countries = countries;
        }
    }

    @Outgoing("country-dao")
    public Publisher<DaoEvent> preparePublisher() {
        return ReactiveStreams.fromPublisher(FlowAdapters.toPublisher(publisher)).buildRs();
    }

    @Incoming("country-dao")
    public void consume(DaoEvent event) {
        logger.info("DaoEvent: " + event.op.toString());
        switch(event.op){
            case INSERT:
                dao.insertCountries(event.countries);
                break;
            case UPDATE:
                Arrays.stream(event.countries).forEach(c -> dao.updateCountry(c.countryId, c.countryName));
                break;
            case DELETE:
                Arrays.stream(event.countries).forEach(c -> dao.deleteCountry(c.countryId));
                break;
        }
    }


    @POST
    @Path("/")
    public void insertCountries(Country[] countries) {
        publisher.submit(new DaoEvent(Operation.INSERT, countries));
    }

    @PUT
    @Path("/{countryId}")
    public void updateCountry(@PathParam("countryId") int countryId, @FormParam("name") String countryName) {
        publisher.submit(new DaoEvent(Operation.UPDATE, new Country[]{new Country(countryId, countryName)}));
    }

    @DELETE
    @Path("/{countryId}")
    public void deleteCountry(@PathParam("countryId") int countryId) {
        publisher.submit(new DaoEvent(Operation.DELETE, new Country[]{new Country(countryId, null)}));
    }


}

