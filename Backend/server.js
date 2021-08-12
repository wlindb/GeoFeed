const express = require('express')
const app = express()
const port = 3000

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
        district: "Östermalm",
        body: "This is the actual post ",
        timestamp: Date.now(),
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
        timestamp: Date.now(),
        comments: []
    },
    {
        uuid: "E2E2E2E2E2E2E2E2",
        location: "Stockholm",
        district: "Östermalm",
        body: "This is the actual post ",
        timestamp: Date.now().toString(),
        comments: []
    }
];

app.get('/', (req, res) => {
  res.send('Hello World!')
})

app.get('/posts', (req, res) => {
    res.status(200).json({posts: mockData})
});

app.get('/post', (req, res) => {
    res.status(200).json(mockData[0])
});

app.listen(port, () => {
  console.log(`Example app listening at http://localhost:${port}`)
})