Event-Driven Microservices Demo
=======================

Requires ActiveMQ listening on tcp://localhost:61616

1) Build all the sub-projects - mvn clean install

- event-common
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

* java -Dactivemq.hostname=localhost -jar event-client-1.0.2.jar Leon

* java -Dactivemq.hostname=localhost -jar event-client-1.0.2.jar Roy

* java -Dactivemq.hostname=localhost -jar event-client-1.0.2.jar Eldon

or start JMeter and run test suite

..event-ingestion/src/test/jmeter/Camel Mock Publisher


4) View Results in ActiveMQ Console - http://localhost:8161/admin

default uid/pw = admin/admin


5) View Audit File Results

cd event-auditing/target/events