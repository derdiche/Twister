package services;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import bdd.Requetebdd;
import tools.FriendsTools;
import tools.Tools;
import tools.UserTools;

public class FriendsServices {
        public static JSONObject addFriend(String key, String id_friend) {
                int  idf;
                try {
                        /*if(UserTools.dateSession(key)) {
                                UserTools.deleteSession(key);
                                return Tools.serviceRefused("session expirée", 10000);
                        }*/
                        if(id_friend==null||id_friend.isEmpty())
                                return Tools.serviceRefused("Veuillez saisir un id", -1);
                        if(key==null||key.length()!=32||!UserTools.userConnected(key)) {
                                return Tools.serviceRefused("vous n'etes pas connecte", -1);
                        }
                        idf = Integer.parseInt(id_friend);
                        FriendsTools.ajouterAmi(key, idf);
                        return Tools.serviceAccepted();
                }
                catch (SQLException e) {
                        return Tools.serviceRefused(e.getMessage(), 1000);
                }
                catch(NumberFormatException e){
                return Tools.serviceRefused("id_friend pas un entier", -1);
            }
        }
        public static JSONObject deleteFriend(String key, String id_friend) {
                int  idf;
                try {
                       /* if(UserTools.dateSession(key)) {
                                UserTools.deleteSession(key);
                                return Tools.serviceRefused("session expirée", 10000);
                        }*/
                        if(id_friend==null||id_friend.isEmpty())
                                return Tools.serviceRefused("Veuillez saisir un id", -1);
                        if(key==null||!UserTools.userConnected(key)) {
                                return Tools.serviceRefused("vous n'etes pas connecte", -1);
                        }
                        idf = Integer.parseInt(id_friend);
                        FriendsTools.supprimerAmi(key, idf);
                        return Tools.serviceAccepted();
                }
                catch (SQLException e) {
                        Tools.serviceRefused("erreur de SQL", 1000);
                }
                catch(NumberFormatException e){
                return Tools.serviceRefused("id_friend pas un entier", -1);
            }
                return null;
        }

        public static JSONObject listFriends(String key) {
                try {
                        /*if(UserTools.dateSession(key)) {
                                UserTools.deleteSession(key);
                                return Tools.serviceRefused("session expirée", 10000);
                        }*/
                        if(key==null || key.length()!=32 || !UserTools.userConnected(key))
                                return Tools.serviceRefused("vous n'etes pas connecte", -1);
                        return FriendsTools.listAmis(key);
                } catch (SQLException e) {
                        return Tools.serviceRefused("erreur de SQL", 1000);
                }catch (JSONException e) {
                        return Tools.serviceRefused("erreur JSON", 100);
                }
        }

        public static JSONObject acceptFriend(String key, String id_friend, String booleen) {
                int  idf;
                try {
                        /*if(UserTools.dateSession(key)) {
                                UserTools.deleteSession(key);
                                return Tools.serviceRefused("session expirée", 10000);
                        }*/
                        if(id_friend==null||id_friend.isEmpty())
                                return Tools.serviceRefused("Veuillez saisir un id", -1);
                        if(key==null||key.length()!=32||!UserTools.userConnected(key)) {
                                return Tools.serviceRefused("vous n'etes pas connecte", -1);
                        }
                        idf = Integer.parseInt(id_friend);
                        if(booleen.equals("true")) {
                                FriendsTools.accepterAmi(key, idf);
                                return Tools.serviceAccepted();
                        }
                        if(booleen.equals("false")) {
                                FriendsTools.supprimerAmi(Requetebdd.KeyFromId(idf), Requetebdd.IdFromKey(key));
                                return Tools.serviceAccepted();
                        }
                        else {
                                return Tools.serviceRefused("booleen incorrect", -1);
                        }
                }
                catch (SQLException e) {
                        return Tools.serviceRefused("erreur de SQL", 1000);
                }
                catch(NumberFormatException e){
                return Tools.serviceRefused("id_friend pas un entier", -1);
            }
        }
		public static JSONObject chercheAmis(String key, String regex)  {
			try {
                /*if(UserTools.dateSession(key)) {
                        UserTools.deleteSession(key);
                        return Tools.serviceRefused("session expirée", 10000);
                }*/
                if(regex==null||regex.isEmpty())
                        return Tools.serviceRefused("Veuillez saisir un regex", -1);
                if(key==null||key.length()!=32||!UserTools.userConnected(key)) {
                        return Tools.serviceRefused("vous n'etes pas connecte", -1);
                }
                
                return FriendsTools.chercheAmis(key,regex);
		    }
	    	catch (SQLException e) {
	            return Tools.serviceRefused(e.getMessage(), 1000);
	    	}
		    catch(NumberFormatException e){
		    	return Tools.serviceRefused("id_friend pas un entier", -1);
		    }
			catch(JSONException e) {
                return Tools.serviceRefused("erreur JSON", 100);
			}
		}


}
