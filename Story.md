
#Get all customers based on loan amount range and laon status

To get all customers based on loan related properties. 
I would add a resource in `CustomerController` in `loan-application-service`.

As `loan-application-service` has loan and customer information stored. 

Api will look like 

```
GET /api/loanapplications/customers?loanMinAmount=10&loanMaxAmount=100&status=APPLIED

```






