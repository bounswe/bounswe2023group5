import mongoose from "mongoose";
const { Schema } = mongoose;

const gameByCategorySchema = new Schema(
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
        review : String, 
        publish_date: String
    }],
  },
  { timestamps: true }
);

const GameReview = mongoose.model("gameReview", gameReviewSchema);

export default GameReview;
