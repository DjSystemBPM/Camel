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

import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.component.jms.JmsEndpoint;
import org.apache.camel.component.jms.QueueBrowseStrategy;

public class Jms2Component extends JmsComponent {

    public Jms2Component() {
        super(Jms2Endpoint.class);
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

}
