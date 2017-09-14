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

import javax.jms.JMSException;
import javax.jms.Topic;

import org.apache.camel.api.management.ManagedAttribute;
import org.apache.camel.component.jms.JmsBinding;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.component.jms.JmsConsumer;
import org.apache.camel.component.jms.JmsEndpoint;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.springframework.jms.listener.AbstractMessageListenerContainer;

/**
 * The jms2 component allows messages to be sent to (or consumed from) a JMS Queue or Topic (uses JMS 2.x API).
 */
@UriEndpoint(firstVersion = "2.20.0", scheme = "jms2", extendsScheme = "jms", title = "JMS2",
        syntax = "jms2:destinationType:destinationName", consumerClass = JmsConsumer.class, label = "messaging")
public class Jms2Endpoint extends JmsEndpoint  {

    @UriParam(label = "consumer")
    private String subscriptionName;

    @UriParam(label = "consumer")
    private boolean subscriptionDurable;

    @UriParam(label = "consumer")
    private boolean subscriptionShared;

    public Jms2Endpoint() {
    }

    public Jms2Endpoint(Topic destination) throws JMSException {
        super(destination);
    }

    public Jms2Endpoint(String uri, JmsComponent component, String destinationName, boolean pubSubDomain, JmsConfiguration configuration) {
        super(uri, component, destinationName, pubSubDomain, configuration);
    }

    public Jms2Endpoint(String endpointUri, JmsBinding binding, JmsConfiguration configuration, String destinationName, boolean pubSubDomain) {
        super(endpointUri, binding, configuration, destinationName, pubSubDomain);
    }

    public Jms2Endpoint(String endpointUri, String destinationName, boolean pubSubDomain) {
        super(endpointUri, destinationName, pubSubDomain);
    }

    public Jms2Endpoint(String endpointUri, String destinationName) {
        super(endpointUri, destinationName);
    }

    @Override
    public void configureListenerContainer(AbstractMessageListenerContainer listenerContainer, JmsConsumer consumer) {
        super.configureListenerContainer(listenerContainer, consumer);

        // now configure the JMS 2.0 API

        if (getDurableSubscriptionName() != null) {
            listenerContainer.setDurableSubscriptionName(getDurableSubscriptionName());
        } else if (isSubscriptionDurable()) {
            listenerContainer.setSubscriptionDurable(true);
            if (getSubscriptionName() != null) {
                listenerContainer.setSubscriptionName(getSubscriptionName());
            }
        }
        listenerContainer.setSubscriptionShared(isSubscriptionShared());
    }

    @ManagedAttribute
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

    @ManagedAttribute
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

    @ManagedAttribute
    public String getSubscriptionName() {
        return subscriptionName;
    }

    /**
     * Set the name of a subscription to create. To be applied in case
     * of a topic (pub-sub domain) with a shared or durable subscription.
     * <p>The subscription name needs to be unique within this client's
     * JMS client id. Default is the class name of the specified message listener.
     * <p>Note: Only 1 concurrent consumer (which is the default of this
     * message listener container) is allowed for each subscription,
     * except for a shared subscription (which requires JMS 2.0).
     */
    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

}
