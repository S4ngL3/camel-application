package com.local.example4;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.Date;

import static org.apache.camel.model.dataformat.JsonLibrary.Gson;

public class ObjectToActiveMQ {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        ConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("admin","admin","tcp://localhost:61616");

        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(activeMQConnectionFactory));
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .to("jms:queue:my_queue");
            }
        });
        context.start();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(new  Person(1, "Sang"));

        ProducerTemplate producerTemplate = context.createProducerTemplate();
        producerTemplate.sendBody("direct:start", json);
    }
}

