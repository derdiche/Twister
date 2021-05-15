package test;

import java.sql.SQLException;

import org.json.JSONException;
import org.json.JSONObject;

import bdd.DBStatic;
import bdd.Database;
import bdd.Requetebdd;
import services.MessageServices;
import services.UserServices;
import tools.FriendsTools;
import tools.MessageTools;
import tools.UserTools;

public class Test {
        public static void affiche(Object o) {
                System.out.println(o);
        }
        private static void assertion(Object emailExist, Object b) {
                if( emailExist == b)
                        affiche("assertion booleen valide");
                else
                        affiche("assertion booleen non valide");

        }
        public static void main(String[] args) {
                affiche("/*******************************************************/\n"+
                                "/*                         USER                        */\n"+
                                "/*******************************************************/\n");
                try {
                        assertion(UserTools.register("Aaguida", "Ilias", "smaug", "lolol", "devil@live.fr"),!Requetebdd.exist("SELECT * FROM User WHERE user_name = 'smaug';"));
                        assertion(UserTools.register("Derdiche", "Massyl", "fossyl", "lolol", "adressmail@live.fr"),!Requetebdd.exist("SELECT * FROM User WHERE User_name = 'fossyl';"));
                        assertion(UserTools.emailExist("adressmail@live.fr"),true);
                        assertion(UserTools.emailExist("****"),false);
                        assertion(UserTools.userExist("fossyl"),true);
                        assertion(UserTools.userExist("****"),false);
                        assertion(UserTools.checkPassword("fossyl", "lolol"), true);
                        assertion(UserTools.checkPassword("****", "****"), false);
                } catch (SQLException e) {e.printStackTrace();}

                affiche("/*******************************************************/\n"+
                                "/*                       FRIEND                        */\n"+
                                "/*******************************************************/\n");
                JSONObject l1=UserServices.login("fossyl", "lolol");
                JSONObject l2=UserServices.login("smaug", "lolol");
                try {
                        //FriendsServices.addFriend(l1.getString("key"), l2.getInt("id")+"");
                        assertion(FriendsTools.ajouterAmi(l1.getString("key"), l2.getInt("id")), true);
                        affiche(FriendsTools.listAmis(l1.getString("key")));
                        affiche(FriendsTools.listAmis(l2.getString("key")));
                        assertion(FriendsTools.accepterAmi(l2.getString("key"), l1.getInt("id")),true);
                        affiche(FriendsTools.listAmis(l1.getString("key")));
                        affiche(FriendsTools.listAmis(l2.getString("key")));
//                      FriendsTools.supprimerAmi(l1.getString("key"), l2.getInt("id"));
                        affiche(FriendsTools.listAmis(l1.getString("key")));
                        try {
                                Thread.sleep(4000);
                        } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                        }
                        affiche(FriendsTools.listAmis(l2.getString("key")));
                } catch (SQLException e2) {
                        e2.printStackTrace();
                } catch (JSONException e2) {
                        e2.printStackTrace();
                }



                affiche("/*******************************************************/\n"+
                                "/*                      MESSAGE                        */\n"+
                                "/*******************************************************/\n");
        /*      try {
                        MessageTools.ajouteMessage(l1.getString("key"), "cc");
                        affiche(MessageTools.listMessage(l1.getString("key")));
                        MessageTools.like(l1.getString("key"), "0");
                        affiche(MessageTools.listMessage(l1.getString("key")));
                        MessageServices.addMessage(l1.getString("key"), "message", null, null);
                        MessageServices.addMessage(l2.getString("key"), "commentaire", null, "0");
                        MessageServices.addMessage(l1.getString("key"), "J'ai pas trop aim� ton commentaire -_-'", "1", null);
                        MessageServices.like(l2.getString("key"), "2");
                        affiche(MessageTools.listMessage(l1.getString("key")));
                        affiche(MessageTools.listMessage(l2.getString("key")));
                        affiche(MessageServices.deleteMessage(l1.getString("key"), "0"));
                        affiche(MessageTools.listMessage(l1.getString("key")));
                        affiche(MessageTools.listMessage(l2.getString("key")));
//                      affiche(UserServices.logout(l1.getString("key")));
//                      affiche(UserServices.logout(l2.getString("key")));

//                      Database.createCountersCollection().drop();
//                      Database.createLikeCollection(-1).drop();
//                      Database.mongoCollection(DBStatic.mongoColl[0]).drop();

                } catch (SQLException | JSONException e) {
                        e.printStackTrace();
                }*/
                try {
                	Database.mongoCollection(DBStatic.mongoColl[0]).drop();
                	Database.mongoCollection(DBStatic.mongoColl[1]).drop();
                	//String key, String id_dest, String idmessagecommenter
                	//String key, String text, String id_dest, String idmessagecommenter
					MessageTools.ajouteMessage(l1.getString("key"), "cc",null,null);
					MessageTools.ajouteMessage(l2.getString("key"), "cc",null,null);
					MessageTools.ajouteMessage(l1.getString("key"), "salut","9",null);
					MessageTools.ajouteMessage(l2.getString("key"), "salut","10",null);
					JSONObject msgp=MessageTools.ajouteMessage(l2.getString("key"), "cool",null,"3");
					//affiche(MessageTools.listMessage(l1.getString("key"), null, null));
					//affiche(MessageTools.listMessage(l2.getString("key"), "10", null));
					affiche(MessageTools.listMessage(l2.getString("key"), null,"3"));
					
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Database.mongoCollection(DBStatic.mongoColl[0]).drop();
        }


}
