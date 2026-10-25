# Calculator Client

A main program that reuses the `Calculator` library from the `doc-demo`
project. The reuse happens entirely through Maven: look at the
`<dependencies>` block in `pom.xml` — it names `doc-demo` by its
`groupId`/`artifactId`/`version` coordinates, and `App.java` simply imports
`com.example.docs.Calculator` as if it were any other library.

## Project Structure

```
calculator-client/
├── pom.xml
└── src/
    └── main/
        └── java/
            └── com/
                └── example/
                    └── client/
                        └── App.java
```

## Build and Run

**First**, install the library this project depends on. In the `doc-demo`
directory, run:

`mvn install`

That places the `doc-demo` artifact in your local Maven repository
(`~/.m2/repository`), where Maven resolves dependencies from. Without this
step, compiling this project fails with an unresolvable-dependency error.

Then, from this directory:

## Compile the project
`mvn compile`

## Run the app
`mvn exec:java`

(The main class is configured in `pom.xml` by the `exec-maven-plugin`.)
