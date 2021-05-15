import React, { Component } from 'react';/*acces au propriètées de React et au Componenet*/
import search from '../images/search.png';
import partage from '../images/partage.png';
import chat from '../images/chat.png';
import logo from '../images/logo.png';
import '../style/accueil.css';
import Inscription from './Accueil/Inscription'
import Login from './Accueil/Login'
import axios from 'axios';

class Accueil extends Component {
/*******************************************************/
/*                    constructeur                     */
/*******************************************************/
  constructor(props){
    super(props);
     this.connect=this.connect.bind(this);
  }
   connect(login,mdp){
    if(login===""||mdp===""){
      alert("Veuillez saisir votre login et votre mot de passe");
    }
    else{
      axios.get("http://localhost:8080/Twister/authentification/login",{params:{login:login, mdp:mdp}})
      .then(response => {
        this.connectjson(response.data);
      })
    }
  }

  connectjson(data){
    const valeur = JSON.parse(JSON.stringify(data));
    if(valeur.message===undefined){
  		this.props.saveDataConnection(valeur.id, valeur.login, valeur.key);
  		this.props.page();
    }
    else{
      alert(valeur.message);
    }
  }
  render(){
    return (<div className="coupe2">
			<div id="gauche">
				<div className="ligne">
					<img className="icone" src={search}/><p>Recherchez vos centres d'intérêt</p>
				</div>
				<div className="ligne">
					<img className="icone" src={partage}/><p>Partagez avec le monde entier</p>
				</div>
				<div className="ligne">
					<img className="icone" src={chat}/><p>Discutez librement</p>
				</div>
			</div>
			<div id="droite">
				<nav id="menu">
					<img id="logo" src={logo}/>
					<h1>TWISTER</h1>
					<Login connect={this.connect}/>
				</nav>
				<div id="save">
					<Inscription saveDataConnection={this.props.saveDataConnection} connect={this.connect} saveDataUser={this.props.saveDataUser}/>
				</div>
			</div>

		</div>);

  }
}

export default Accueil;//permet de pouvoir exporter 