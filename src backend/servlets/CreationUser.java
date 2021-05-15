package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import services.*;
import org.json.JSONObject;
public class CreationUser extends HttpServlet {
        public CreationUser() {
        super();
    }

        private static final long serialVersionUID = 1L;
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
                response.setContentType("text/json");//reponse en JSON
                PrintWriter pw= response.getWriter();//Writer de la reponse qui nous permettra un affichage sur la page web
                response.setHeader("Access-Control-Allow-Origin","*");//permettre à n'importe quelle ressource d'accéder à vos ressources
                /*******************************************************/
                /*              recuperation des parametres            */
                /*******************************************************/
                String nom, prenom, login, mdp, adressmail;
                nom=request.getParameter("nom");
                prenom=request.getParameter("prenom");
                login=request.getParameter("login");
                mdp=request.getParameter("mdp");
                adressmail=request.getParameter("adressmail");
                /*******************************************************/
                /*               traitement de la requete              */
                /*******************************************************/
                JSONObject resultat=UserServices.creationUser(nom, prenom, login, mdp, adressmail);
                /*******************************************************/
                /*                       Affichage                     */
                /*******************************************************/
                pw.println(resultat);

        }

}
