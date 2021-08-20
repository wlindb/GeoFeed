const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const commentModel = {
    uuid: {
       type: String,
       required: true
    },
    comment: {
       type: String,
       required: true
    },
    district: {
       type: String,
       required: true
    },
    location: {
       type: String,
       required: true
    },
    timestamp: {
       type: String,
       required: true
    }
 };

const commentSchema = new Schema(commentModel);

const postModel = {
   uuid: {
      type: String,
      required: true
   },
   location: {
      type: String,
      required: true
   },
   district: {
      type: String,
      required: true
   },
   body: {
      type: String,
      required: true
   },
   timestamp: {
      type: String,
      required: true
   },
   comments: {
      type: [commentSchema],
      required: true
   }
};

// {
//     uuid: "E2E2E2E2E2E2E2E2",
//     location: "Stockholm",
//     district: "Östermalm",
//     body: "This is the actual post ",
//     timestamp: Date.now().toString(),
//     comments: []
// }

const PostSchema = new Schema(postModel);
module.exports = mongoose.model("posts", PostSchema);