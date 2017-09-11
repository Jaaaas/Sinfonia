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
We will divide the functionality for the two main entities described above.

#### ConnectionCore
ConnectionCore allows you to establish the first connection to the database. It is possible to establish a connection to the database through a json file where we will find the configuration parameters.

```java
ConnectionCore cc = new ConnectionCore().openConnectionToDB("AbsolutePath/To/ConfigDatabase.json");
```
A possible json file might be:
```json
{
    "Driver": "com.mysql.cj.jdbc.Driver",
    "Ip": "jdbc:mysql://127.0.0.1",
    "Port": "3306",
    "Username": "username",
    "Password": "password",
    "Database": "Sinfonia",
    "Timezone": "Europe/Rome"
}
```

ConnectionCore will read these parameters and establish the connection with the specified database. All json file parameters are mandatory except for the database name. *Remember that the project must have the specified driver installed.*

When you will retrieve the connection, you can set the auto commit by passing a boolean.

```java
public Connection fetchConnection(boolean commitMode)
{
    c.setAutoCommit(commitMode);
    return getConnection();
}
```

Other functions are:
```
public Connection getConnection(){}

public void closeConnection(){}

public void doCommit(){}

public void doRollback(){}
```



