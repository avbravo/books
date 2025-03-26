# Como generar repositorios

## Imyectar
Definir atributos, leer de la configuration de @Repository los datos de la base de datos y de la coleccoión

    // <editor-fold defaultstate="collapsed" desc="@Inject">
    @Inject
    private Config config;
    @Inject
    @ConfigProperty(name = "mongodb.database")
    private String mongodbDatabase;
    private String mongodbCollection = "autosequence";
    @Inject
    MongoClient mongoClient;
 

// </editor-fold>



## En cada metodo
 MongoDatabase databaseMongoDB = mongoClient.getDatabase(mongodbDatabase);

  MongoCollection<Document> collectionMongoDB = databaseMongoDB.getCollection(mongodbCollection);

