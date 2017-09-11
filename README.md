# Sinfonia

> **Java library that helps the query execution process using a chain construct and Java 8. Perform easily queries and updates to any databases.**

## Overview

In quite complicated projects, we often find ourselves having to find a good management system for communication with databases.
Very often, ORMs make this process difficult. Some of these require the user to follow strict rules that often can not be configured.

Sinfonia has been designed with a simpler design. It does not offer complex configuration mechanisms but an easy way to execute and map queries. It uses a chain constructor that is, from the first time, user friendly and easy to learn.

Sinfonia uses two main entities:

* **ConnectionCore**: manages the database connection and all the operations associated with it(commit,rollback and so on).

* **QueryCore**: is used to run and map the query using the chain construct.

## Getting Started

### Prerequisites
In order to use Sinfonia you simply need to import the **JAR** into the project and you can use its features.

### Features
We will divide the functionality for the two main entities described above

#### ConnectionCore
