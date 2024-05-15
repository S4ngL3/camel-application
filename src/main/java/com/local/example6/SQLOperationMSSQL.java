package com.local.example6;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.SimpleRegistry;

public class SQLOperationMSSQL {
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

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                        .to("jdbc:myDataSource")
                        .bean(new ResultHandler(),"printResult");
            }
        });
        context.start();

        ProducerTemplate producerTemplate = context.createProducerTemplate();
        producerTemplate.sendBody("direct:start", "SELECT * FROM Inventory");
    }
}
