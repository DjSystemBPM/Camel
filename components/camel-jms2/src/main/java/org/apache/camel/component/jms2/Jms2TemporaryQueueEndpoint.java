/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.jms2;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TemporaryQueue;

import org.apache.camel.component.jms.DestinationEndpoint;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.component.jms.QueueBrowseStrategy;

/**
 * A <a href="http://activemq.apache.org/jms.html">JMS Endpoint</a>
 * for working with a {@link TemporaryQueue}
 * <p/>
 * <b>Important:</b> Need to be really careful to always use the same Connection otherwise the destination goes stale
 *
 * @version
 */
public class Jms2TemporaryQueueEndpoint extends Jms2QueueEndpoint implements DestinationEndpoint {
    private Destination jmsDestination;

    public Jms2TemporaryQueueEndpoint(String uri, JmsComponent component, String destination, JmsConfiguration configuration) {
        super(uri, component, destination, configuration);
        setDestinationType("temp-queue");
    }

    public Jms2TemporaryQueueEndpoint(String uri, JmsComponent component, String destination, JmsConfiguration configuration, QueueBrowseStrategy queueBrowseStrategy) {
        super(uri, component, destination, configuration, queueBrowseStrategy);
        setDestinationType("temp-queue");
    }

    public Jms2TemporaryQueueEndpoint(String endpointUri, String destination) {
        super(endpointUri, destination);
        setDestinationType("temp-queue");
    }

    public Jms2TemporaryQueueEndpoint(TemporaryQueue jmsDestination) throws JMSException {
        super("jms:temp-queue:" + jmsDestination.getQueueName(), null);
        setDestinationType("temp-queue");
        this.jmsDestination = jmsDestination;
        setDestination(jmsDestination);
    }

    /**
     * This endpoint is a singleton so that the temporary destination instances are shared across all
     * producers and consumers of the same endpoint URI
     *
     * @return true
     */
    public boolean isSingleton() {
        return true;
    }

    public synchronized Destination getJmsDestination(Session session) throws JMSException {
        if (jmsDestination == null) {
            jmsDestination = createJmsDestination(session);
        }
        return jmsDestination;
    }

    protected Destination createJmsDestination(Session session) throws JMSException {
        return session.createTemporaryQueue();
    }
}
