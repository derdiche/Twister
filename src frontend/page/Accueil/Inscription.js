import React, { Component } from 'react';/*acces au propriètées de React et au Componenet*/
import '../../style/inscription.css';
import axios from 'axios';
class Inscription extends Component {
/*******************************************************/
/*                    constructeur                     */
/*******************************************************/
  constructor(props){
    super(props);
    this.state={
      nom:"",
      prenom:"",
      login:"",
      adressmail:"",
      mdp:"",
      confirme:"",
    };
    this.changement=this.changement.bind(this);
    this.soumission=this.soumission.bind(this);
  }
  changement(event){
    event.preventDefault();
    const nom=event.target.name;
    const valeur=event.target.value;
    switch(nom){
      case "nom":
        this.setState({nom: valeur});
        break;
      case "prenom":
        this.setState({prenom: valeur});
        break;
      case "login":
        this.setState({login: valeur});
        break;
      case "adressmail":
        this.setState({adressmail: valeur});
        break;
      case "mdp":
         this.setState({mdp: valeur});
         break;
      case "confirme":
        this.setState({confirme: valeur});
        break;
      default:
        break;

    }
     
  }

  formulaireValide(){
    var retour=true;
    if(this.state.nom===""||this.state.prenom===""||this.state.login===""||this.state.adressmail===""||this.state.mdp===""||this.state.confirme===""){
      retour=false;
    }
    if(this.state.mdp!==this.state.confirme){
      alert("La confirmation doit être égale au mot de passe");
      retour = false;
    }
    return retour;
  }

  soumission(event){
    event.preventDefault();
    const nom=event.target.name;
    const valeur=event.target.value;
    if (this.formulaireValide()) {
      alert(`
        --soumission--
        prenom: ${this.state.prenom}
        pseudo: ${this.state.login}
        email: ${this.state.adressmail}
        mot de passe: ${this.state.mdp}
        confirmation du mot de passe : ${this.state.confirme}
      `);
      axios.get("http://localhost:8080/Twister/user/create",{params:{nom:this.state.nom, prenom:this.state.prenom, login:this.state.login, mdp:this.state.mdp, adressmail: this.state.adressmail}})
      .then(response => {
        this.connectjson(response.data);
      })
      //this.props.page();
    }
    else{
        alert("formulaire invalide");
    }
  	
  }

  connectjson(data){
    const valeur = JSON.parse(JSON.stringify(data));
    if(valeur.etat==="ok"){
      this.props.saveDataUser(this.state.nom, this.state.prenom, this.state.login, this.state.adressmail, this.state.mdp)
      this.props.connect(this.state.login,this.state.mdp);
    }
    else{
      alert(valeur.message);
    }
  }

  render(){
  	return (
  		<form  onSubmit={this.soumission} >
  			<h2>ENREGISTREMENT</h2>
        <input onChange={this.changement} name="nom" type="text" placeholder="Nom" />
  			<input onChange={this.changement} name="prenom" type="text" placeholder="Prenom" />
  			<input onChange={this.changement} name="login" type="text" placeholder="Login" />
        <input onChange={this.changement} name="adressmail" type="email" placeholder="Email" />
        <input onChange={this.changement} name="mdp" type="text" placeholder="Mot de passe" />
        <input onChange={this.changement} name="confirme" type="text" placeholder="Confirmer le mot de passe" />
  			<button onClick={this.soumission}>Envoyer</button>
  		</form>


  	);
  }
}
export default Inscription;//permet de pouvoir exporter 
/*
*/