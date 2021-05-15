package bdd;


import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException; 
import javax.naming.*;
import javax.sql.DataSource;
import org.bson.Document;
import com.mongodb.client.*;


public class Database{
	private DataSource dataSource;
	private static Database database;
	public Database(String jndiname) throws SQLException { 
		try { 
			dataSource = (DataSource) new InitialContext().lookup("java:comp/env/" + jndiname); 
			
		} catch (NamingException e) { 
			throw new SQLException(jndiname + " is missing in JNDI! : "+e.getMessage()); 
		} 
	}
	public Connection getConnection() throws SQLException { 
		return dataSource.getConnection(); 
	}
	public static  Connection getMySQLConnection() throws SQLException { 
		if (DBStatic.mysql_pooling==false) {
			Class c;
			try {
				c = Class.forName("com.mysql.jdbc.Driver");
				Driver pilote = (Driver)c.newInstance() ;
				DriverManager.registerDriver(pilote);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         
			return(DriverManager.getConnection("jdbc:mysql://" + DBStatic.mysql_host + "/" + 
			DBStatic.mysql_db, DBStatic.mysql_username, DBStatic.mysql_password));
		} 
		else { 
			if (database==null) { 
				database=new Database("jdbc/db"); 
			} 
			return(database.getConnection()); 
		} 
	}
	
	public static MongoDatabase mongoCreate(){
			MongoClient mongo = MongoClients.create(DBStatic.mongo_create);
			MongoDatabase db = mongo.getDatabase(DBStatic.mongo_db);
			return db;
		
	}
	public static MongoCollection<Document> mongoCollection(String nom) {
		return mongoCreate().getCollection(nom);
	}
	public static MongoCollection<Document> createCountersCollection() {
		 MongoCollection<Document>coll = mongoCollection(DBStatic.mongoColl[1]);
		Document document = new Document();
	    document.append("seq", 0);
	    coll.insertOne(document);
	    return coll;
	}
	public static MongoCollection<Document> createLikeCollection(int id_source) {
		MongoCollection<Document> like = Database.mongoCollection("like");
		Document document =new Document();
		document.append("id_source", -1);
		like.insertOne(document);
		return like;
	}
	

		
}


