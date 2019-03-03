package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

import Commands.Command;

public class Report {
	private static final String COLLECTION_NAME = "reports";

	private static MongoCollection<Document> collection = null;

	public static HashMap<String, Object> create(HashMap<String, Object> atrributes,String username) {

		MongoClientURI uri = new MongoClientURI(
				"mongodb://admin:admin@cluster0-shard-00-00-nvkqp.gcp.mongodb.net:27017,cluster0-shard-00-01-nvkqp.gcp.mongodb.net:27017,cluster0-shard-00-02-nvkqp.gcp.mongodb.net:27017/El-Menus?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true");

		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("El-Menus");

		// Retrieving a collection
		MongoCollection<Document> collection = database.getCollection("reports");
		Document newReport = new Document();

		for (String key : atrributes.keySet()) {
			newReport.append(key, atrributes.get(key));
		}
		newReport.append("reported_username", username);
		collection.insertOne(newReport);

		return atrributes;
	}
	public static ArrayList<HashMap<String, Object>> get(String username) {
		MongoClientURI uri = new MongoClientURI(
				"mongodb://admin:admin@cluster0-shard-00-00-nvkqp.gcp.mongodb.net:27017,cluster0-shard-00-01-nvkqp.gcp.mongodb.net:27017,cluster0-shard-00-02-nvkqp.gcp.mongodb.net:27017/El-Menus?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true");

		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("El-Menus");

		// Retrieving a collection
		MongoCollection<Document> collection = database.getCollection("reports");
		BasicDBObject query = new BasicDBObject();
		query.put("reported_username", username);

		FindIterable<Document> docs = collection.find(query);
		JSONParser parser = new JSONParser(); 
		ArrayList<HashMap<String, Object>> reports = new ArrayList<HashMap<String, Object>>();

		for (Document document : docs) {
			JSONObject json;
			try {
				json = (JSONObject) parser.parse(document.toJson());
				HashMap<String, Object> report = Command.jsonToMap(json);	
				reports.add(report);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		mongoClient.close();
        return reports;
		
	}
}