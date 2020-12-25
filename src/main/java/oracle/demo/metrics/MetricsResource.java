/*
 * Copyright (c) 2018, 2019 Oracle and/or its affiliates. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package oracle.demo.metrics;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Metric;
import org.eclipse.microprofile.metrics.annotation.RegistryType;

/**
 * implement application scope metrics
 */
@ApplicationScoped
@Path("/mpmetrics")
public class MetricsResource{

    private final Logger logger = Logger.getLogger(MetricsResource.class.getName());
    
    @Inject
    @Metric(name="total")
    private Counter total;

    @Inject
    @RegistryType(type=MetricRegistry.Type.APPLICATION)
    private MetricRegistry metricRegistry;

    private synchronized void countup(){
        total.inc();
    }

    @Metered
    @GET @Path("/apple")
    @Produces(MediaType.TEXT_PLAIN)
    public String apple(){
        logger.info("!!! APPLE");
        countup();
        return "APPLE";
    }   

    @Metered
    @GET @Path("/orange")
    @Produces(MediaType.TEXT_PLAIN)
    public String orange(){
        logger.info("!!! ORANGE");
        countup();
        return "ORANGE";
    }

    @GET @Path("/count-total")
    @Produces(MediaType.TEXT_PLAIN)
    public String total(){
        // this is the simplest way
        // Long.toString(total.getCount());
        
        // This time, get the counter through the registry
        final Counter counter = metricRegistry.getCounters()
            .entrySet().stream().filter(e -> {
                //System.out.println("Counter: " + e.getKey() + ", " + e.getValue());
                return e.getKey().getName().equals("oracle.demo.metrics.MetricsResource.total") ? true : false;
            }).findFirst().get().getValue();
        return Long.toString(counter.getCount());
    }   


}

