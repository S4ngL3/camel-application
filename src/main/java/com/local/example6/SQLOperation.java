package com.local.example6;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.SimpleRegistry;

public class SQLOperation {
    public static void main(String[] args) throws Exception {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3306/TestDB");
        dataSource.setUser("root");
        dataSource.setPassword("Password@123");

        SimpleRegistry simpleRegistry = new SimpleRegistry();
        simpleRegistry.bind("myDataSource", dataSource);

        CamelContext context = new DefaultCamelContext(simpleRegistry);
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .to("jdbc:myDataSource")
                        .bean(new ResultHandler(), "printResult");
            }
        });
        context.start();

        ProducerTemplate producerTemplate = context.createProducerTemplate();
        producerTemplate.sendBody("direct:start","SELECT * FROM Inventory");
    }
}
