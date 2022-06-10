# pheonix-bank
pheonix simple bank app load in, transfer to another customer using react and springboot 

Assumptions:
- no session management is handled for login user
- no concurrency is handled
- request validations are not done assuming values are not null or empty
- no transaction management
- incoming currency amount is to be positive 
- debts are handled only for one user
- no UI/UX is considered for front end
- login, payment, logout only these are supported with basic features
- login checks before every payment is not handled

Not Completed:
- unit test cases for react js front end is not able to complete



TestCases:

> login Alice
Hello, Alice!
Your balance is 0.

> topup 100
Your balance is 100.

> login Bob
Hello, Bob!
Your balance is 0.

> topup 80
Your balance is 80.

> pay Alice 50
Transferred 50 to Alice.
Your balance is 30.

> pay Alice 100
Transferred 30 to Alice.
Your balance is 0.
Owing 70 to Alice.

> topup 30
Transferred 30 to Alice.
Your balance is 0.
Owing 40 to Alice.

> login Alice
Hello, Alice!
Owing 40 from Bob.
Your balance is 210.

> pay Bob 30
Owing 10 from Bob.
Your balance is 210.

> login Bob
Hello, Bob!
Your balance is 0.
Owing 10 to Alice.

> topup 100
Transferred 10 to Alice.
Your balance is 90.

How to Run
----------
for front end app :
- you have to use npm to run this, please install npm in your machine
- please run 
> npm run build
> 
> npm start
- use the browser to navigate the services, the port is listening on 3000

for back end app :
- you have to use java to run this, please install java 1.11 or above
- i have configured cors whitelist url in the application.properties look out for "pheonix.allowedOrigins", make sure you use right ip and port. if you run both frontend, backend in localhost, no need of any changes
- mvn clean install - to cover packaging with unit tests
- you can run the app using, the port is listening on 11022
 > java - jar target\phoenixbankapp-1.0.jar  (or)
 > 
 > mvn spring-boot:run command


