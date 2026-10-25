# Library Catalog

## Project Structure

```
library-catalog/
├── pom.xml
└── src
    ├── main
    │   └── java
    │       └── com
    │           └── example
    │               └── library
    │                   ├── App.java
    │                   ├── domain
    │                   │   └── Book.java
    │                   ├── repo
    │                   │   ├── BookRepository.java
    │                   │   └── InMemoryBookRepository.java
    │                   ├── service
    │                   │   └── CatalogService.java
    │                   └── util
    │                       └── IsbnValidator.java
    └── test
        └── java
            └── com
                └── example
                    └── library
                        ├── util
                        │   └── IsbnValidatorTest.java
                        └── service
                            └── CatalogServiceTest.java

```

## Build and Run

All commands below are run from inside this project directory (`cd library-catalog`).

## Compile the project
`mvn compile`

## Run tests
`mvn test`

## Run the app
`mvn exec:java`

(The main class is configured in `pom.xml` by the `exec-maven-plugin`, so no
`-Dexec.mainClass=...` flag is needed here — compare with `multi-file-maven`.)

## Package into a JAR
`mvn package`

## Run the JAR
`java -cp target/library-catalog-1.0-SNAPSHOT.jar com.example.library.App`

