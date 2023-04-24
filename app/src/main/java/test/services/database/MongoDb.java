package test.services.database;

import com.mongodb.client.MongoCollection;
import exceptions.ConnectionFailedException;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import test.services.Env;

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

    public static void connectToDatabase() throws ConnectionFailedException {
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
            Document commandResult = database.runCommand(command);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Document findFirstDocument(String collectionName, String fieldName, String equalsTo) {
        MongoCollection<Document> collection = database.getCollection(collectionName);
        return collection.find(eq(fieldName, equalsTo)).first();
    }
}
