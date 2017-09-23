Event-Driven Microservices Demo
=======================

### Docker Execution

1) Start-up Docker via Compose

	docker-compose up

2) Open Hawtio send messages to _event.ingestion_ (admin/admin)

	http://localhost:8090/hawtio/welcome

Connect Remote:

* Name: broker
* Scheme: http
* Host: event-broker
* Port: 8161
* Path: api/jolokia

Open Queue-->event.injestion

Open Send Tab

	
OR

2) Open ActiveMQ Queues (admin/admin)

	http://localhost:8161/admin

Click Send Tab

* Destination: event-ingestion

3) Send messages

Leon Kowalksi

	{
	  "resourceType": "Encounter",
	  "id": "EMR56789",
	  "text": {
	    "status": "generated",
	    "div": "Nose bleading and headaches"
	  },
	  "status": "in-progress",
	  "class": "outpatient",
	  "patient": {
	    "reference": "Patient/P45678",
	    "display": "Leon Kowalski"
	  }
	}

Roy Batty

	{
	  "resourceType": "Encounter",
	  "id": "EMR56788",
	  "text": {
	    "status": "generated",
	    "div": "Patient admitted with chest pains"
	  },
	  "status": "in-progress",
	  "class": "inpatient",
	  "patient": {
	    "reference": "Patient/P12345",
	    "display": "Roy Batty"
	  }
	}

Eldon Tyrell


	{
	  "resourceType": "Encounter",
	  "id": "EMR56799",
	  "text": {
	    "status": "generated",
	    "div": "Patient admitted with massive headache"
	  },
	  "status": "in-progress",
	  "class": "outpatient",
	  "patient": {
	    "reference": "Patient/99999",
	    "display": "Eldon Tyrell"
	  }
	}



### Spring-Boot Execution

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