package tools;
import java.util.Random;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;
public class Tools {
        public static boolean isEmailValid(String email) {
            final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);
            return EMAIL_REGEX.matcher(email).matches();
        }
        public static JSONObject serviceRefused(String message,int codeE) {
                JSONObject retour =new  JSONObject();
                try {
                        retour.put("message",message);
                        retour.put("code erreur", codeE);
                } catch (JSONException e) {
                        e.printStackTrace();
                }
                return retour;
        }
        public static JSONObject serviceAccepted() {
                JSONObject retour =new  JSONObject();
                try {
                        retour.put("etat","ok");
                } catch (JSONException e) {
                        e.printStackTrace();
                }
                return retour;
        }
        public static String createKey() {
                int min,max;
                min=65;max=90;
                Random r= new Random();
                String cle="";
                for (int i =0;i<32;i++) {
                        int random=r.nextInt(max - min + 1) + min;
                        cle+=(char)random;
                }
                return cle;
        }
		public static JSONObject idMessage(int id) {
			JSONObject retour =new  JSONObject();
            try {
                    retour.put("Id",id);
            } catch (JSONException e) {
                    e.printStackTrace();
            }
            return retour;
		}

}
