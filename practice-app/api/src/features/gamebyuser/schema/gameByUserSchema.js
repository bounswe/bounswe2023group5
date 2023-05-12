import mongoose from "mongoose";
const { Schema } = mongoose;

const gameByUserSchema = new Schema(
  {
    user_email: {
      type: String,
      required: [true, "Email field is required!"],
    },
    games: [
      {
        game_id: Number,
        playtime: Number,
        playtime_on_windows: Number
      }
    ]
  },
  { timestamps: true }
);

const GameByUser = mongoose.model("gameByUser", gameByUserSchema);

export default GameByUser;
