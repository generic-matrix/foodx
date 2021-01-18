import {React,useState} from 'react';
import CssBaseline from '@material-ui/core/CssBaseline';
import Link from '@material-ui/core/Link';
import Paper from '@material-ui/core/Paper';
import Box from '@material-ui/core/Box';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import SignIn from "./SignIn"
import SignUp from "./SignUp"
import UseStyles from './UseStyles.js'
function Copyright() {
  return (
    <Typography variant="body2" color="textSecondary" align="center">
      {'Copyright © '}
      <Link color="inherit" href="https://material-ui.com/">
        FoodX
      </Link>{' '}
      {new Date().getFullYear()}
      {'.'}
    </Typography>
  );
}

export default function SignComponent(props) {
  const classes = UseStyles();
  const [page, setPage] = useState("sign_in");
  return (
    <Grid container component="main" className={classes.root}>
      <CssBaseline />
      <Grid item xs={false} sm={4} md={7} className={classes.image} />
      <Grid item xs={12} sm={8} md={5} component={Paper} elevation={6} square>
      {
        page==="sign_in"?(<SignIn state_change={setPage} token={props.token} />):(<SignUp state_change={setPage} token={props.token} />)
      }
        <Box mt={5}>
          <Copyright />
        </Box>
      </Grid>
    </Grid>
  );
}