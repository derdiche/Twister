package tools;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import bdd.Requetebdd;
import tools.Tools;

public class FriendsTools extends Tools {
        public static boolean ajouterAmi(String key, int idf) throws SQLException {
                int id= Requetebdd.IdFromKey(key);
                if(id==idf){
                	return false;
                }
                if(id<0||Requetebdd.exist("SELECT * FROM FriendShip WHERE id_user1 = '"+id+"' AND id_user2 = '"+idf+"';")) {
                    return false;
                }
                Requetebdd.requeteUpdate("INSERT INTO FriendShip(id_user1, id_user2, date_friendship) values('"+id+"', '"+idf+"', CURRENT_TIMESTAMP);");
                return true;
        }
        public static JSONObject listAmis(String key) throws SQLException, JSONException {
                int id =Requetebdd.IdFromKey(key);
                JSONObject retour = new JSONObject();
                Object elem[]=Requetebdd.requeteQuery("SELECT * FROM FriendShip f, User u WHERE f.id_user1 = '"+id+"' AND u.id_user = f.id_user2 AND f.accepted = 1;");
                ResultSet resultats = (ResultSet) elem[0];
    			while(resultats.next()) {
    				JSONObject courant=new JSONObject();
                	courant.put("user_name", resultats.getString("user_name"));
                	courant.put("description", resultats.getString("description"));
                	retour.put(resultats.getInt("id_user")+"" ,courant );
                       
                }
    			Requetebdd.queryClose(elem);
    			return retour;
        }
        public static boolean supprimerAmi(String key, int idf) throws SQLException {
                int id= Requetebdd.IdFromKey(key);
                boolean retour=Requetebdd.exist("SELECT * FROM FriendShip WHERE id_user1 = '"+idf+"' AND id_user2 = '"+id+"' AND accepted = 1;");
                if(retour) {
                        Requetebdd.requeteUpdate("DELETE FROM FriendShip WHERE id_user1 = '"+id+"' AND id_user2 = '"+idf+"';");
                        Requetebdd.requeteUpdate("DELETE FROM FriendShip WHERE id_user1 = '"+idf+"' AND id_user2 = '"+id+"';");
                }
                return retour;
        }
        public static boolean accepterAmi(String key, int idf) throws SQLException {
                int id = Requetebdd.IdFromKey(key);
                if(Requetebdd.exist("SELECT * FROM FriendShip WHERE id_user1 = '"+idf+"' AND id_user2 = '"+id+"' AND accepted = 0;")) {
                        Requetebdd.requeteUpdate("UPDATE FriendShip SET accepted = 1 WHERE id_user1 = '"+idf+"' AND id_user2 = '"+id+"';");
                        Requetebdd.requeteUpdate("INSERT INTO FriendShip(id_user1, id_user2, date_friendship, accepted) values('"+id+"', '"+idf+"', CURRENT_TIMESTAMP, 1);");
                        return true;
                }
                return false;
        }
		public static JSONObject chercheAmis(String key,String regex) throws SQLException, JSONException {
			JSONObject retour = new JSONObject();
            Object elem[]=Requetebdd.requeteQuery("SELECT * FROM `user` WHERE `user`.`user_name` REGEXP \"^"+regex+"\";");
            ResultSet resultats = (ResultSet) elem[0];
            int id =Requetebdd.IdFromKey(key);
            while(resultats.next()) {
            	if(id!=resultats.getInt("id_user")){
            	JSONObject courant=new JSONObject();
            	courant.put("user_name", resultats.getString("user_name"));
            	courant.put("description", resultats.getString("description"));
            	retour.put(resultats.getInt("id_user")+"" ,courant );}
                   
            }
            Requetebdd.queryClose(elem);
			return retour;
		}
		public static JSONObject notificationFriend(String key) throws SQLException, JSONException {
			int id =Requetebdd.IdFromKey(key);
			JSONObject retour = new JSONObject();
			String requete="SELECT * FROM `user`, `friendship` WHERE `friendship`.`id_user2`="+id+" AND `user`.`id_user`=`friendship`.`id_user1` AND `friendship`.`accepted`=0";
			Object elem[]=Requetebdd.requeteQuery(requete);
			ResultSet resultats = (ResultSet) elem[0];
			while(resultats.next()) {
				JSONObject courant=new JSONObject();
            	courant.put("user_name", resultats.getString("user_name"));
            	courant.put("description", resultats.getString("description"));
            	retour.put(resultats.getInt("id_user")+"" ,courant );
                   
            }
			Requetebdd.queryClose(elem);
			return retour;
		}
}
