import {React,useState} from 'react';
import TextField from '@material-ui/core/TextField';
import Autocomplete from '@material-ui/lab/Autocomplete';
import Util from '../Util/util.js';
import Cookies from 'js-cookie';
import Grid from '@material-ui/core/Grid';
import { Button } from '@material-ui/core';
import FoodByLocation from './FoodByLocation.js'
import FoodDetails from '../FoodDetails/FoodDetails'
function Dashboard(props) {
  const [results, setResults] = useState([]);
  const [default_message, setMessage] = useState("Yes ! Type in to get suggesstions");
  const [specific_food_id, set_specific_food_id] = useState(null);
  const OnSelectOption=(value)=>{
    let food_id=Util.GetFoodId(results,value);
    if(food_id!=null){
        set_specific_food_id(food_id)
    }
  }

  const AutoComplete=(query)=>{
    if(query.length>0){
      setMessage("Nothing found as searched")
      var res=[];
      //call the api to Autocomplete and append to results
      Util.AutoComplete(query,Cookies).then((arr)=>{
        arr.forEach(element => {
          res.push(element)
        });
        setResults(res)
      }).catch((error)=>{
        console.log(error);
      })
    }else{
      setMessage("Yes ! Type in to get suggesstions")
    }
  }



  return (
    <div>
      {
        (specific_food_id===null)?(
          <Grid container justify = "center" style={{"paddingTop":"20px"}}>
            <Autocomplete style={{"width":"80%"}}
              id="autocomplete"
              options={results}
              getOptionLabel={(option) => option.name}
              renderInput={(params) => <TextField {...params} label="What do you want to make today ?" onChange={(event)=>{AutoComplete(event.target.value)}}  placeholder="Search for terms like Panckaes , maggie etc.." variant="outlined" />}
              onSelect={(val)=>OnSelectOption(val.target.value)}
              noOptionsText={default_message}
            />
            <Button onClick={()=>{props.logout()}}>&nbsp;Logout</Button>
          </Grid>
        ):(<div/>)
      }
      <br/>
      {
        (specific_food_id!==null)?(<FoodDetails set_food_id={set_specific_food_id} food_id={specific_food_id} />):(<FoodByLocation set_food_id={set_specific_food_id}/>)
      }
    </div>
  );
}


export default Dashboard

