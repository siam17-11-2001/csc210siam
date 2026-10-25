# Multi-File Maven Project

## Project Structure

```
multi-file-maven/
├── pom.xml
└── src/
    └── main/
        └── java/
            └── com/
                └── example/
                    ├── App.java
                    └── util/
                        ├── MathUtils.java
                        └── StringUtils.java
```

All commands below are run from inside this project directory (`cd multi-file-maven`).

## Compile the project
`mvn compile`

## Run the main program
`mvn exec:java -Dexec.mainClass="com.example.App"`

## Or package into a JAR
`mvn package`

## Run the JAR
`java -cp target/multi-file-maven-1.0-SNAPSHOT.jar com.example.App`
