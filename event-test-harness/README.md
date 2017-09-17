event-client
=======================

Built with Java 8+, Spring (4.3.7), and ActiveMQ (5.14.0)

> Test harness for Event-driven Microservices Demo - Not for Production

### Java Examples

Assumes ActiveMQ is running on localhost

    java -Dactivemq.hostname=localhost -jar event-client-1.0.2.jar Roy
    
    java -Dactivemq.hostname=localhost -jar event-client-1.0.2.jar Leon
    
    java -Dactivemq.hostname=localhost -jar event-client-1.0.2.jar Eldon
    
    
### Docker Examples

Assumes ActiveMQ is running within _messaging-network_ on host _event-broker_

    docker run -e PATIENT=‘Roy’ --net=messaging-network event-client:latest

    docker run -e PATIENT='Leon' --net=messaging-network event-client:latest

    docker run -e PATIENT=‘Eldon' --net=messaging-network event-client:latest
    
