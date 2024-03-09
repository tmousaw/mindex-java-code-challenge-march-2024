# Solution to the Coding Challenge
## Bugs Found
As I was implementing the solution, I noticed and fixed the following bugs:
1. The `EmployeController` class logging said "Received employee create..." for `create()`, `read()`, and `update()`.
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
1. The test file `EmployeeServiceImplTest` was misnamed.
When you look at what it does, it is best described as an integration test which it's entry point through the EmployeeController.
Therefore, I moved the test to one named `EmployeeControllerIT` and changed the package from `com.mindex.challenge.service.impl` to `com.mindex.challenge.controller`.

## Dependencies Added
I added Lombok compile and annotation processor dependencies for both build and test in order to make it easier to create data classes.
I noticed that the gradle file uses Java 8.
Had it been at least Java 14, I would have used records for my data classes.

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