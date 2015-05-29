Event-Driven Microservices Demo
=======================

Requires ActiveMQ listening on tcp://localhost:61616

1) Build all the sub-projects - mvn clean install

- event-test-harness
- event-ingestion
- event-auditing
- event-care-management
- event-cep

2) Start all Microservices - mvn spring-boot:run -Drun.arguments="-Xmx256m,-Xms128m" 

- event-ingestion
- event-auditing
- event-care-management
- event-cep

3) Run Test Harness 

cd event-test-harness/target

- java -jar event-client-1.0.0.jar Leon
- java -jar event-client-1.0.0.jar Roy
- java -jar event-client-1.0.0.jar Eldon


4) View Results in ActiveMQ Console - http://localhost:8161/admin

default uid/pw = admin/admin


5) View Audit File Results

cd event-auditing/target/events