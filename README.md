# Backend challenge

Welcome to Bankin's Java backend challenge.

You will be adding to the existing code of this project. Setting up the project requires Java 8 and IntelliJ CE (https://www.jetbrains.com/idea/download) with the Scala plugin installed.

## Running the project

The project builds and runs in its initial state. You can start it by either typing `./sbt run` in the console or by creating an SBT run configuration in IntelliJ with the `run` task.

Once started, calling the health check endpoint `curl localhost:9000/ping` should return `HTTP 200`.

The command `./sbt test` will run the unit tests.

## The challenge

Upon completing the challenge, the endpoint `/mycontroller/myroute` is supposed to return the sum of a user's checking and saving accounts, rounded to the upper hundred.

The user's credentials as well as its authentication call are already coded in the `BridgeClient` service. 

Your task consists in implementing the `List account` (https://docs.bridgeapi.io/v2018.06.15/reference#list-accounts) endpoint as well as the appropriate business logic.

A few unit tests and renaming would be welcome.