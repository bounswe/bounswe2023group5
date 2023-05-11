import mongoose from "mongoose";
const { Schema } = mongoose;

const gameByUserSchema = new Schema(
  {
    game_id: {
      type: Number,
      required: [true, "GameId field is required!"],
    },
    user_email: {
      type: String,
      required: [true, "Email field is required!"],
    },
    playtime: {
      type: Number,
      required: [true, "Playtime field is required!"],
    },
    playtime_on_windows: {
      type: Number,
      required: [true, "Short description field is required!"],
    },
  },
  { timestamps: true }
);

const GameByUser = mongoose.model("gameByUser", gameByUserSchema);

export default GameByUser;
