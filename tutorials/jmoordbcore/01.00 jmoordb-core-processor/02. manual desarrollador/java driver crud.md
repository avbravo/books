# Java Driver Crud

[skip/limit ](https://www.mongodb.com/docs/drivers/java/sync/v4.3/fundamentals/crud/read-operations/limit/)

## Combining Skip and Limit
To see the next three longest books, append the skip() method to your find() call. The integer argument passed to skip() will determine how many documents the find operation returns:  
```java
  MongoCursor<Document> cursor = collection.find()
      .sort(ascending("length"))
      .limit(3)
      .skip(3)
      .iterator();
```

*** 
## Insert
[InsertOne](https://www.mongodb.com/docs/drivers/java/sync/v4.3/fundamentals/crud/write-operations/insert/)
 
```java
Document doc1 = new Document("color", "red").append("qty", 5);
InsertOneResult result = collection.insertOne(doc1);
System.out.println("Inserted a document with the following id: " 
    + result.getInsertedId().asObjectId().getValue());
```

Your output should look something like this:

Inserted a document with the following id: 60930c39a982931c20ef6cd6

***
## Write Operations
[write-operations](https://www.mongodb.com/docs/drivers/java/sync/v4.3/fundamentals/crud/write-operations/)
***
## Delete
[Delete](https://www.mongodb.com/docs/drivers/java/sync/v4.3/fundamentals/crud/write-operations/delete/)

***
## Update
[Update](https://www.mongodb.com/docs/drivers/java/sync/v4.3/fundamentals/crud/write-operations/change-a-document/)

