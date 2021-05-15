import React, { Component } from 'react';/*acces au propriètées de React et au Componenet*/
import '../../style/burger.css';
import axios from 'axios';
import logo from '../../images/logo.png';
class Burger extends Component {
/*******************************************************/
/*                    constructeur                     */
/*******************************************************/
  constructor(props){
    super(props);
    this.state={description:"salut",clef:this.props.keyprincipale,prompt:undefined};
    this.description(this.state.clef,"");
    this.deco=this.deco.bind(this);
    this.description=this.description.bind(this);
    }
    deco(){
    	if(window.confirm("voulez vous vraiment vous deconnecter ?")){
    		 axios.get("http://localhost:8080/Twister/authentification/logout",{params:{key:this.props.keyprincipale}})
              .then(response => {
                this.descriptionjson(response.data);
              });
            this.props.page();
    	}
    }
    profil(event){
    	
    }
    description(cle,message){
    	 axios.get("http://localhost:8080/Twister/add/description",{params:{key:cle, message:message}})
      .then(response => {
        this.descriptionjson(response.data);
      })
    }
    
    async sendDescription(){
        var userinput= prompt("une petite description c'est cool!");
    	await this.setState({prompt : userinput});
        if(this.state.prompt!==null && this.state.prompt.length!==0){
    		
    		this.description(this.props.keyprincipale,this.state.prompt);
    		
    	}
        
    	
    }
    descriptionjson(data){

        const valeur = JSON.parse(JSON.stringify(data));
        if(valeur.message===undefined ){
            if(valeur.etat===undefined){
                this.setState({description:valeur.description});
            }
            else{
               
                this.setState({description:this.state.prompt});
                
            }
        }
        else{
            alert(valeur.message);
        }

    }  
  render(){
			return (
				<div className={this.props.estouvert()? "menuo": "menuf"}>
				<input onChange={(event)=>this.profil()} type="file" accept=".jpg, .jpeg, .png"/>
				<img  src={logo}/>
                <p id="pseudo">{this.props.login}</p>
				<br></br>
				<hr></hr>
				<p id="desc" onClick={()=>this.sendDescription()}>{this.state.description} </p>
				<hr></hr>
				<ul>
                <br></br>
					<br></br>
					<li ><p>Creer un salon de discussion</p></li>
					<li><p>Rejoindre un salon de discussion</p></li>
		
					
					<br></br><br></br><br></br><br></br>
					<li><span id="delog" onClick={this.deco} >Deconnexion</span></li>

					
				</ul>
			</div>);
		}
}
export default Burger;//permet de pouvoir exporter 