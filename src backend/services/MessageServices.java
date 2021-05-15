package services;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import bdd.Requetebdd;
import tools.MessageTools;
import tools.Tools;
import tools.UserTools;

public class MessageServices {
        public static JSONObject addMessage(String key, String text, String id_dest, String idmessagecommenter) {
            try {
                   /* if(UserTools.dateSession(key)) {
                            UserTools.deleteSession(key);
                            return Tools.serviceRefused("expiration", 10000);
                    }*/
                    if(key==null||!UserTools.userConnected(key)) {
                            return Tools.serviceRefused("clef non valide",-1);
                    }
                    if(text==null||text.isEmpty()) {
                            return Tools.serviceRefused("Veuillez saisir un texte",-1);
                    }
                    return MessageTools.ajouteMessage(key, text,id_dest,idmessagecommenter);
            } catch (SQLException e) {
                    return Tools.serviceRefused("erreur de SQL", 1000);
            }
            catch(NumberFormatException e) {
            	return Tools.serviceRefused("parametre incorrect", -1);
            }
            catch(JSONException e) {
                return Tools.serviceRefused("erreur JSON", 100);
            }
        }
        public static JSONObject deleteMessage(String key,String id_message) {
                try {
                        if(UserTools.dateSession(key)) {
                                UserTools.deleteSession(key);
                                return Tools.serviceRefused("expiration", 10000);
                        }
                        if(key==null||!UserTools.userConnected(key)) {
                                return Tools.serviceRefused("clef non valide",-1);
                        }
                        if(id_message==null || id_message.isEmpty()|| !Requetebdd.mongoExistId(id_message)) {
                                return Tools.serviceRefused("id message non valide", -1);
                        }
                        if(MessageTools.supprimerMessage(key, id_message)) {
                                return Tools.serviceAccepted();
                        }
                        else {
                                return Tools.serviceRefused("Erreur inconnue", -1);
                        }
                } catch (SQLException e) {
                        return Tools.serviceRefused("erreur de SQL", 1000);
                }
                catch(NumberFormatException e) {
                        return Tools.serviceRefused("parametre incorrect", -1);
                }
        }
        public static JSONObject listMessage(String key, String id_dest, String idmessagecommenter) {
                try {
                       /* if(UserTools.dateSession(key)) {
                                UserTools.deleteSession(key);
                                return Tools.serviceRefused("expiration", 10000);
                        }*/
                        if(key==null||!UserTools.userConnected(key)) {
                                return Tools.serviceRefused("clef non valide",-1);
                        }
                        return MessageTools.listMessage(key,id_dest, idmessagecommenter);
                } catch (SQLException e) {
                        return Tools.serviceRefused("erreur de SQL", 1000);
                }
                catch(JSONException e) {
                        return Tools.serviceRefused("erreur JSON", 100);
                }

        }
        public static JSONObject like(String key, String idm) {
                try {
                        /*if(UserTools.dateSession(key)) {
                                UserTools.deleteSession(key);
                                return Tools.serviceRefused("expiration", 10000);
                        }*/
                        if(key==null||!UserTools.userConnected(key)) {
                                return Tools.serviceRefused("clef non valide",-1);
                        }
                        if(idm==null||idm.isEmpty()) {
                                return Tools.serviceRefused("Veuillez saisir un identifiant",-1);
                        }
                        MessageTools.like(key, idm);
                        return Tools.serviceAccepted();
                } catch (SQLException e) {
                        return Tools.serviceRefused("erreur de SQL", 1000);
                }
                catch(NumberFormatException e ){
                        return Tools.serviceRefused("Parametre incorrect", -1);
                }
        }
		/*public static JSONObject comment(String key, String id) {
			 try {
                 if(UserTools.dateSession(key)) {
                         UserTools.deleteSession(key);
                         return Tools.serviceRefused("expiration", 10000);
                 }
                 if(key==null||!UserTools.userConnected(key)) {
                         return Tools.serviceRefused("clef non valide",-1);
                 }
                 return MessageTools.listComment(id);
         } catch (SQLException e) {
                 return Tools.serviceRefused("erreur de SQL", 1000);
         }
         catch(JSONException e) {
                 return Tools.serviceRefused("erreur JSON", 100);
         }
		}*/

}
