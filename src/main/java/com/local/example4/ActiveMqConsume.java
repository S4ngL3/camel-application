package com.local.example4;

import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ConsumerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

public class ActiveMqConsume {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        ConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("admin","admin","tcp://localhost:61616");

        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(activeMQConnectionFactory));
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jms:queue:my_queue")
                        .to("seda:end");
            }
        });

        context.start();

        ConsumerTemplate consumerTemplate = context.createConsumerTemplate();
        String message = consumerTemplate.receiveBody("seda:end", String.class);

        System.out.println(message);
    }
}
