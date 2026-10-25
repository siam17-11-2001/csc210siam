# Documentation Demo (doc-demo)

A library project (no main program) demonstrating Maven-generated
documentation: Javadoc, test reports, coverage, and a full project site.

## Project Structure

```
doc-demo/
├── pom.xml
├── src
│   ├── main
│   │   └── java
│   │       └── com
│   │           └── example
│   │               └── docs
│   │                   ├── Calculator.java
│   │                   └── package-info.java
│   ├── test
│   │   └── java
│   │       └── com
│   │           └── example
│   │               └── docs
│   │                   └── CalculatorTest.java
│   └── site
│       ├── site.xml
│       └── markdown
│           ├── index.md
│           └── usage.md

```

All commands are run from the project root (this directory).

## Compile the project
`mvn compile`

## Compile, run the tests, and produce coverage
`mvn clean verify`

## Generate just the API docs
`mvn javadoc:javadoc`

## Build the full site (Javadoc, reports, and your markdown pages)
`mvn site`

## Preview the site locally
`mvn site:run` — then open http://localhost:8080

## (Optional) Deploy the site somewhere (requires configuration)
`mvn site-deploy`

## Install into your local Maven repository
`mvn install`

This makes the library available to other Maven projects on your machine.
The `calculator-client` project depends on this artifact, so run this
command before building `calculator-client`.

