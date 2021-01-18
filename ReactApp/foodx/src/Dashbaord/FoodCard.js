import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Typography from '@material-ui/core/Typography';
import Rating from '@material-ui/lab/Rating';
import Chip from '@material-ui/core/Chip';
import Box from '@material-ui/core/Box';
import Util from '../Util/util';

const useStyles = makeStyles({
  root: {
    maxWidth: 345,
  },
  media: {
    height: 140,
  },
});

export default function FoodCard(props) {
  const classes = useStyles();
  return (
    <Card className={classes.root}>
      <CardActionArea>
        <CardMedia
          className={classes.media}
          image={"https://source.unsplash.com/featured/?"+Util.GetNouns(props.title)}
          title={props.title}
        />
        <CardContent>
          <Typography gutterBottom variant="h5" component="h2">
          {props.title}
          </Typography>
          <Typography variant="body2" color="textSecondary" component="p">
          <Box display="flex">
            <Rating name="read-only" value={parseFloat(props.rating)} readOnly />
            <Chip size="small" label={props.review_count+" reviews"}/>
          </Box>
          </Typography>
        </CardContent>
      </CardActionArea>
    </Card>
  );
}