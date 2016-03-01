event-care-router
=======================

Built with Java 8+, Apache Camel (2.16.2) and Spring Boot (1.3.3.RELEASE)

Tested with JUnit (4.11)

Executes with Spring Boot

`mvn spring-boot:run -Drun.arguments="-Xmx256m,-Xms128m"`

Runs against ActiveMQ (5.13.1) using JMeter (2.13); requires activemq-all-5.13.1.jar in lib directory

>Message content can viewed in the ActiveMQ or hawtio Web Consoles.

Start ActiveMQ and navigate to http://localhost:8161/admin/

Or launch hawtio

    $java -jar hawtio-app-1.4.61.jar --port 8090 

and navigate to http://localhost:8090/hawtio/welcome 
