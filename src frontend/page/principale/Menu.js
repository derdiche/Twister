import React, { Component } from 'react';/*acces au propriètées de React et au Componenet*/
import '../../style/menu.css';
class Menu extends Component {
/*******************************************************/
/*                    constructeur                     */
/*******************************************************/
  constructor(props){
    super(props);
    }
    select(){
         return <hr></hr>
    }
  
  render(){
		return (
			<div className={this.props.estouvert() ?"menuprincipalo":"menuprincipalf"}>
                    <div onClick={()=>this.props.public()}>
                         <br></br>
                              <span className="clikable">PUBLIC</span>
                         <br></br>
                         <br></br>
                         {this.props.etat==="publique"? <hr className="select"></hr>:<div></div> }
                         </div>
                    <div onClick={()=>this.props.prive()}>
                         <br></br>
                              <span className="clikable">PRIVE</span>
                         <br></br>
                         <br></br>
                         {this.props.etat==="publique"? <div></div>:<hr className="select"></hr> }
				</div>	
			</div>

		);			
	}
}
export default Menu;//permet de pouvoir exporter 
