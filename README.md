## URL DATABASE
### Setup Instruction
#### Requirements:
- Java 1.8+
- Maven
#### Building:
```shell script
mvn clean install
```
#### Building jar:
```shell script
mvn clean package
```
Then jar should be in the target folder
#### Running the app locally
```shell script
mvn spring-boot:run
```
#### Running the app in the IDE
You should be able to import the project into your IDE with no problems. Once there you can run the StartApplication from its main method and debug it.
#### H2 Database Console
Once the application is up and running, you could open 
> http://localhost:8080/h2-console

### Design
#### Technologies and frameworks:
- Java 8
> Development language
- Spring Boot
> Development framework
- Lombok
> Code generation library
- Log4j
> Logging library
- H2 Database
> Embedded database
- Spock
> Test framework
#### Further Plan
The time is limited, so I only include unit test;
But I also listed my further plan here:
- Refactor CommandRunner into consumable service and command line interface / RESTful APIs.
- Adding integration tests and acceptance tests.
- Change embedded database to PostgreSQL or MySQL.
- Containerized service and database.
- Jenkins based CI/CD pipeline.
- User k8s create pods that contain service and database.
- Use agent to inject logs from service.
- Introducing monitoring, such as Prometheus.
- Even dynamic infrastructure on SaaS via infrastructure-as-code.