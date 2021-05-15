import React, { Component } from 'react';/*accès aux propriétés de React et au Component*/
import '../../../style/amis.css';
import axios from 'axios';
class Amis extends Component {
/*******************************************************/
/*                    constructeur                     */
/*******************************************************/
  constructor(props){
    super(props);
    // {this.props.idLM} {this.props.descriptionLM}
  }
    
  ajouteAmis(event){
    axios.get("http://localhost:8080/Twister/friends/add",{params:{key:this.props.keyLM, idf:this.props.idLM}})
				.then(response => {
					this.chercheamisjson(response.data)
				}
			);
  }
  render(){
			return (
        <div onClick={(event)=>this.ajouteAmis(event)} >
			    <p >@{this.props.loginLM}</p> 
				</div>
			);
		}
}
export default Amis; //permet de pouvoir exporter 
 