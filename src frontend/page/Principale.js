import React, { Component } from 'react';/*acces au propriètées de React et au Componenet*/
import '../style/principale.css';
import Burger from './principale/Burger';
import Menu from  './principale/Menu';
import ListMessage from  './principale/ListMessage';
import ListeAmis from  './principale/ListeAmis';
import axios from 'axios';
class Principale extends Component {
/*******************************************************/
/*                    constructeur                     */
/*******************************************************/
  constructor(props){
    super(props);
    this.state={menu:"fermer",notification:null,etat:"publique",amis:0};
    this.public=this.public.bind(this);
    this.prive=this.prive.bind(this);
    this.gestionMenu=this.gestionMenu.bind(this);
    this.estouvert=this.estouvert.bind(this);
    this.notificationfriend=this.notificationfriend.bind(this);
    this.notificationfriend();
    this.nbamis=this.nbamis.bind(this);
   
  }
  /*******************************************************/
  /*                    PUBLIC/PRIVE                     */
  /*******************************************************/
  public(){
    this.setState({etat:"publique"});
  }
  prive(){
    if(this.state.amis>0)
      this.setState({etat:"prive"}); 
    else
      alert("ajoutez un amis pour aller en prive");

  }
  nbamis(nombre){
    this.setState({amis:nombre});
  }

  /*******************************************************/
  /*                    NOTIFICATION                     */
  /*******************************************************/
  notificationfriend(){
    axios.get("http://localhost:8080/Twister/friends/notification",{params:{key:this.props.keyindex}})
				.then(response => {
					this.notificationamisjson(response.data)
				}
      );
  }
  notificationamisjson(data){ 
    var valeur=JSON.parse(JSON.stringify(data));
		for (var value in valeur) {
      var reponse =window.confirm(`
                          @${valeur[value].user_name} souhaite vous ajouter comme amis:
                          description: ${valeur[value].description}
                       `);
	
        axios.get("http://localhost:8080/Twister/friends/accept",{params:{key:this.props.keyindex, idf:value,bool:reponse}})
				.then(response => {
          var variable=JSON.parse(JSON.stringify(response.data));
            if(variable.message!=undefined){
              alert(variable.message)
            }
          } 
        );
       
		}

  }
  /*******************************************************/
  /*                    GESTION MENU                     */
  /*******************************************************/
  gestionMenu(){
    if(this.estouvert()){
      this.setState({menu:"fermer"});
    }
    else{
      this.setState({menu:"ouvert"});
    }
  }
  estouvert(){
    return this.state.menu==="ouvert";
  }


  /*******************************************************/
  /*                       RENDER                        */
  /*******************************************************/
  render(){
    return (
          <div id="Principale">
            <div className="bouton" onClick={this.gestionMenu} >
              <div className="barre"></div>
              <div className="barre"></div>
              <div className="barre"></div> 
            </div>
            <Burger login={this.props.login} keyprincipale={this.props.keyindex} page={this.props.page} estouvert={this.estouvert}/>
            <Menu estouvert={this.estouvert} public={this.public} prive={this.prive} etat={this.state.etat}/>
            <div className="messamis">
              {this.state.etat==="publique"?<ListMessage login={this.props.login} estouvert={this.estouvert} keyprincipale={this.props.keyindex}/>:<div></div>}
              <ListeAmis  keyprincipale={this.props.keyindex} nbamis={this.nbamis}/>
            </div>
          </div>
      );
  }
}
export default Principale;//permet de pouvoir exporter 