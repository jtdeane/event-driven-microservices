event-ingestion-router
=======================

Built with Java 8+, Apache Camel (2.15.3) and Spring Boot (1.2.5.RELEASE)

Tested with JUnit (4.11)

Executes with Spring Boot

`mvn spring-boot:run -Drun.arguments="-Xmx256m,-Xms128m"`

Runs against ActiveMQ (5.12.0) using JMeter (2.13); requires activemq-all-5.12.0.jar in lib directory

>Note that viewing message content in ActiveMQ Web Console is incompatible with Java 8 but will be fixed in ActiveMQ 5.13 (upgrading Jetty to 9+). Until then use the hawtio (http://hawt.io) to view message content.  
