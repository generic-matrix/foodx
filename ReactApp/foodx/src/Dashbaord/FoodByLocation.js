import React from 'react';
import Cookies from 'js-cookie';
import Grid from '@material-ui/core/Grid';
import FoodCard from './FoodCard';
import Util from '../Util/util';
import LinearProgress from '@material-ui/core/LinearProgress';
const ipLocation = require("iplocation");
class FoodByLocation extends React.Component{
    componentDidMount(){
        this.setState({
            "data":[],
            "page":"loading"
        })
        //get the gps coordinates by ip
        Util.GetIP().then((ip)=>{
            (async () => {
                let json=await ipLocation(ip)
                //load from the API
                Util.GetNearbyFoods(json.latitude,json.longitude,Cookies).then((data)=>{
                    this.setState({
                        "data":data,
                        "page":"loaded"
                    })
                }).catch((error)=>{
                    this.setState({
                        "data":[],
                        "page":"error"
                    })
                })
            })();
        }).catch((error)=>{
            //just show the error state 
            this.setState({
                "data":[],
                "page":"error"
            })
        })
    }

    GetFoodDetails(id){
        this.props.set_food_id(id)
    }
    render() {
        return(
            (this.state===null)?(
                <div />
            ):(
                this.state.page==="loading"?
                (
                    <div style={{"paddingLeft":"10%","paddingRight":"10%","paddingTop":"2%"}}>
                        <LinearProgress/>
                    </div>
                )
                :(
                    this.state.page==="loaded"?(
                        <div className="MuiTypography-root MuiTypography-body1">
                            <h2>People Around you have tried it</h2>
                            <p>The images are powered by Unsplash</p>
                            <br/>
                            <Grid container spacing={3}>
                            {this.state.data.map((obj, index) => (
                                    <Grid item xs onClick={()=>{this.GetFoodDetails(obj.id)}} key={index} >
                                        <FoodCard key={obj.id} title={obj.name} rating={obj.rating} review_count={obj.rating_count} />
                                    </Grid>
                                ))
                            }
                            </Grid>
                        </div>
                    ):(
                        <Grid item xs={12} style={{textAlign: "center"}}>
                            <h2>Bummer :( We ran into an internal error</h2>
                        </Grid>
                    )
                )
            )
        )
    }
}

export default FoodByLocation;