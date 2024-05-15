package com.local.example6;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.SimpleRegistry;

import java.util.ArrayList;
import java.util.Map;

public class SQLOperationMSSQLToActiveMQ {
    public static void main(String[] args) throws Exception {
        SQLServerDataSource dataSource = new SQLServerDataSource();
//        dataSource.setURL("jdbc:sqlserver://localhost:1433;DatabaseName=TestDB;encrypt=true;trustServerCertificate=true;");
        dataSource.setURL("jdbc:sqlserver://localhost");
        dataSource.setPortNumber(1433);
        dataSource.setDatabaseName("TestDB");
        dataSource.setUser("sa");
        dataSource.setPassword("Password@123");
        dataSource.setEncrypt("true");
        dataSource.setTrustServerCertificate(true);

        SimpleRegistry simpleRegistry = new SimpleRegistry();
        simpleRegistry.bind("myDataSource", dataSource);

        CamelContext context = new DefaultCamelContext(simpleRegistry);

        // Setup activeMQ
        ConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("admin","admin","tcp://localhost:61616");

        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(activeMQConnectionFactory));
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:select")
                        .to("jdbc:myDataSource")
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                // Transform data
                                ArrayList<Map<Integer, Map<String,Integer>>> dataList = (ArrayList<Map<Integer, Map<String,Integer>>>) exchange.getIn().getBody();
                                ObjectMapper mapper = new ObjectMapper();
                                for (Map<Integer, Map<String, Integer>> data : dataList){
                                    Inventory inventory = mapper.convertValue(data, Inventory.class);
                                    System.out.println(data.get("id") + "-" + data.get("name") + "-" + data.get("quantity"));
                                    System.out.println(inventory.getName());
                                }
                            }
                        })
                        .to("jms:queue:my_queue")
                        .bean(new ResultHandler(),"printResult")
                        .to("seda:end");
            }
        });
        context.start();

        ProducerTemplate producerTemplate = context.createProducerTemplate();
        producerTemplate.sendBody("direct:select", "SELECT * FROM Inventory");

        // Consume data
        ConsumerTemplate consumerTemplate = context.createConsumerTemplate();
        String message = consumerTemplate.receiveBody("seda:end", String.class);

        System.out.println(message);
    }
}
