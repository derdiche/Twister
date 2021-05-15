package services;
import tools.*;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;
public class UserServices {
        public static JSONObject creationUser(String nom,String prenom,String login,String mdp,String adressmail) {
                try {
                        if((nom==null) ||nom.isEmpty()) {
                                return Tools.serviceRefused("Veuillez saisir votre nom", -1);
                        }
                        if((prenom==null) ||prenom.isEmpty()) {
                                return Tools.serviceRefused("Veuillez saisir votre prenom", -1);
                        }
                        if((login==null) ||login.isEmpty()) {
                                return Tools.serviceRefused("Veuillez saisir votre login", -1);
                        }
                        if((mdp==null) ||mdp.isEmpty()) {
                                return Tools.serviceRefused("Veuillez saisir votre mot de passe", -1);
                        }
                        else {
                                if(mdp.length()<5) {
                                        return Tools.serviceRefused("Mot de passe trop court", -1);
                                }
                        }
                        if((adressmail==null)||!Tools.isEmailValid(adressmail)) {
                                return Tools.serviceRefused("Adresse mail incorrecte", -1);
                        }

                        if(UserTools.userExist(login)) {
                                return Tools.serviceRefused("Login deja existant", 1);
                        }
                        if(UserTools.emailExist(adressmail)) {
                                return Tools.serviceRefused("Email deja approprie", 1);
                        }
                        if(UserTools.register( nom, prenom, login, mdp, adressmail))
                                return Tools.serviceAccepted();
                        else {
                                return Tools.serviceRefused("parametres incorrects", -1);
                        }
                } catch (SQLException e) {
                        return UserTools.serviceRefused("erreur de SQL", 1000);
                }
        }
        public static JSONObject login(String login, String mdp) {
                try {
                        if((login==null) || (login.isEmpty())){
                                return Tools.serviceRefused("Saisir un login", -1);
                        }
                        if((mdp==null) || (mdp.isEmpty())){
                                return Tools.serviceRefused("Saisir un mot de passe", -1);
                        }
                        if(!UserTools.userExist(login)) {
                                return Tools.serviceRefused("Le login n'existe pas", -1);
                        }
                        if(!UserTools.checkPassword(login, mdp)){
                                return Tools.serviceRefused("Mot de passe incorrect", -1);
                        }
                        String clef=Tools.createKey();
                        while(UserTools.userConnected(clef)) {
                                clef=Tools.createKey();
                        }
                        return UserTools.InsertNouvelleSession(login,clef);
                } catch (SQLException e) {
                        return UserTools.serviceRefused("erreur de SQL", 1000);

                } catch (JSONException e) {
                        return Tools.serviceRefused("erreur JSON", 100);
                }
        }
        public static JSONObject logout(String key)  {
                try {
                if((key==null) || (key.isEmpty())||!UserTools.userConnected(key)) {
                        return Tools.serviceRefused("clef invalide", -1);
                }
                UserTools.deleteSession(key);
                return Tools.serviceAccepted();
                } catch (SQLException e) {
                        return Tools.serviceRefused("erreur de SQL", 1000);
                }
        }
       
		public static JSONObject description(String key, String message) {
			try {
	            if((key==null) || (key.isEmpty())||!UserTools.userConnected(key)) {
	                    return Tools.serviceRefused("clef invalide", -1);
	            }
	            if(message==null||message.isEmpty()){
	            	return UserTools.getDescription(key);
	            }
	            else{
	            	return UserTools.setDescription(key,message);
	            }
			} catch (SQLException e) {
                return Tools.serviceRefused("erreur de SQL", 1000);
			} catch (JSONException e) {
        	 return Tools.serviceRefused("erreur JSON", 100);
			}
		}
		public static JSONObject notificationFriend(String key) {
			try {
				if((key==null) || (key.isEmpty())||!UserTools.userConnected(key)) {
                    return Tools.serviceRefused("clef invalide", -1);
				}
				return FriendsTools.notificationFriend(key);
				
			} catch (SQLException e) {
                return Tools.serviceRefused("erreur de SQL", 1000);
			} catch (JSONException e) {
        	 return Tools.serviceRefused("erreur JSON", 100);
			}
		}
		
}
