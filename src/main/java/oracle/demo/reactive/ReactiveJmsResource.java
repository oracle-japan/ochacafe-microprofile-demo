package oracle.demo.reactive;

import java.util.Arrays;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.ObjectMessage;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.eclipse.microprofile.reactive.messaging.Acknowledgment;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.FlowAdapters;
import org.reactivestreams.Publisher;

import io.helidon.messaging.connectors.jms.JmsMessage;
import oracle.demo.jpa.Country;
import oracle.demo.jpa.CountryDAO;
import oracle.demo.reactive.DaoEvent.Operation;


/**
 * Reactive way to update database via JPA
 *
 */
@ApplicationScoped
@Path("/reactive/jms/country")
public class ReactiveJmsResource {

    private final Logger logger = Logger.getLogger(ReactiveResource.class.getName());

    private final Jsonb jsonb = JsonbBuilder.create();

    private final ExecutorService es = ExecutorServiceHelper.getExecutorService();
    private final SubmissionPublisher<Message<String>> publisher = new SubmissionPublisher<>(es, Flow.defaultBufferSize());


    @Inject private CountryDAO dao;

    //@Outgoing("to-jms")
    public Publisher<Message<String>> preparePublisher() {
        return ReactiveStreams.fromPublisher(FlowAdapters.toPublisher(publisher)).buildRs();
    }

    //@Incoming("from-jms")
    @Acknowledgment(Acknowledgment.Strategy.MANUAL)
    public CompletionStage<?> consume(Message<String> message) {
        final DaoEvent event = jsonb.fromJson(message.getPayload(), DaoEvent.class);
        try{
            switch(event.getOperation()){
                case INSERT:
                    dao.insertCountries(event.getCountries());
                    break;
                case UPDATE:
                    Arrays.stream(event.getCountries()).forEach(c -> dao.updateCountry(c.getCountryId(), c.getCountryName()));
                    break;
                case DELETE:
                    Arrays.stream(event.getCountries()).forEach(c -> dao.deleteCountry(c.getCountryId()));
                    break;
            }
            logger.info(String.format("Complete: %s", jsonb.toJson(event)));
        }catch(Exception e){
            logger.log(Level.WARNING, String.format("Error[%s]: %s", e.getMessage(), jsonb.toJson(event)), e);
        }
        return message.ack();
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
        Message<String> message = JmsMessage.builder(jsonb.toJson(event)).build();
        publisher.submit(message); // then submit an event
    }

}

