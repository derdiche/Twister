import React, { Component } from 'react';
import '../../../style/message.css';
import fleche from '../../../images/fleche.png';
import axios from 'axios';
class Message extends Component {
  constructor(props){
  super(props);
  this.state={commentaire:"+", userInput:"",ensemble:[],date:[],nblikes:[],id:[],id_source:[],login:[]};
  this.envoyerComment=this.envoyerComment.bind(this);
  this.onChange=this.onChange.bind(this);
  } 
  
  
  commentaire(){
    if(this.state.commentaire==="-"){
      return(
        <form className="ecrit">
        {this.renderlist()}
          <input 
              value={this.state.userInput} 
                    type="text" 
                    placeholder="commenter..."
                    onChange={this.onChange}
          />
          <img className="fleche" onClick={this.envoyerComment} src={fleche} ></img>
        </form>
        );
    }
  }
  comments(event){
    event.preventDefault();
    if(this.state.commentaire==="-"){
      this.setState({commentaire:"+"})
    }
    else{
       this.setState({commentaire:"-"});
       this.recupereListComment();
       alert(`
       mon id message :${this.props.idm}
       `);
    }
  }
  recupereListComment(){
      axios.get("http://localhost:8080/Twister/messages/list",{params:{key:this.props.keyListmessage, id_commentaire:this.props.idm}})
      .then(response => {
        this.listcommentjson(response.data);
      })
  }
  listcommentjson(data){

    this.setState({ensemble:[],date:[],nblikes:[],id:[]});
    const valeur = JSON.parse(JSON.stringify(data));
    for (var value in valeur) {
        if(valeur[value].id_commentaire===this.props.idm)
          
            this.setState({ensemble:[...this.state.ensemble,valeur[value].texte], date:[...this.state.date,valeur[value].date],nblikes:[...this.state.nblikes,valeur[value].likes],id:[...this.state.id,value], id_source:[...this.state.id_source,valeur[value].id_source],login:[...this.state.login,valeur[value].login]});
    }
  }
  envoyerComment(event){
    event.preventDefault();
    axios.get("http://localhost:8080/Twister/messages/add",{params:{key:this.props.keyListmessage, txt:this.state.userInput,idmessagecommenter:this.props.idm}})
      .then(response => {
        this.commentjson(response.data);

      })
  }
  commentjson(data){
    const valeur = JSON.parse(JSON.stringify(data));
    this.setState({
        userInput: '',
        ensemble: [...this.state.ensemble, this.state.userInput],
        date: [...this.state.date,valeur.date],
        nblikes:[...this.state.nblikes,valeur.nblikes],
        id:[...this.state.id,valeur.id],
        id_source:[...this.state.id_source,valeur.id_source],
        login:[...this.state.login,valeur.login]
    });
  }
  
  onChange(event){
    this.setState({
            userInput: event.target.value
        });
  }
  renderlist(){
    return this.state.ensemble.map((item,index) => {
        return (
              <div className="messageindividuelle"  key={index}>
                <Message item={item} pere={this.props.pere} date={this.state.date[index]} nblikes={this.state.nblikes[index]} idm={this.state.id[index]} keyListmessage={this.props.keyprincipale} login={this.props.login} />
              </div>     
        );
    });
  }
  /*******************************************************/
  /*                       LIKE                          */
  /*******************************************************/
  like(event){
    event.preventDefault();
    axios.get("http://localhost:8080/Twister/messages/like",{params:{key:this.props.keyListmessage, idm:this.props.idm}})
    .then(response => {
      this.likejson(response.data);

    })
  }
  likejson(data){
    const valeur = JSON.parse(JSON.stringify(data));
    if(valeur.message!=undefined) {
      alert(valeur.Message)
    }
  }
  /*******************************************************/
  /*                  SUPPRIMER MESSAGE                  */
  /*******************************************************/
  supprimeMessage(event){
    event.preventDefault();
    axios.get("http://localhost:8080/Twister/friends/delete",{params:{key:this.props.keyListmessage,idm:this.props.idm}})
			.then(response => {
				this.likejson(response.data)
			}
		);
  }

  /*******************************************************/
  /*                       RENDER                        */
  /*******************************************************/
  render() {
    return (
      <div className="Msg">
        @{this.props.login}: <br></br>
        {this.props.item}<br></br>
        <button onClick={(event)=>this.supprimeMessage(event)}>X</button>
        <button onClick={(event)=>this.comments(event)}>{this.state.commentaire}</button> 
        <button onClick={(event)=>this.like(event)} class="like">
        
          <span> {this.props.nblikes} Like</span>
        </button>
       
        {this.commentaire()}
        
      </div>
    );
  }
}

export default Message;
      