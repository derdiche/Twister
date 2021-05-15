package bdd;

public class DBStatic {
	public static boolean mysql_pooling=false;
	public static String mysql_db="Derdiche-Ratiarison";
	public static String mysql_username= "root";
	public static String mysql_host= "localhost";
	public static String mysql_password= "";
	public static String mongo_host=mysql_host;
	public static String mongo_db=mysql_db;
	public static String mongo_port="27017";
	public static String mongo_create="mongodb://localhost:27017";
	public static String mongoColl[]= {"messages","compteur"};
	
	//root a la fac
	/*public static final String tables [] = {"user","session","friendship"};
	public static final String userColonne [] = {"id_user","user_name","password","email","name","first_name"};
	public static final String sessionColonne [] = {"id_user","key_session","date_session","root_session"};
	public static final String friendShipColonne [] = {"id_user1","id_user2","date_friendship"};*/
	
}
