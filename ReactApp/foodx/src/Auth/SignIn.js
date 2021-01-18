import EmojiFoodBeverageIcon from '@material-ui/icons/EmojiFoodBeverage';
import TextField from '@material-ui/core/TextField';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import UseStyles from './UseStyles.js'
import Grid from '@material-ui/core/Grid';
import Link from '@material-ui/core/Link';
import {React,useState} from 'react';
import Snackbar from '@material-ui/core/Snackbar';
import Util from '../Util/util.js';
function Signin(props){
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [open, setOpen] = useState(false);
    const [message, setMessage] = useState("Feilds are missing");
    const classes = UseStyles();

    const SubmitSignIn=()=>{
        if(email==="" || password===""){
            setMessage("Feilds are missing");
            setOpen(true);
        }else{
            //call the API
            setMessage("Validating...");
            setOpen(true);
            Util.Login(email,password).then((token)=>{
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
            Sign in to access 35K+ recipies
          </Typography>
          <form className={classes.form} noValidate  onSubmit={(event)=>{event.preventDefault();SubmitSignIn();}}>
            <TextField
              variant="outlined"
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              name={email}
              autoComplete="email"
              onChange={(event)=>{setEmail(event.target.value)}}
              autoFocus
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
              onChange={(event)=>{setPassword(event.target.value)}}
              autoComplete="current-password"
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              color="primary"
              className={classes.submit}
            >
              Sign In
            </Button>
            <Grid container>
              <Grid item>
                <Link href="#" variant="body2" onClick={()=>{props.state_change('sign_up')}}>
                  {"Don't have an account? Sign Up"}
                </Link>
              </Grid>
            </Grid>
          </form>
        </div>
    )
}

export default Signin