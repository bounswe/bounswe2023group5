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
    playtime_forever: {
      type: Number,
      required: [true, "Playtime field is required!"],
    },
    playtime_windows_forever: {
      type: Number,
      required: [true, "Short description field is required!"],
    },
    playtime_mac_forever: {
      type: Number,
      required: [true, "Genre field is required!"],
    },
    playtime_linux_forever: {
      type: Number,
      required: [true, "Platform field is required!"],
    },
    rtime_last_played: {
      type: Number,
      required: [true, "Publisher field is required!"],
    },
  },
  { timestamps: true }
);

const GameByUser = mongoose.model("gameByUser", gameByUserSchema);

export default GameByUser;
