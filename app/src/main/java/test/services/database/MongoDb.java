package test.services.database;

import com.mongodb.client.*;
import test.exceptions.ConnectionFailedException;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
import test.services.Env;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class MongoDb {
    private static MongoClient databaseClient;
    private static MongoDatabase database;
    private static final String uri = Env.get("DB_URI"),
            dbName = Env.get("DB_NAME");

    public static void init() throws ConnectionFailedException {
        databaseClient = MongoClients.create(uri);
        connectToDatabase();
        verifyConnection();
    }

    public static void connectToDatabase() {
        database = databaseClient.getDatabase(dbName);
    }

    public static void verifyConnection() throws ConnectionFailedException {
        boolean isConnected = isDatabaseConnected();
        if (databaseClient == null || !isConnected) {
            throw new ConnectionFailedException();
        }
    }

    public static boolean isDatabaseConnected() {
        try {
            Bson command = new BsonDocument("ping", new BsonInt64(1));
            database.runCommand(command);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Document[] findAllInCollection(String collectionName) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        FindIterable<Document> collectionElements = collection.find();
        List<Document> docList = new ArrayList<>();
        collectionElements.forEach(docList::add);
        return docList.toArray(Document[]::new);
    }

    public static Document findFirstDocument(String collectionName, String fieldName, String equalsTo) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection.find(eq(fieldName, equalsTo)).first();
    }
}
