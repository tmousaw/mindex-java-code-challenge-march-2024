# Solution to the Coding Challenge
## Bugs Found
As I was implementing the solution, I noticed and fixed the following bugs:
1. The `Employee` class did not declare an ID.
Therefore, any update to a particular employee was actually creating another entry in MongoDB which caused any query of that employee thereafter to fail with  