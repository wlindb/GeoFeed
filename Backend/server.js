const express = require('express')
const app = express()
const port = 8080

app.use(express.json());

const mockData = [
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
    res.status(200).json({posts})
});

app.post('post', (req, res) => {
    const { uuid, location, district, body, timestamp, comments } = req.body;
    res.status(200).json({msg: "Success"})
});

app.get('/post', (req, res) => {
    res.status(200).json(mockData[0])
});

app.listen(port, () => {
  console.log(`Server listening at http://localhost:${port}`)
})

const getByLocation = location => mockData.filter(post => post.location.toUpperCase() === location.toUpperCase());