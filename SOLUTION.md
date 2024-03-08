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
      "type":"ReportID",
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