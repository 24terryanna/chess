# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly&shrinkToFit=true#initialData=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-ppCp5cuoeTNMv1obELho-WJKNklrmqgELwLSuxelgMAbAMCSIEESEg4mj9yl7wqD1Vi-VBrGFFTyoJmjuB4FhNiahrlNnNxABauEGy5JorebQx1VqUWWOOfdA1+sABUoibheqOXdfVfUA2iI+uy+WvqI0wEDZCrxBKlG7QVCKl5gTgTBP5TvVNFSc0ZsZmKmJz8nCSswEAA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
