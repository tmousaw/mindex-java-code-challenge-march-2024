# Solution to the Coding Challenge
## Bugs Found
As I was implementing the solution, I noticed and fixed the following bugs:
1. The `EmployeeController` class logging said "Received employee create..." for `create()`, `read()`, and `update()`.
1. The `EmployeeServiceImpl` class logging said "Creating employee..." for both `create()` and `read()`.
1. The `Employee` class did not declare an ID.
Therefore, any update to a particular employee was actually creating another entry in MongoDB which caused any query of that employee thereafter to fail with a `org.springframework.dao.IncorrectResultSizeDataAccessException`.
1. There was a disconnect between the `employee_database.json` file used for bootstrapping, the definition of the `Employee` class, and the `README.md` file.
The `employee_database.json` had a list of objects that can be represented by the following schema:
    ```json
    { 
      "type": "ReportEmployeeID",
      "properties": {
        "employeeId": {
          "type": "string"
        }
      }
    }
    ```
   The `Employee` class defined `directReports` as `List<Employee>`.
   The `README.md` file describes `directReports` as a list of strings.
   I chose to go with what is described in the `README.md` file because this makes most sense from a space perspective in the DB.
1. Updated the `springBootVersion` to `2.7.18`.
This could be considered an improvement, but I'm including it as a bug because version `2.2.0.RELEASE` has a known security vulnerabilities in `org.springframework.boot:spring-boot-starter-web`.
`org.springframework.boot:spring-boot-starter-web` still has dependencies with known security vulnerabilities in `2.7.18`, but not as many as the prior version.
Also, the dependencies for `org.springframework.boot:spring-boot-starter-data-mongodb` had known security vulnerabilities in version `2.2.0.RELEASE`, but not for version `2.7.18`.

## Improvements Made to the Code
1. Added `@RequestMapping(path = {"/employee"})` before class `EmployeeController` and removed the `/employee` part of the mappings thereafter.
1. The test file `EmployeeServiceImplTest` was misnamed.
   When you look at what it does, it is best described as an integration test with its entry point through the EmployeeController.
   Therefore, I moved the test to one named `EmployeeControllerIT` and changed the package from `com.mindex.challenge.service.impl` to `com.mindex.challenge.controller`.
1. I updated `EmployeeController`, `EmployeeServiceImpl`, and `DataBootstrap` to use constructor injection rather than using field injection.
This helps with testability, immutability, and has other benefits.
1. Added Lombok and rewrote `Employee` to use it. This reduces the amount of code that needs to be written.
1. I updated Gradle to use version 8.0.

## Dependencies Added
I added Lombok compile and annotation processor dependencies for both build and test in order to make it easier to create data classes.

## Additional Considerations
I thought about updating to use a later Java version as there were some features I would have liked to have used (e.g. Java 14's records).
I chose not to update Java since that is a task that is typically done across all microservices and wouldn't typically be done as part of adding a new feature.

## Additional Endpoints
The following endpoints were added:
```text
* Get Reporting Structure
    * HTTP Method: GET
    * URL: localhost:8080/employee/{id}/reporting
    * RESPONSE: ReportingStructure
* Create Compensation
    * HTTP Method: POST
    * URL: localhost:8080/compensation
    * RESPONSE: Compensation
* Get Compensation
    * HTTP Method: GET
    * URL: localhost:8080/compensation/{employeeId}
    * RESPONSE: Compensation
```