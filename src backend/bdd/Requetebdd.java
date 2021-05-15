package bdd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class Requetebdd {

        /*******************************************************/
        /*                         SQL                         */
        /*******************************************************/
        public static int requeteUpdate(String requete) throws SQLException {
                Connection connection = Database.getMySQLConnection();
                Statement statement = connection.createStatement() ;
                int retour= statement.executeUpdate(requete);
                statement.close();
                connection.close();
                return retour;
        }
        public static Object[] requeteQuery(String requete) throws SQLException {
                Object retour[]=new Object[3];
                Connection connection = Database.getMySQLConnection();
                Statement statement = connection.createStatement() ;
                ResultSet resultats = statement.executeQuery(requete) ;
                retour[0]=resultats;
                retour[1]=statement;
                retour[2]=connection;
                return retour;


        }
        public static void queryClose(Object tab[]) throws SQLException {
                ResultSet resultats=(ResultSet)tab[0];
                Statement statement=(Statement)tab[1];
                Connection connection=(Connection)tab[2];
                resultats.close();
                statement.close();
                connection.close();

        }
        public static Boolean exist(String requete) throws SQLException {
                boolean retour=false;
                Object[]elem=requeteQuery(requete);
                ResultSet resultats=(ResultSet)elem[0];
                retour=resultats.next();
                queryClose(elem);
                return retour;
        }
        public static int getId(String login) throws SQLException {
                int id=-1;
                Object elem[]=requeteQuery("SELECT * FROM User WHERE user_name = '"+login+"';");
                ResultSet resultats = (ResultSet) elem[0];
                resultats.next();
                id=resultats.getInt("id_user");
                queryClose(elem);
                return id;
        }

        public static int IdFromKey(String key) throws SQLException {
                int mon_id = -1;
                Object[]elem = Requetebdd.requeteQuery("SELECT id_user FROM session WHERE key_session ='"+key+"';");
                ResultSet resultats = (ResultSet) elem[0];
                resultats.next();
                mon_id=resultats.getInt("id_user");
                return mon_id;
        }
        /*******************************************************/
        /*                        NOSQL                        */
        /*******************************************************/
        public static String compteurId() {
                MongoCollection<Document> coll=Database.createCountersCollection();
                Document compteur =coll.find().first();
                return compteur.get("_id").toString();

        }
        public static boolean mongoExistId(String id_message) {
                MongoCollection<Document> coll=Database.mongoCollection(DBStatic.mongoColl[0]);
                Document condition=new Document();
                condition.append("id",Integer.parseInt(id_message));
                MongoCursor<Document> curseur=coll.find(condition).iterator();
                return curseur.hasNext();

        }
        public static MongoCursor<Document> listMessage(String nomdeligne, int id){
                MongoCollection<Document> coll=Database.mongoCollection(DBStatic.mongoColl[0]);
                Document condition=new Document();
                condition.append(nomdeligne, id);
                return coll.find(condition).iterator();
        }
        public static MongoCursor<Document> listMessage(String key, String id_dest, String idmessagecommenter) throws SQLException{
            MongoCollection<Document> coll=Database.mongoCollection(DBStatic.mongoColl[0]);
            Document condition=new Document();
            Document date = new Document();
            ArrayList<Document> op = new ArrayList<>();
        	Document condition1=new Document();
        	Document condition2=new Document();
        	date.append("date", 1);
            /*if((id_dest=="-1"||id_dest==null||id_dest.isEmpty()) && (idmessagecommenter=="-1" || idmessagecommenter==null ||  idmessagecommenter.isEmpty())) {//message public
            	condition1.append("id_dest", -1);
            	condition2.append("id_commentaire", -1);
            	op.add(condition1);
            	op.add(condition2);
            	condition.append("$and", op);
            	return coll.find(condition).sort(date).iterator();
            }
            if(id_dest=="-1"||id_dest==null||id_dest.isEmpty()) {//commentaire
            	System.out.println("oui je rentre dans le if "+idmessagecommenter);
            	condition.append("id_commentaire",idmessagecommenter);
            	condition.append("id_dest", -1);
            	return coll.find(condition).iterator();
            }*/
        	if(((id_dest=="-1"||id_dest==null||id_dest.isEmpty()) && (idmessagecommenter=="-1" || idmessagecommenter==null ||  idmessagecommenter.isEmpty()))||(id_dest=="-1"||id_dest==null||id_dest.isEmpty())){
        		//condition.append("id_commentaire",-1);
        		return coll.find(condition).sort(date).iterator();
        	}
            else {//message prive
            	condition1.append("id_dest", IdFromKey(key));
            	condition1.append("id_source", id_dest);
            	condition2.append("id_dest", id_dest);
            	condition2.append("id_source", IdFromKey(key));
            	op.add(condition1);
            	op.add(condition2);
            	condition.append("$or", op);
            	
            	return coll.find(condition).sort(date).iterator();
            }
            
    }
        public static int autoIncremente() {
            MongoCollection<Document>coll=Database.createCountersCollection();
            Document searchQuery = new Document();
            searchQuery.append("_id",compteurId());
            Document increase = new Document();
            increase.append("seq", 1);
            Document updateQuery = new Document();
            updateQuery.append("$inc", increase);
            Document result = coll.findOneAndUpdate(new Document(), updateQuery);
            		// coll.findOneAndUpdate(searchQuery, updateQuery); avant ca marcher chez moi non 
            return Integer.parseInt(result.get("seq")+"");
        }
        public static JSONObject addMessage(int id_source, String text, String id_dest, String idmessagecommenter) throws JSONException, SQLException {
        	MongoCollection<Document> coll=Database.mongoCollection(DBStatic.mongoColl[0]);
        	int id=autoIncremente();
        	JSONObject retour = new JSONObject();
            Document document =new Document();
            document.append("id", id);
            document.append("text",text);
            document.append("login", pseudoFromId(id_source));
            document.append("nblikes", 0);
            GregorianCalendar calendar = new java.util.GregorianCalendar();
            Date date = calendar.getTime();
            document.append("date", date);
            document.append("id_source", id_source);
            retour.put("id_source", id_source);
            retour.put("id", id);
            retour.put("login", pseudoFromId(id_source));
            retour.put("text",text);
            retour.put("nblikes", 0);
            retour.put("date", date);
        	if(id_dest!=null&&!id_dest.isEmpty()) {//message prive
        		document.append("id_dest", id_dest);                                         //-1 message visible par tout le monde
                document.append("id_commentaire", -1);                          //-1 message non comment� sinon id du message comment�
                coll.insertOne(document);
                return retour;
        	}

		    if(idmessagecommenter!=null&&!idmessagecommenter.isEmpty()) {//commentaire
		    	document.append("id_dest", -1);                                         //-1 message visible par tout le monde
                document.append("id_commentaire", idmessagecommenter);                          //-1 message non comment� sinon id du message comment�
                coll.insertOne(document);
		    }
		    else {//message public
                document.append("id_dest", -1);                                         //-1 message visible par tout le monde
                document.append("id_commentaire", -1);                          //-1 message non comment� sinon id du message comment�
                coll.insertOne(document);               
		    }
		    return retour;
		}
                
        public static boolean like(int id_source,int id_message) {
                MongoCollection<Document> coll=Database.mongoCollection(DBStatic.mongoColl[0]);
                Document condition = new Document();
            condition.append("id",id_message);
            Document incremente = new Document();
                if(likeExist(id_source)) {
                        supprimeLike(id_source);
                   incremente.append("nblikes", -1);
                    Document maj = new Document();
                    maj.append("$inc", incremente);
                    coll.findOneAndUpdate(condition, maj);
                    return true ;
                }
                else {
                        ajouteLike(id_source);
                    incremente.append("nblikes", 1);
                    Document maj = new Document();
                    maj.append("$inc", incremente);
                    coll.findOneAndUpdate(condition, maj);
                    return true ;
                }
        }
        private static void supprimeLike(int id_source) {
                MongoCollection<Document> like=Database.createLikeCollection(id_source);
                Document condition=new Document();
                condition.append("id_source", id_source);
                like.findOneAndDelete(condition);
        }
        private static void ajouteLike(int id_source) {
                MongoCollection<Document> like=Database.createLikeCollection(id_source);
                Document condition=new Document();
                condition.append("id_source", id_source);
                like.insertOne(condition);
        }
        private static boolean likeExist(int id_source) {
                MongoCollection<Document> like=Database.createLikeCollection(id_source);
                Document condition=new Document();
                condition.append("id_source", id_source);
                return like.find(condition).iterator().hasNext();
        }

        public static boolean supprimeMessage(int id_messageASupp) {
                ArrayList<Integer>ids=aCommentaire(id_messageASupp);
                if(ids.size()!=0) {
                        for(Integer i:ids) {
                                supprimeMessage(Integer.parseInt(i+""));
                        }
                }
                MongoCollection<Document> coll=Database.mongoCollection(DBStatic.mongoColl[0]);
                Document document =new Document();
                document.append("id", id_messageASupp);
                coll.deleteOne(document);
                return true;

        }
        public static boolean monMessage(int id_source,int id_messageASupp) {
                MongoCollection<Document> coll=Database.mongoCollection(DBStatic.mongoColl[0]);
                Document condition=new Document();
                condition.append("id_source", id_source);
                condition.append("id", id_messageASupp);
                return coll.find(condition).iterator().hasNext();

        }
        /*
        }*/
        public static String KeyFromId(int idf) throws SQLException {
                String cle = "";
                Object[]elem = Requetebdd.requeteQuery("SELECT key_session FROM session WHERE id_user ='"+idf+"';");
                ResultSet resultats = (ResultSet) elem[0];
                resultats.next();
                cle=resultats.getString("key_session");
                return cle;
        }
        public static String pseudoFromId(int idf) throws SQLException {
            String pseudo = "";
            Object[]elem = Requetebdd.requeteQuery("SELECT * FROM User WHERE id_user ='"+idf+"';");
            ResultSet resultats = (ResultSet) elem[0];
            resultats.next();
            pseudo=resultats.getString("user_name");
            return pseudo;
    }
    public static ArrayList<Integer> aCommentaire(int id_message) {
            ArrayList<Integer> retour=new ArrayList<Integer>();
            MongoCollection<Document> coll=Database.mongoCollection(DBStatic.mongoColl[0]);
            Document condition=new Document();
            condition.append("id_commentaire", id_message);
            MongoCursor<Document> curseur= coll.find(condition).iterator();
            while(curseur.hasNext()) {
                    retour.add(curseur.next().getInteger("id"));
            }
            return retour;
    }
		
		
}
/*public static JSONObject addMessagePrive(int id_source,int id_dest,String message)throws JSONException,SQLException{
        		JSONObject retour = new JSONObject();
        		int id=autoIncremente();
                MongoCollection<Document> coll=Database.mongoCollection(DBStatic.mongoColl[0]);
                String login=Requetebdd.pseudoFromId(id_source);
                Document document =new Document();
                document.append("login", login);
                document.append("id", id);
                document.append("login", pseudoFromId(id_source));
                document.append("text",message);
                document.append("nblikes", 0);
                GregorianCalendar calendar = new java.util.GregorianCalendar();
                Date date = calendar.getTime();
                document.append("date", date);
                document.append("id_dest",id_dest );
                document.append("id_commentaire", -1);                          //-1 message non comment� sinon id du message comment�
                coll.insertOne(document);
                retour.put("id_source", id_source);
                retour.put("id", id);
                retour.put("login", pseudoFromId(id_source));
                retour.put("text",message);
                retour.put("nblikes", 0);
                retour.put("date", date);
                return retour;
        }
        public static JSONObject addCommentaire(int id_source,String message,int idmessagecommenter) throws JSONException,SQLException{
        		JSONObject retour = new JSONObject();
        		String login=Requetebdd.pseudoFromId(id_source);
        		MongoCollection<Document> coll=Database.mongoCollection(DBStatic.mongoColl[0]);
                int id=autoIncremente();
                Document document =new Document();
                document.append("id_source", id_source);
                document.append("login",login);
                document.append("text",message);
                document.append("id",id);
                document.append("nblikes", 0);
                GregorianCalendar calendar = new java.util.GregorianCalendar();
                Date date = calendar.getTime();
                document.append("date", date);
                document.append("id_dest",-1 );
                document.append("id_commentaire", idmessagecommenter);                          //-1 message non commenter sinon id du message commenter
                coll.insertOne(document);
                retour.put("id_source", id_source);
                retour.put("id",id);
                retour.put("login", pseudoFromId(id_source));
                retour.put("text",message);
                retour.put("nblikes", 0);
                retour.put("date", date);
                retour.put("id_commentaire", idmessagecommenter); 
                return retour;
                
        */
