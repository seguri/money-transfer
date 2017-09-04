# Money Transfer

This demo is about a REST API for money transfer.

# Requirements

Main application:

- Java 8
- Maven 3+

Functional tests:

- Python 3+
- `requests` module

# Usage

Package the executable application with:

    mvn clean compile package

And execute:

    java -jar target/*jar-with-dependencies.jar

The server is now listening at `http://localhost:4567/api/v1`.

The application can be deployed on Heroku with:

    mvn heroku:deploy

And will be listening at `https://money-transfer-r3v0.herokuapp.com/api/v1`.

You can now test the endpoints described below with:

    python test.py

# Endpoints

| Endpoint         | Method | Payload          | Payload example                                                                                               | Return                                      |
|:-----------------|:-------|:-----------------|:--------------------------------------------------------------------------------------------------------------|:--------------------------------------------|
| /accounts        | POST   | email, balance   | {"email": "alice@example.com", "balance": 500}                                                                | `200 OK` + your payload completed with uuid |
| /accounts/:uuid  | GET    | uuid             |                                                                                                               | `200 OK` + the existing account             |
| /transfers       | POST   | from, to, amount | {"from": "e0f1eb51-7b0d-4544-9005-d2dc246b2cb7", "to": "95b6f2a1-bbe7-47a3-b5ac-0fc7fa37a657", "amount": 100} | `200 OK` + your payload completed with uuid |
| /transfers/:uuid | GET    | uuid             |                                                                                                               | `200 OK` + the existing transfer            |

# Architecture

This simple application consists of two parts.

The first is the Main class where it declares routes and controllers.

The second part is made by DAOs that interface with a persistent storage (in-memory in this case).
As storage management is very simple in this application, they also embed business logic and they act more like services than repositories.

# Intentionally missing

Following the requirement to keep things simple, this application produces a working API with a lot of real-world features missing:

- Authentication
- Real storage
- Transaction management
- Model input validation (email, negative balances or amounts, etc.)
- PUT, DELETE, etc.
- A deeper NPE safety
- Better separation between repositories and services
- Error documentation

