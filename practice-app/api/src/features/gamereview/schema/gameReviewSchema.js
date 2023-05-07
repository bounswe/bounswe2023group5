import mongoose from "mongoose";
const { Schema } = mongoose;

const gameReviewSchema = new Schema(
  {
    user_email: {
        type: String,
        required: [true, "Email field is required!"],
    },
    game_name: {
        type: String,
        required: [true, "GameName field is required"]
    },
    game_id: {
      type: Number,
      required: [true, "GameId field is required!"],
    },
    reviews: [{
        review_text : String, 
        publish_date: String
    }],
  },
  { timestamps: true }
);

const GameReview = mongoose.model("gameReview", gameReviewSchema);

export default GameReview;
