# Homework
Homework 2.1
## Built With
* Java
* Spring Boot - Framework
* Maven - Build tool
* h2 - In-memory database
* RabbitMQ - Message Broker

### Other libraries used
* Spring AMQP - For integrating rabbitmq 
* Junit5 - For testing
* Mockito - For mocking in tests
* Javax validator- For request validation
* Lombok - For logging, auto code generation

## Running locally prerequisite
### Rabbit MQ

Before you can build your application, 
you need to set up a server to handle receiving and sending messages.

You can download and run in a terminal 

```$xslt
brew install rabbitmq
rabbitmq-server
```

or 

If you have docker running locally, There is a docker-compose.yml in the root of the complete project. Run following 
command to get RabbitMQ in container.
```$xslt
docker-compose up
```
## Content
Here is the list of services and associated ports: 
```
eureka
    Default Port: 8761
    To start use: mvn spring-boot:run
gateway
    Default Port: 9080
    To start use: mvn spring-boot:run
auth
    Default Port: 9180
    To start use: mvn spring-boot:run
customer-service
    Default Port: 8018
    To start use: mvn spring-boot:run
loan-application service
    Default Port: 8019
    To start use: mvn spring-boot:run
```
## Follow steps to build and run the project using command line
 
```bash

mvn -f eureka/pom.xml clean install 

java -jar eureka/target/eureka-1.0.0-SNAPSHOT.jar&

mvn -f auth/pom.xml clean install 

java -jar auth/target/auth-1.0.0-SNAPSHOT.jar

mvn -f gateway/pom.xml clean install 

java -jar gateway/target/gateway-1.0.0-SNAPSHOT.jar

mvn -f customer-service/pom.xml clean install 

java -jar customer-service/target/customer-service-0.0.1-SNAPSHOT.jar

mvn -f loanapplicationservice/pom.xml clean install 

java -jar loanapplicationservice/target/loanapplicationservice-0.0.1-SNAPSHOT.jar
```

## Services offer following endpoints
This api doesn't have a Swagger/OpenAPI integration yet.
Thus the available endpoints information has been added here.

### User Endpoints
#### POST localhost:9180/users 
To register a new user 
```console
curl -H "Content-type: application/json" -X POST  -d '{"username": "test","password": "test"}' http://localhost:9180/users
```
You can get Access token after this stage.  
```console
curl -X POST http://localhost:9180/oauth/token -H 'authorization: Basic Y2xpZW50OnNlY3JldA==' -d 'username=test&password=test&grant_type=password'
```
You need to use the `access_token` received from above curl in following secured requests.

#### GET localhost:9180/api/users 
Please add auth token received
```console
curl -X GET http://localhost:9180/api/users/1 -H 'authorization: Bearer <AUTH TOKEN HERE>' 
```

### Customer Endpoints
We will route customer endpoints through gateway. 

#### POST localhost:9080/api/customers 
To register a new user. 
Please userid obtained from above user creation in this api.
Please use access token.
```console
curl -X POST http://localhost:9080/api/customers -H 'authorization: Bearer <AUTH TOKEN HERE>'  -H 'content-type: application/json' -d '{"userId": "<USER ID HERE>","firstName": "test","lastName": "test","phone": "test132","email": "test@gmail.com"}'
```

#### GET localhost:9080/api/customers/
To get a new customer. 
Please customerid obtained from above customer creation in this api.
Please use access token.
```console
curl -X GET http://localhost:9080/api/customers/1 -H 'authorization: Bearer <AUTH TOKEN HERE>' 
```

### Loan Endpoints
We will route customer endpoints through gateway. 

#### POST localhost:9080/api/loanapplications 
To register a new user. 
Please customerid obtained from above user creation in this api.
Please use access token.
```console
curl -X POST http://localhost:9080/api/loanapplications -H 'authorization: Bearer <AUTH TOKEN HERE>' -H 'content-type: application/json' -d '{"customerId" :1,"amount": 3300.20,"duration": 1,"status": "CREATED"}'
```

#### GET localhost:9080/api/loanapplications?customer
To get all associated loans per customer
Please customerid obtained from above customer creation in this api.
Please use access token.
```console
curl -X GET 'http://localhost:8019/api/loanapplications?customerId=1' -H 'authorization: Bearer<AUTH TOKEN HERE> ' 
```

## Service Metrics
Basic spring boot actuators configured. Can be extended. 
### customer-service 

http://localhost:8018/actuator

### loan-application-service 

http://localhost:8019/actuator