/*
 * Copyright 2005-2014 Red Hat, Inc.
 * Red Hat licenses this file to you under the Apache License, version
 * 2.0 (the "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 */
package org.hornetq.tests.integration.openwire.amq;

import java.util.Arrays;
import java.util.Collection;

import javax.jms.DeliveryMode;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.command.ActiveMQDestination;
import org.hornetq.tests.integration.openwire.BasicOpenWireTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * adapted from: org.apache.activemq.JMSConsumerTest
 *
 * @author <a href="mailto:hgao@redhat.com">Howard Gao</a>
 *
 */
@RunWith(Parameterized.class)
public class JMSConsumer4Test extends BasicOpenWireTest
{
   @Parameterized.Parameters(name = "deliveryMode={0} destinationType={1}")
   public static Collection<Object[]> getParams()
   {
      return Arrays.asList(new Object[][] {
         {DeliveryMode.NON_PERSISTENT, ActiveMQDestination.TOPIC_TYPE},
         {DeliveryMode.PERSISTENT, ActiveMQDestination.TOPIC_TYPE}
      });
   }

   public int deliveryMode;
   public byte destinationType;

   public JMSConsumer4Test(int deliveryMode, byte destinationType)
   {
      this.deliveryMode = deliveryMode;
      this.destinationType = destinationType;
   }

   @Test
   public void testDurableConsumerSelectorChange() throws Exception
   {
      // Receive a message with the JMS API
      connection.setClientID("test");
      connection.start();
      Session session = connection.createSession(false,
            Session.AUTO_ACKNOWLEDGE);
      ActiveMQDestination destination = createDestination(session,
            destinationType);
      MessageProducer producer = session.createProducer(destination);
      producer.setDeliveryMode(deliveryMode);
      MessageConsumer consumer = session.createDurableSubscriber(
            (Topic) destination, "test", "color='red'", false);

      // Send the messages
      TextMessage message = session.createTextMessage("1st");
      message.setStringProperty("color", "red");
      producer.send(message);

      Message m = consumer.receive(1000);
      assertNotNull(m);
      assertEquals("1st", ((TextMessage) m).getText());

      // Change the subscription.
      consumer.close();
      consumer = session.createDurableSubscriber((Topic) destination, "test",
            "color='blue'", false);

      message = session.createTextMessage("2nd");
      message.setStringProperty("color", "red");
      producer.send(message);
      message = session.createTextMessage("3rd");
      message.setStringProperty("color", "blue");
      producer.send(message);

      // Selector should skip the 2nd message.
      m = consumer.receive(1000);
      assertNotNull(m);
      assertEquals("3rd", ((TextMessage) m).getText());

      assertNull(consumer.receiveNoWait());
   }

}
