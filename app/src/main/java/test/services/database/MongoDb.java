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
    private static MongoClient DATABASE_CLIENT;
    private static MongoDatabase DATABASE;
    private static final String URI = Env.get("DB_URI"),
            DB_NAME = Env.get("DB_NAME");

    public static void init() throws ConnectionFailedException {
        DATABASE_CLIENT = MongoClients.create(URI);
        connectToDatabase();
        verifyConnection();
    }

    public static void connectToDatabase() {
        DATABASE = DATABASE_CLIENT.getDatabase(DB_NAME);
    }

    public static void verifyConnection() throws ConnectionFailedException {
        boolean isConnected = isDatabaseConnected();
        if (DATABASE_CLIENT == null || !isConnected) {
            final String errorMessage = "\nDatabase connection failed." +
                    " Please check DB_URI and DB_NAME env vars." +
                    " And verify that the database running.\n";
            throw new ConnectionFailedException(errorMessage);
        }
    }

    public static boolean isDatabaseConnected() {
        try {
            Bson command = new BsonDocument("ping", new BsonInt64(1));
            DATABASE.runCommand(command);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Document[] findAllInCollection(String collectionName) {
        MongoCollection<Document> collection = DATABASE.getCollection(collectionName);
        FindIterable<Document> collectionElements = collection.find();
        List<Document> docList = new ArrayList<>();
        collectionElements.forEach(docList::add);
        return docList.toArray(Document[]::new);
    }

    public static Document findFirstDocument(String collectionName, String fieldName, String equalsTo) {
        MongoCollection<Document> collection = DATABASE.getCollection(collectionName);
        return collection.find(eq(fieldName, equalsTo)).first();
    }
}
