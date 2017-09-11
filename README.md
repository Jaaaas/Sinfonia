# Sinfonia

> **Java library that helps the query execution process using Java 8. Perform easily queries and updates to any databases.**

## Overview

In quite complicated projects, we often find ourselves having to find a good management system for communication with databases.
Very often, ORMs make this process difficult. Some of these require the user to follow strict rules that often can not be configured.

Sinfonia has been designed with a simpler design. It does not offer complex configuration mechanisms but an easy way to execute and map queries. It uses a chain constructor that is, from the first time, user friendly and easy to learn.

Sinfonia uses two main entities:

* **ConnectionCore**: manages the database connection and all the operations associated with it(commit,rollback and so on).

* **QueryCore**: is used to run and map the query.

## Getting Started

### Prerequisites
In order to use Sinfonia you simply need to import the **JAR** into the project and you can use its features.

### Features
We will divide the functionality for the two main entities described above.

### ConnectionCore
ConnectionCore allows you to establish the first connection to the database. It is to do it through a json file where we will find the configuration parameters.

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

Other useful functions are:
```
public Connection getConnection(){}

public void closeConnection(){}

public void doCommit(){}

public void doRollback(){}
```
### QueryCore
After we connect to the database through ConnectionCore, QueryCore gives us the ability to run our queries. 
To create a QueryCore object we need the query as the first parameter and as the second parameter a list that will contain the values that will replace the **"?"** for the use of the prepared statement. 
If the prepared statement is not used in the query, you can just omit it.

```java
public QueryCore(String query,List<Object> l);
```

An example might be:
```java
QueryCore qc = new QueryCore("Select * From tableName");
```

or
```java
QueryCore qc = new QueryCore("SELECT * FROM persona WHERE field = ? ",Arrays.asList(args));
```
*Remember that the order of the prepared statement list is critical for the correct execution of the query*.

To avoid finding sql queries inside the constructor(or inside java files in general), we recommend using some functions that return the query directly. For this purpose, we recommend using [Cosmo](https://github.com/Jaaaas/Cosmo)(the documentation will be added soon).

After you instantiate the QueryCore object, you can use the chain constructor. We will take various examples explaining the different use cases

### Use cases

* **Select mapped to json**: if we have to make a simple select query and we need to map it to a json object
```java
JsonArray jsonArray = (JsonArray) queryCore.init(connectionCore.fetchConnection(false))
                                    .buildQuery()
                                    .executionQ()
                                    .mapping(new DatabaseUtility().rsToJson)
                                    .destroy()
                                    .getResponse();
```

The code above, will build the query, will run it, will map it to a json object, will release the resources and will take the result.

* **Select mapped to custom class**: if we have to make a simple select query and we need to map it to a custom class
```java
ArrayList<CustomObject> l = (ArrayList<CustomObject>) queryCore.init(connectionCore.fetchConnection(false))
                                                .buildQuery()
                                                .executionQ()
                                                .to(CustomObject.class)
                                                .mapping(new DatabaseUtility().rsToModel)
                                                .destroy()
                                                .getResponse();
```
*Remember that the customObject fields must have the same name as the columns selected in the query*.

* **Update query**: if we have to make a simple update query
```java
qc.init(cCore.getConnection())
            .buildQuery()
            .executionU()
            .destroy();
```

* **Update query retrieving key**: if we need to get what **key** was updated
```java
int keyUpdated = qc.init(cCore.getConnection())
            .buildQuery()
            .executionU()
            .destroy()
            .getKey();
```

* **Select query with validation and custom error**: if we need to validate the resultSet, we can use **Validators**. At the moment there are only two Validators(others will be added soon). 

1. notEmpty
2. notEmptyWithError

Validators can be used as follows

```java
ArrayList<CustomObject> l = (ArrayList<CustomObject>) queryCore.init(connectionCore.fetchConnection(false))
                                                .buildQuery()
                                                .executionQ(new Validator().notEmptyWithError, "Empty result set"))
                                                .to(Post.class)
                                                .mapping(new DatabaseUtility().rsToModel)
                                                .destroy()
                                                .getResponse();
```
if the ResultSet will be empty, an exception will be thrown with the specified message.

```java
ArrayList<CustomObject> l = (ArrayList<CustomObject>) queryCore.init(connectionCore.fetchConnection(false))
                                                .buildQuery()
                                                .executionQ(new Validator().notEmpty)
                                                .to(Post.class)
                                                .mapping(new DatabaseUtility().rsToModel)
                                                .destroy()
                                                .getResponse();
```
in this case, an exception will be thrown without any error.

### Chain functions

**init**
```java
public QueryCore init(Connection c)
```
Init functions will instantiate Prepared Statement object inside QueryCore.

**buildQuery**
```java
public QueryCore buildQuery()
```
BuildQuery will replace all Prepared Statement with the values of the list we passed as second parameter.







