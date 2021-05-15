package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import services.*;

public class Logout extends HttpServlet {
        private static final long serialVersionUID = 1L;

    public Logout() {
        super();
    }

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                response.setContentType("text/json");//reponse en JSON
                PrintWriter pw= response.getWriter();//Writer de la reponse qui nous permettra un affichage sur la page web
                response.setHeader("Access-Control-Allow-Origin","*");//permettre à n'importe quelle ressource d'accéder à vos ressources
                /*******************************************************/
                /*              recuperation des parametres            */
                /*******************************************************/
                String clef = request.getParameter("key");
                /*******************************************************/
                /*               traitement de la requete              */
                /*******************************************************/
                JSONObject resultat=UserServices.logout(clef);
                /*******************************************************/
                /*                       Affichage                     */
                /*******************************************************/
                pw.println(resultat);
        }

}
