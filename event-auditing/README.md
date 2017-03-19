event-audit-router
=======================

Built with Java 8+, Apache Camel (2.18.3) and Spring Boot (1.5.2.RELEASE)

Tested with JUnit (4.11)

Executes with Spring Boot

`mvn spring-boot:run -Drun.arguments="-Xmx256m,-Xms128m"`

Runs against ActiveMQ (5.14.0) using JMeter (2.13); requires activemq-all-5.14.0.jar in lib directory

>Message content can viewed in the ActiveMQ or hawtio Web Consoles.

Start ActiveMQ and navigate to http://localhost:8161/admin/

Or launch hawtio

    $java -jar hawtio-app-1.4.65.jar --port 8090 

and navigate to http://localhost:8090/hawtio/welcome