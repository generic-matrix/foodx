
import EmojiFoodBeverageIcon from '@material-ui/icons/EmojiFoodBeverage';
import {React,useState} from 'react';
import TextField from '@material-ui/core/TextField';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import UseStyles from './UseStyles.js'
import Grid from '@material-ui/core/Grid';
import Link from '@material-ui/core/Link';
import Snackbar from '@material-ui/core/Snackbar';
import Util from '../Util/util.js';
function Signin(props){
    const [name, setName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [r_password, setRPassword] = useState("");
    const [open, setOpen] = useState(false);
    const [message, setMessage] = useState("Feilds are missing");

    const classes = UseStyles();

    const SubmitSignUp=()=>{
      if(email==="" || password==="" || name===""){
          setMessage("Feilds are missing");
          setOpen(true);
      }else if(password!==r_password){
        setMessage("The passwords are not matching");
        setOpen(true);
      }
      else{
            //call the API
            setMessage("Validating...");
            setOpen(true);
            Util.SignUp(name,email,password).then((token)=>{
                setOpen(false);
                props.token(token);
            }).catch((err)=>{
                setMessage(err);
                setOpen(true);
            });
        }
    }

    return (
        <div className={classes.paper}>
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
          <Avatar className={classes.avatar}>
            <EmojiFoodBeverageIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Welcome Dear ! Access 35K+ Foods Recepies just in one go
          </Typography>
          <form className={classes.form} noValidate onSubmit={(event)=>{event.preventDefault();SubmitSignUp();}}>

          <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              id="text"
              label="Your Name"
              name={name}
              autoComplete="Name"
              onChange={(event)=>{setName(event.target.value)}}
              autoFocus
            />

            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              name={email}
              autoComplete="Email"
              onChange={(event)=>{setEmail(event.target.value)}}
            />
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name={password}
              label="Password"
              type="password"
              id="password"
              autoComplete="Password"
              onChange={(event)=>{setPassword(event.target.value)}}
            />

            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              name={r_password}
              label="Retype Password"
              type="password"
              id="r_password"
              autoComplete="Retype Password"
              onChange={(event)=>{setRPassword(event.target.value)}}
            />

            <Button
              type="submit"
              fullWidth
              variant="contained"
              color="primary"
              className={classes.submit}
            >
              Sign Up
            </Button>
            <Grid container>
              <Grid item>
                <Link href="#" variant="body2" onClick={()=>{props.state_change('sign_in')}}>
                  {"Login with existing account? Sign In"}
                </Link>
              </Grid>
            </Grid>
          </form>
        </div>
    )
}

export default Signin