package tools;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.client.MongoCursor;

import bdd.Requetebdd;
public class MessageTools extends Tools {
		public static JSONObject ajouteMessage(String key, String text, String id_dest, String idmessagecommenter)throws SQLException,JSONException  {
			int id= Requetebdd.IdFromKey(key);
            if(id<0)
                    return Tools.serviceRefused("Ce n'est pas un commentaire", -1);
            return Requetebdd.addMessage(id,text,id_dest,idmessagecommenter);
		}
       
        public static boolean like(String key, String id_message) throws SQLException, NumberFormatException {
                int id = Requetebdd.IdFromKey(key);
                int idm;
                if(id<0)
                        return false;
                idm=Integer.parseInt(id_message);
                return Requetebdd.like(id, idm);

        }
        public static JSONObject listMessage(String key, String id_dest, String idmessagecommenter) throws SQLException,JSONException {
                JSONObject retour = new JSONObject();
                MongoCursor<Document> curseur=Requetebdd.listMessage(key,id_dest, idmessagecommenter);
                while(curseur.hasNext()) {
                        Document courant=curseur.next();
                        JSONObject message=new JSONObject();
                        message.put("texte", courant.getString("text"));
                        message.put("id_source", courant.getInteger("id_source").toString());
                        message.put("login", courant.getString("login"));
                        message.put("id_dest", courant.get("id_dest"));
                        message.put("likes", courant.getInteger("nblikes").toString());
                        message.put("date", courant.getDate("date"));
                        message.put("id_commentaire", courant.get("id_commentaire"));
                        retour.put(courant.get("id").toString(),message);
                }
                return retour;
        }
      
       

		public static boolean supprimerMessage(String key, String id_message) throws SQLException , NumberFormatException{
                int id= Requetebdd.IdFromKey(key);
                int idm;
                if(id<0)
                        return false;
                idm=Integer.parseInt(id_message);
                if(!Requetebdd.monMessage(id, idm)) {
                        return false;
                }
                return Requetebdd.supprimeMessage(idm);
        }
        public static JSONObject comment(String key, String message, String id_commentaire) throws SQLException,NumberFormatException {
                int id= Requetebdd.IdFromKey(key);
                int idm;
                if(id<0)
                        return Tools.serviceRefused("Id incorrect", -1);
                idm=Integer.parseInt(id_commentaire);
                return Tools.idMessage(idm);
        }
}
