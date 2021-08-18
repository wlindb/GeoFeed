const express = require('express')
const app = express()
require("dotenv").config(); // for loading environment variables
const mongoose = require("mongoose");

const port = 8080
const Post = require("./models/Post");


app.use(express.json());

const MONGO_URI = process.env.MONGO_URI;
//console.log(MONGO_URI)
mongoose
   .connect(MONGO_URI, { useNewUrlParser: true, useUnifiedTopology: true })
   .then(() => console.log("Mongo Connection successful"))
   .catch(err => console.log("err in mongoose connection", err));

mongoose.set("useFindAndModify", false);
mongoose.Promise = global.Promise;

let mockData = [
    {
        uuid: "E2E2E2E2E2E2E2E2",
        location: "Stockholm",
        district: "Östermalm",
        body: "This is the actual post ",
        timestamp: Date.now().toString(),
        comments: []
    },
    {
        uuid: "E2E2E2E2E2E2E2E2",
        location: "Stockholm",
        district: "Östermalm",
        body: "This is the actual post ",
        timestamp: Date.now().toString(),
        comments: []
    },
    {
        uuid: "E2E2E2E2E2E2E2E2",
        location: "Stockholm",
        district: "Vasastan",
        body: "This is the actual post ",
        timestamp: Date.now(),
        comments: []
    },
    {
        uuid: "E2E2E2E2E2E2E2E2",
        location: "Örebro",
        district: "Väster",
        body: "This is the actual post ",
        timestamp: Date.now().toString(),
        comments: []
    },
    {
        uuid: "E2E2E2E2E2E2E2E2",
        location: "Örebro",
        district: "Norr",
        body: "This is the actual post ",
        timestamp: Date.now().toString(),
        comments: []
    },
    {
        uuid: "E2E2E2E2E2E2E2E2",
        location: "Örebro",
        district: "Adolfsberg",
        body: "This is the actual post ",
        timestamp: Date.now(),
        comments: []
    },
    {
        uuid: "E2E2E2E2E2E2E2E2",
        location: "Västerås",
        district: "Malmaberg",
        body: "This is the actual post ",
        timestamp: Date.now().toString(),
        comments: []
    }
];

app.get('/', (req, res) => {
  res.send('Hello World!')
})

app.get('/posts/:location', (req, res) => {
    const { location } = req.params;
    const posts = getByLocation(location);
    Post.find({location: location})
        .then(posts => {
            console.log(posts);
            res.status(200).json({posts})
        })
        .catch(err => {
            console.error(err)
            res.status(500).json({msg: "error gettings posts from database"})
        })
});

app.post('/post', (req, res) => {
    // const { uuid, location, district, body, timestamp, comments } = req.body.post;
    const newPost = req.body.post
    const newDbPost = new Post(newPost);
    newDbPost.save()
        .then(post => {
            res.status(200).json({post: post})
        })
        .catch(err => {
            console.error(err)
            res.status(500).json({msg: "error adding post to database"})
        })
    //mockData.push(newPost)
    //res.status(200).json({post: newPost})
});

app.get('/post', (req, res) => {
    res.status(200).json(mockData[0])
});

app.listen(port, () => {
  console.log(`Server listening at http://localhost:${port}`)
})

const getByLocation = location => mockData.filter(post => post.location.toUpperCase() === location.toUpperCase());