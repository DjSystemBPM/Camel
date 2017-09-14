/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
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
import javax.jms.TemporaryTopic;

import org.apache.camel.component.jms.DestinationEndpoint;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;

public class Jms2TemporaryTopicEndpoint extends Jms2Endpoint implements DestinationEndpoint {

    private Destination jmsDestination;

    public Jms2TemporaryTopicEndpoint(String uri, JmsComponent component, String destination, JmsConfiguration configuration) {
        super(uri, component, destination, true, configuration);
    }

    public Jms2TemporaryTopicEndpoint(String endpointUri, String destination) {
        super(endpointUri, destination);
        setDestinationType("temp-topic");
    }

    public Jms2TemporaryTopicEndpoint(TemporaryTopic jmsDestination) throws JMSException {
        super("jms2:temp-topic:" + jmsDestination.getTopicName(), null);
        this.jmsDestination = jmsDestination;
        setDestinationType("temp-topic");
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
        return session.createTemporaryTopic();
    }
}
