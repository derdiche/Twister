import React, { Component } from 'react';/*accès aux propriétés de React et au Component*/
import '../../../style/mesamis.css';
import axios from 'axios';
class MesAmis extends Component {
/*******************************************************/
/*                    constructeur                     */
/*******************************************************/
  constructor(props){
    super(props);
    // id:{this.props.myidLM} desc:{this.props.mydescriptionLM} clef:{this.props.keyLM}
  }
  /*******************************************************/
  /*                     SUPPRIMER AMIS                  */
  /*******************************************************/
  deletefriend(event){
    event.preventDefault();
    axios.get("http://localhost:8080/Twister/friends/delete",{params:{key:this.props.keyLM,idf:this.props.myidLM}})
			.then(response => {
				this.deletefriendjson(response.data)
			}
		);
  }
  deletefriendjson(data){
    const valeur = JSON.parse(JSON.stringify(data));
    if(valeur.message!=undefined){
      alert(valeur.message);
    }
    else{
      this.props.mesamiesLM();
    }
  }
  /*******************************************************/
  /*       MISE A JOUR DE L'INDICE DE L'AMIS COURANT     */
  /*******************************************************/
  
  majAmis(){
    this.props.actualiseIndice(this.props.indice);
  }
  /*******************************************************/
  /*                       RENDER                        */
  /*******************************************************/
  render(){
        return (
            <div className="myfriend" onClick={()=>this.majAmis()}>
              <p >login:@{this.props.myloginLM}  description:{this.props.mydescriptionLM}</p> 
              <p className="suppfriend" onClick={(event)=>this.deletefriend(event)}>supprimer</p>
            </div>
        );
    }
}
export default MesAmis; //permet de pouvoir exporter 
 