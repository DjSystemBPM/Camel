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
package org.apache.camel.component.jms2.consumer;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms2.support.Jms2TestSupport;
import org.apache.camel.component.mock.MockEndpoint;
import org.junit.Test;

public class InOnlyTopicDurableConsumerTest extends Jms2TestSupport {

    @Override
    protected boolean useJmx() {
        return false;
    }

    @Test
    public void testDurableTopic() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedBodiesReceived("Hello World");

        MockEndpoint mock2 = getMockEndpoint("mock:result2");
        mock2.expectedBodiesReceived("Hello World");

        // wait a bit and send the message
        Thread.sleep(1000);

        template.sendBody("jms2:topic:foo", "Hello World");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jms2:topic:foo?clientId=123&durableSubscriptionName=one")
                    .to("log:test.log.1?showBody=true")
                    .to("mock:result");

                from("jms2:topic:foo?clientId=456&durableSubscriptionName=two")
                    .to("log:test.log.2?showBody=true")
                    .to("mock:result2");
            }
        };
    }
}
