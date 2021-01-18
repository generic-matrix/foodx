import React from "react";
import Cookies from 'js-cookie';
import SignComponent from './Auth/SignComponent';
import Dashboard from './Dashbaord/Dashboard';
class Controller extends React.Component {
    //Check if Cookies are present if yes go to dashboard else Auth
    //Must render based on the state 
    state={
        token: Cookies.get('token')
    };

    SetToken=(new_val)=>{
        Cookies.set('token',new_val);
        this.setState({
            token:new_val
        })
    }


    Logout=()=>{
        Cookies.remove('token');
        this.setState({
            token:undefined
        })
    }

    render(){
        if(this.state===null){
            return <div/>
        }else{
            if(this.state.token!==undefined){
                //to Dashboard
                return <Dashboard logout={this.Logout}/>
            }else{
                //to Auth page
                return <SignComponent token={this.SetToken}/>
            }
        }
    }
}

export default Controller