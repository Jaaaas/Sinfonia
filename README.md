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
In order to use Sinfonia you simply need to import the **JAR** (
you can download it from here [Sinfonia](https://github.com/Jaaaas/Sinfonia/files/1296058/Sinfonia-1.0.zip) or from the releases) into the project and you can use its features.

### Features
We will divide the functionality for the two main entities described above.

### ConnectionCore
ConnectionCore allows you to establish the first connection to the database. It can be done through a json file where we will find the configuration parameters.

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

If you have already a Connection object, you can set it up using setConnection method

```java
public void setC(Connection c) 
```

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
qc.init(connectionCore.getConnection())
            .buildQuery()
            .executionU()
            .destroy();
```

* **Update query retrieving key**: if we need to get what **key** was updated
```java
int keyUpdated = qc.init(connectionCore.getConnection())
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

### Shortcuts

There are some **"shortcuts"** to decrease the length of the chain. 

```java
JsonArray jsonArray = (JsonArray) queryCore
                        .setup("SELECT * FROM tableName", Arrays.asList(args), connectionCore.fetchConnection(false))
                        .buildQuery()
                        .executionQ(new Validator().notEmpty)
                        .mapping( new DatabaseUtility().rsToJson)
                        .release();
```


### Chain functions

**Init** function will instantiate Prepared Statement object inside QueryCore.
```java
public QueryCore init(Connection c)
```


**BuildQuery** will replace all Prepared Statement with the values of the list we passed as second parameter.
```java
public QueryCore buildQuery()
```


**To** function is used to determine which class the ResultSet should be cast.
```java
public QueryCore to(Class c)
```


**ExcetuionQ** is used to execute the query. This function accept also a Validator as parameter.
```java
public QueryCore executionQ()
```

**ExecutionU** is used to execute an update query.
```java
public QueryCore executionU()
```

**Mapping** function is used to map the ResultSet to a JsonElement or to a CustomClass. It accept as parameter a Function or a BiFunction.
```java
public QueryCore mapping()
```

**Destroy** function is used to close all resources opened such as ResultSet and PreparedStatement.
```java
public QueryCore destroy()
```
### Related project
* **[Cosmo](https://github.com/Jaaaas/Cosmo)**

### Contributors
* **[suxl89](https://github.com/suxl89)**
* **[Nazzareno Di Pietro](https://github.com/Tignaman)**
* **[fpafumi](https://github.com/fpafumi)**

### Future developments

* **Testing**
* **Maven integration**
* **New validators**
* **New mapping operation**
* **Improves performance**
