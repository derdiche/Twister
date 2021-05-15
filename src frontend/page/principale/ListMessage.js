import React, { Component } from 'react';/*accès aux propriétés de React et au Component*/
import '../../style/listmessage.css';
import Message from  './listmessage/Message';
import fleche from '../../images/fleche.png';
import axios from 'axios';
class ListMessage extends Component {
/*******************************************************/
/*                    constructeur                     */
/*******************************************************/
  constructor(props){
    super(props);
    this.state={userInput:"",ensemble:[],date:[],nblikes:[],id:[],id_source:[],login:[]};
    this.onChange=this.onChange.bind(this);
    this.addMessage=this.addMessage.bind(this);
    setInterval(()=>this.recuperelistMessage(),1000)
   
    }
        
    recuperelistMessage(){
        axios.get("http://localhost:8080/Twister/messages/list",{params:{key:this.props.keyprincipale}})
          .then(response => {
            this.listmessagejson(response.data);
          })
    }
    listmessagejson(data){
        this.setState({ensemble:[],date:[],nblikes:[],id:[]});
        const valeur = JSON.parse(JSON.stringify(data));
        for (var value in valeur) {
            this.setState({ensemble:[...this.state.ensemble,valeur[value].texte], date:[...this.state.date,valeur[value].date],nblikes:[...this.state.nblikes,valeur[value].likes],id:[...this.state.id,value], id_source:[...this.state.id_source,valeur[value].id_source],login:[...this.state.login,valeur[value].login]});
        }
    }
    onChange(event){
        var objDiv = document.getElementById("scroll");
        objDiv.scrollTop = objDiv.scrollHeight;
    	this.setState({
            userInput: event.target.value
        });
    }
    addMessage(event){
        event.preventDefault();
        axios.get("http://localhost:8080/Twister/messages/add",{params:{key:this.props.keyprincipale, txt:this.state.userInput}})
         .then(response => {
            this.messagejson(response.data)
         })
        
    }
    messagejson(data){
        const valeur = JSON.parse(JSON.stringify(data));
        if(valeur.codeerreur===undefined){
            this.setState({
                userInput: '',
            });
        }
        else{
            alert(valeur.codeerreur);
        }
      }

    renderlist(){
    	return this.state.ensemble.map((item,index) => {
            return (
                <div className="messageindividuelle"  key={index}>
                    <Message item={item} date={this.state.date[index]} nblikes={this.state.nblikes[index]} idm={this.state.id[index]} keyListmessage={this.props.keyprincipale} login={this.state.login[index]} />
                </div>     
            );
        });
    }
  
  render(){
		return (
			<div className={this.props.estouvert() ?"messageo":"messagef"} >
                <div id="scroll" className="scroll">
				    {this.renderlist()}
                </div>
				<section className="ecrit" action={this.addMessage} > 
					<input 
						  value={this.state.userInput} 
			              type="text" 
			              placeholder="Ecrivez un message..."
			              onChange={this.onChange}
					/>
					<img className="fleche" onClick={this.addMessage} src={fleche} ></img>
				</section>
			</div>

		);
	}
}
export default ListMessage; //permet de pouvoir exporter 
