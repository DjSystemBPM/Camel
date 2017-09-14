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

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.component.jms.JmsEndpoint;
import org.apache.camel.component.jms.QueueBrowseStrategy;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriParam;

public class Jms2Component extends JmsComponent {

    @Metadata(label = "consumer")
    private boolean subscriptionDurable;

    @Metadata(label = "consumer")
    private boolean subscriptionShared;

    public Jms2Component() {
        super(Jms2Endpoint.class);
    }

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        // set as parameters if not explicit configured on endpoint level and the option was enabled on component level
        if (subscriptionDurable) {
            parameters.putIfAbsent("subscriptionDurable", "true");
        }
        if (subscriptionShared) {
            parameters.putIfAbsent("subscriptionShared", "true");
        }

        return super.createEndpoint(uri, remaining, parameters);
    }

    @Override
    protected JmsEndpoint createTemporaryTopicEndpoint(String uri, JmsComponent component, String subject, JmsConfiguration configuration) {
        return new Jms2TemporaryTopicEndpoint(uri, component, subject, configuration);
    }

    @Override
    protected JmsEndpoint createTopicEndpoint(String uri, JmsComponent component, String subject, JmsConfiguration configuration) {
        return new Jms2Endpoint(uri, component, subject, true, configuration);
    }

    @Override
    protected JmsEndpoint createTemporaryQueueEndpoint(String uri, JmsComponent component, String subject, JmsConfiguration configuration, QueueBrowseStrategy queueBrowseStrategy) {
        return new Jms2TemporaryQueueEndpoint(uri, component, subject, configuration, queueBrowseStrategy);
    }

    @Override
    protected JmsEndpoint createQueueEndpoint(String uri, JmsComponent component, String subject, JmsConfiguration configuration, QueueBrowseStrategy queueBrowseStrategy) {
        return new Jms2QueueEndpoint(uri, component, subject, configuration, queueBrowseStrategy);
    }

    public boolean isSubscriptionDurable() {
        return subscriptionDurable;
    }

    /**
     * Set whether to make the subscription durable. The durable subscription name
     * to be used can be specified through the "subscriptionName" property.
     * <p>Default is "false". Set this to "true" to register a durable subscription,
     * typically in combination with a "subscriptionName" value (unless
     * your message listener class name is good enough as subscription name).
     * <p>Only makes sense when listening to a topic (pub-sub domain),
     * therefore this method switches the "pubSubDomain" flag as well.
     */
    public void setSubscriptionDurable(boolean subscriptionDurable) {
        this.subscriptionDurable = subscriptionDurable;
    }

    public boolean isSubscriptionShared() {
        return subscriptionShared;
    }

    /**
     * Set whether to make the subscription shared. The shared subscription name
     * to be used can be specified through the "subscriptionName" property.
     * <p>Default is "false". Set this to "true" to register a shared subscription,
     * typically in combination with a "subscriptionName" value (unless
     * your message listener class name is good enough as subscription name).
     * Note that shared subscriptions may also be durable, so this flag can
     * (and often will) be combined with "subscriptionDurable" as well.
     * <p>Only makes sense when listening to a topic (pub-sub domain),
     * therefore this method switches the "pubSubDomain" flag as well.
     * <p><b>Requires a JMS 2.0 compatible message broker.</b>
     */
    public void setSubscriptionShared(boolean subscriptionShared) {
        this.subscriptionShared = subscriptionShared;
    }
}
