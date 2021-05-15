package tools;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.JSONException;
import org.json.JSONObject;

import bdd.Requetebdd;
import tools.Tools;

public class UserTools extends Tools{
        public static JSONObject InsertNouvelleSession(String login,String key) throws SQLException, JSONException {
                int id=0;//id a recupere dans la base de donne
                JSONObject retour = new JSONObject();
                if(!userExist(login)) {
                        return Tools.serviceRefused("Login inexistant", -1);
                }
                id=Requetebdd.getId(login);
                if(Requetebdd.exist("SELECT * FROM session WHERE id_user = '"+id+"';")) {
                        delog(id);
                }
                Requetebdd.requeteUpdate("INSERT INTO `session` (`id_user`, `key_session`, `date_session`, `root_session`) VALUES ('"+id+"', '"+key+"', CURRENT_TIMESTAMP, '0');");
                retour.put("id", id);
                retour.put("login", login);
                retour.put("key", key);
                return retour;
        }
        public static  boolean userExist(String login) throws SQLException {
                boolean retour= Requetebdd.exist("SELECT * FROM `User` WHERE user_name = '"+login+"';");
                return retour;
        }
        public static boolean emailExist(String email) throws SQLException {
                        return Requetebdd.exist("SELECT * FROM `User` WHERE email = '"+email+"';");
        }
        public static boolean checkPassword(String login, String mdp) throws SQLException {
                String requete="SELECT * FROM User WHERE user_name = '"+login+"' AND password = '"+mdp+"';";
                Boolean retour=Requetebdd.exist(requete);
                return retour;
        }
        public static  boolean userConnected(String clef) throws SQLException {
                String requete="SELECT * FROM session WHERE key_session = '"+clef+"' ;";
                return Requetebdd.exist(requete);
        }
        public static boolean register(String nom, String prenom, String login, String mdp, String adressmail)throws SQLException {
                if(userExist(login)||emailExist(adressmail)) {
                        return false;
                }
                int resultats=0;
                String requete="INSERT INTO `User` (`id_user`, `user_name`, `password`, `email`, `name`, `first_name`) VALUES (NULL, '"+login+"', '"+mdp+"', '"+adressmail+"', '"+nom+"', '"+prenom+"'); ";
                resultats=Requetebdd.requeteUpdate(requete);
                if(resultats>=1)
                        return true;
                return false;
        }
        public static void deleteSession(String key) throws SQLException {
                Requetebdd.requeteUpdate("DELETE FROM `session` WHERE `session`.`key_session` = '"+key+"' ;");
        }
        public static void delog(int id) throws SQLException {
                Requetebdd.requeteUpdate("DELETE FROM `session` WHERE `session`.`id_user` = '"+id+"' ;");
        }
        public static boolean dateSession(String key) throws SQLException{
                Object elem[]=Requetebdd.requeteQuery("SELECT * FROM session WHERE key_session = '"+key+"';");
                ResultSet resultats = (ResultSet) elem[0];
                resultats.next();
                Date date_derniere_co = resultats.getDate("date_session");
                Calendar now =Calendar.getInstance();
                now.setTime(date_derniere_co);
            now.add(Calendar.MINUTE, 30);
            Date trentemin = now.getTime();
            GregorianCalendar calendar = new java.util.GregorianCalendar();
            Date date_du_jour = calendar.getTime();
                return date_du_jour.after(trentemin);
        }
		public static JSONObject getDescription(String key) throws SQLException, JSONException {
			//Object elem[]=Requetebdd.requeteQuery("SELECT * FROM `user`,`session` WHERE `user`.`id_user`=`session`.`id_user` AND `session`.`key_session`= \""+key+"\";");
			Object elem[]=Requetebdd.requeteQuery("SELECT * FROM `User`, `session` WHERE `session`.`key_session`=\""+key+"\" AND session.id_user=User.id_user;" );
			ResultSet resultats = (ResultSet) elem[0];
			 JSONObject retour = new JSONObject();
             if(resultats.next()){
            	  retour.put("description",resultats.getString("description"));
            	  return retour;
             }
             else{
            	 return Tools.serviceRefused("clefinvalide", 1);
             }
		}
		public static JSONObject setDescription(String key,String message) throws SQLException, JSONException {
			String requete="UPDATE `Derdiche-Ratiarison`.`User`,`session` SET `description` = \""+message+"\" WHERE `session`.`key_session` = \""+key+"\" AND `session`.`id_user`=`User`.`id_user`;";
			requete="UPDATE `User`,`session` SET `description` = \""+message+"\" WHERE `session`.`key_session`=\""+key+"\" AND `User`.`id_user` = `session`.`id_user`;";
			Requetebdd.requeteUpdate(requete);
			return Tools.serviceAccepted();
		}
		
}
