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
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Metric;

/**
 * implement application scope metrics
 */
@ApplicationScoped
@Path("/mpmetrics")
public class MetricsResource{

    private final Logger logger = Logger.getLogger(MetricsResource.class.getName());
    
    @Inject
    @Metric(name="total")
    Counter total;

    private synchronized void countup(){
        total.inc();
    }

    @Metered
    @GET @Path("/blue")
    @Produces(MediaType.TEXT_PLAIN)
    public String blue(){
        logger.info("!!! BLUE");
        countup();
        return "BLUE";
    }   

    @Metered
    @GET @Path("/green")
    @Produces(MediaType.TEXT_PLAIN)
    public String green(){
        logger.info("!!! GREEN");
        countup();
        return "GREEN";
    }   

}

