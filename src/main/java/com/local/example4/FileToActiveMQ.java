package com.local.example4;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.language.bean.Bean;

public class FileToActiveMQ {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        context.addComponent("jms", jmsComponent());
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("file:input_box?noop=true")
                        .to("jms:queue:my_queue");
//                from("timer:mytimer?period=5000")
//                        .setBody(constant("HELLO from Camel!"))
//                        .to("jms:queue:HELLO.WORLD");
            }
        });

        while(true)
            context.start();
    }
    
    public static JmsComponent jmsComponent() throws JMSException {
        // Create the connectionfactory which will be used to connect to Artemis
        ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
        cf.setBrokerURL("tcp://localhost:61616");
        cf.setUserName("admin");
        cf.setPassword("admin");

        // Create the Camel JMS component and wire it to our Artemis connectionfactory
        JmsComponent jms = new JmsComponent();
        jms.setConnectionFactory(cf);
        return jms;
    }
}
