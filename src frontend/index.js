import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import './style/index.css';
import Accueil from './page/Accueil';
import Principale from './page/Principale'


class MainPage extends Component {
  constructor(props){
    super(props);
    this.state={page:"premier", connect:false,login:"",key:"",id:"", nom:"", prenom:"", adressmail:"", mdp:""}
    this.changePage=this.changePage.bind(this);
    this.saveDataConnection=this.saveDataConnection.bind(this);
    this.saveDataUser=this.saveDataUser.bind(this);
  }
  changePage(){
    if(this.state.page==="premier"){
      this.setState({page:"deuxieme",connect:true});
    }
    else{
      this.setState({page:"premier",connect:false,login:"",key:"",id:"", nom:"", prenom:"", adressmail:"", mdp:""});
    }
  }

  saveDataConnection(valeur_id, valeur_login, valeur_key){
    this.setState({id:valeur_id, login:valeur_login, key:valeur_key});
  }

  saveDataUser(valeur_nom, valeur_prenom, valeur_login, valeur_adressmail, valeur_mdp){
    this.setState({nom:valeur_nom, prenom:valeur_prenom, login:valeur_login, adressmail:valeur_adressmail, mdp:valeur_mdp});
  }
 

  render() {
 if(this.state.page==="premier"){
      return(
        <Accueil page={this.changePage} saveDataConnection={this.saveDataConnection} saveDataUser={this.saveDataUser}/>
        );      
    }
    else{
      return (
        <Principale login={this.state.login} keyindex={this.state.key} page={this.changePage} />
        );
    }
  }
}

ReactDOM.render(
  <MainPage/>,
  document.getElementById('root')
  
);