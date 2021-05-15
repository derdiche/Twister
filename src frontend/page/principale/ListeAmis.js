import React, { Component } from 'react';/*acces au propriètées de React et au Componenet*/
import '../../style/listeamis.css';
import Amis from  './listeamis/Amis';
import axios from 'axios';
import MesAmis from './listeamis/MesAmis';
class ListeAmis extends Component {
/*******************************************************/
/*                    constructeur                     */
/*******************************************************/
  constructor(props){
		super(props);
		this.state={userInput:'',ensemble:[],login:[],description:[],//amis rechercher
							myensemble:[],mylogin:[],mydescription:[],//mes amis
							indice:0								//indice de l'amis courant	
	
	};
		this.onChange=this.onChange.bind(this);
		this.chercherAmi=this.chercherAmi.bind(this);
		this.actualiseIndice=this.actualiseIndice.bind(this);
		this.mesAmis();
		setInterval(()=>this.mesAmis(),60000);//toute les 1 min
	}
	/*******************************************************/
	/*       MISE A JOUR DE L'INDICE DE L'AMIS COURANT     */
	/*******************************************************/
	actualiseIndice(i){
		this.setState({indice:i});
		alert(i+" a ete selectionner");
	}
	/*******************************************************/
	/*                  LISTE DE MES AMIS                  */
	/*******************************************************/
	async mesAmis(){
		await this.setState({
			myensemble:[],
			mylogin:[],
			mydescription:[]
		});
		axios.get("http://localhost:8080/Twister/friends/list",{params:{key:this.props.keyprincipale}})
			.then(response => {
				this.mesAmisjson(response.data)
			}
		);

	}
	mesAmisjson(data){
		this.setState({myensemble:[],mylogin:[],mydescription:[]});
		const valeur = JSON.parse(JSON.stringify(data));
		for (var value in valeur) {
			this.setState({myensemble:[...this.state.myensemble,value],mylogin:[...this.state.mylogin,valeur[value].user_name],mydescription:[...this.state.mydescription,valeur[value].description]})
		}
		this.props.nbamis(this.state.myensemble.length);
	}
	rendermesamis(){
		return this.state.myensemble.map((item,index) => {
				return (
					<div className="mesami"  key={index}>
						<MesAmis myidLM={item} myloginLM={this.state.mylogin[index]} mydescriptionLM={this.state.mydescription[index]} keyLM={this.props.keyprincipale} mesamiesLM={()=>this.mesAmis()} indice={index} actualiseIndice={this.actualiseIndice}/>
					</div>     
				);
			}
		);
	}
	/*******************************************************/
	/*                 RECHERCHER UN AMIS                  */
	/*******************************************************/
	async onChange(event){
		await this.setState({
			userInput: event.target.value,
			ensemble:[],
			login:[],
			description:[]
		});
		if(this.state.userInput!="")
			axios.get("http://localhost:8080/Twister/friends/search",{params:{key:this.props.keyprincipale, regex:this.state.userInput}})
				.then(response => {
					this.chercheamisjson(response.data)
				}
			);
	}
	chercheamisjson(data){
		this.setState({ensemble:[],date:[],nblikes:[],id:[]});
		const valeur = JSON.parse(JSON.stringify(data));
		for (var value in valeur) {
			this.setState({ensemble:[...this.state.ensemble,value],login:[...this.state.login,valeur[value].user_name],description:[...this.state.description,valeur[value].description]});
		}
	
	}
 	

	chercherAmi(event){
		event.preventDefault();
		this.setState({
				userInput: '',
				ensemble: [...this.state.ensemble, this.state.userInput]
		});
	}
	renderchercheramis(){
		
		return this.state.ensemble.map((item,index) => {
			return (
				<div className="ami"  key={index}>
					<a >
						<Amis idLM={item} loginLM={this.state.login[index]} descriptionLM={this.state.description[index]} keyLM={this.props.keyprincipale} /> 
						<span>{this.state.description[index]}.</span>
					</a>
					
				</div>     
			);
		});
		
	}
	/*******************************************************/
	/*                       RENDER                        */
	/*******************************************************/
	render(){
		return (
			<div className="amis">
				<section>
						<input 
							value={this.state.userInput} 
							type="text"
							placeholder="Chercher un ami"
							onChange={this.onChange}
							onkeydown={(event)=>this.coucou(event)}
						/>
				</section>
				{this.renderchercheramis()}
				<hr></hr>
				{this.rendermesamis()}
			</div>
			);
		}
}
export default ListeAmis;//permet de pouvoir exporter 
