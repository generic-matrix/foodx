import React from 'react';
import {useState} from 'react';
import PropTypes from 'prop-types';
import { makeStyles } from '@material-ui/core/styles';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Typography from '@material-ui/core/Typography';
import Box from '@material-ui/core/Box';
import Chip from '@material-ui/core/Chip';
import Util from '../Util/util';
import Cookies from 'js-cookie';
import LinearProgress from '@material-ui/core/LinearProgress';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import Avatar from '@material-ui/core/Avatar';
import Rating from '@material-ui/lab/Rating';
import CardContent from '@material-ui/core/CardContent';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import Snackbar from '@material-ui/core/Snackbar';
function TabPanel(props) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`simple-tabpanel-${index}`}
      aria-labelledby={`simple-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box p={3}>
          <Typography>{children}</Typography>
        </Box>
      )}
    </div>
  );
}

TabPanel.propTypes = {
  children: PropTypes.node,
  index: PropTypes.any.isRequired,
  value: PropTypes.any.isRequired,
};

function a11yProps(index) {
  return {
    id: `simple-tab-${index}`,
    'aria-controls': `simple-tabpanel-${index}`,
  };
}

const useStyles = makeStyles((theme) => ({
  root: {
    flexGrow: 1,
    backgroundColor: theme.palette.background.paper,
  },
}));

export default function FoodDetails(props) {
  const classes = useStyles();
  const [value, setValue] = React.useState(0);
  const [loading, setLoading] = React.useState(true);
  const [data, setData] = React.useState({});
  const [feedback_msg, setFeedbackMsg] = React.useState("");
  const [feedback_star, setFeedbackStar] = React.useState(5);
  const [message, setMessage] = useState(null);
  const [open, setOpen] = useState(false);
  if(loading===true){
      //props.food_id
    Util.GetFoodDetails(props.food_id,Cookies).then((result)=>{
        setLoading(false)
        setData(result)
    }).catch((error)=>{
        setLoading(false)
    })
  }
  const handleChange = (event, newValue) => {
    setValue(newValue);
  };

  return (
    (loading!==true)?  
    (<div>
        {
            (message!==null)?(
                <Snackbar
                    anchorOrigin={{
                        vertical: 'bottom',
                        horizontal: 'left',
                    }}
                    open={open}
                    autoHideDuration={5}
                    onClick={()=>{setOpen(false)}}
                    message={message}
                />
            ):(<div/>)
        }
        <Box display="flex" justifyContent="center" m={1} p={1} >
            <Chip
                label={"Go back"}
                color="secondary"
                onClick={()=>{props.set_food_id(null)}}
            />
        </Box>
        <div className={classes.root} style={{"backgroundColor":"white"}}>
        <Tabs value={value} onChange={handleChange} centered>
            <Tab label="Ingrediends" {...a11yProps(0)} />
            <Tab label="Steps to Cook" {...a11yProps(1)} />
            <Tab label="Rating" {...a11yProps(3)} />
            <Tab label="Add A Rating" {...a11yProps(4)} />
            </Tabs>
        <TabPanel value={value} index={0} style={{"padding":"2%"}}>
            {
                (data.ingredients===undefined)?(
                    <div />
                ):(
                    
                    data.ingredients.map((obj, index) => (
                        (index===0)?(
                                <div>
                                    <h2>{data.title}</h2><br/>
                                    <div key={index}><p>{obj}</p><br/></div>
                                </div>
                            ):(
                                <div key={index}><p>{obj}</p><br/></div>
                            )
                        )
                    )
                )
            }
        </TabPanel>
        <TabPanel value={value} index={1} style={{"padding":"2%"}}>
            {
                (data.instructions===undefined)?(
                    <div />
                ):(
                    data.instructions.map((obj, index) => (
                            <div key={index}><p>{obj}</p><br/></div> 
                        )
                    )
                )
            }
        </TabPanel>
        <TabPanel value={value} index={2} style={{"padding":"2%"}}>
            {
                (data.rating===undefined || data.rating===null)?(
                    <div>
                        <p>Sorry ! NO reviews found for this food , you may write one heading over to the next section</p>
                    </div>
                ):(
                    data.rating.map((obj, index) => (
                        <div style={{"padding":"10px"}}>
                            <Card className={classes.root}>
                                <CardHeader
                                    avatar={
                                    <Avatar aria-label="recipe" className={classes.avatar}>
                                        {obj.user_name[0]}
                                    </Avatar>
                                    }
                                    title={obj.user_name}
                                    subheader={obj.message}
                                />
                                <CardContent>
                                <Box display="flex">
                                    <Rating name="read-only" value={parseFloat(obj.star)} readOnly />
                                </Box>
                                </CardContent>
                            </Card>
                        </div>
                        )
                    )
                )
            }
        </TabPanel>
        <TabPanel value={value} index={3} style={{"padding":"2%"}}>
            <h2>Do add a review it allows us to improve ! </h2><br />
            <TextField style={{"width":"100%"}}
                placeholder="Add a review "
                multiline
                rows={2}
                rowsMax={4}
                label={feedback_msg}
                onChange={(event)=>{setFeedbackMsg(event.target.value)}} 
            />
            <br/><br/>
            <Rating value={feedback_star} onChange={(event)=>{setFeedbackStar(event.target.value)}}  />
            <br/><br/>
            <Button variant="outlined" color="secondary" onClick={()=>{
                  //setMessage,setOpen
                  Util.AddRating(data.id,"test user",feedback_msg,feedback_star,Cookies).then((result)=>{
                    setMessage("Review Added !")
                    setOpen(true)
                  }).catch((error)=>{
                    setMessage(error)
                    setOpen(true)
                  })
            }}>
                Submit
            </Button>
        </TabPanel>
        </div>
    </div>):(
        <div style={{"paddingLeft":"10%","paddingRight":"10%","paddingTop":"2%"}}>
            <LinearProgress />
        </div>
    )
  );
}