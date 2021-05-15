import React, { Component } from 'react';
import '../../style/login.css';
import axios from 'axios';
class Login extends Component {
  constructor(props){
    super(props);
    this.state={login:"",mdp:""};
    this.changement=this.changement.bind(this);
    this.connect=this.connect.bind(this);
  }
  connect(){
    this.props.connect(this.state.login,this.state.mdp);
  }
 
  
  changement(event){
    event.preventDefault();
    const nom=event.target.name;
    const valeur=event.target.value;
    if(nom==="login"){
      this.setState({login:valeur});
    }
    else{
      this.setState({mdp:valeur});
    }     
  }

  render() {
 	return (
 		<div className="login">
      <h4>LOGIN</h4>
      <input onChange={this.changement} className="item" type="text" name="login" placeholder="Entrez un Login"/>
      <input onChange={this.changement} className="item" type="password" name="mdp" placeholder="Mot de Passe" />
      <button className="item" onClick={this.connect}>Connexion</button>
		</div>
 		);
  }
}
export default Login;
