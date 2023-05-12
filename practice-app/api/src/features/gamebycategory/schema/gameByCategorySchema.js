import mongoose from "mongoose";
const { Schema } = mongoose;

const gameByCategorySchema = new Schema(
  {
    user_email: {
      type: String,
      required: [true, "Email field is required!"],
    },
    category: {
      type: String,
      required: [true, "Category field is required!"],
    },
    games: {
      type: [
        {
          game_id: {
            type: Number,
            required: [true, "GameId field is required!"],
          },
          name: {
            type: String,
            required: [true, "Name field is required!"],
          },
          short_description: {
            type: String,
            required: [true, "Short description field is required!"],
          },
          platform: {
            type: String,
            required: [true, "Platform field is required!"],
          },
          publisher: {
            type: String,
            required: [true, "Publisher field is required!"],
          },
          developer: {
            type: String,
            required: [true, "Developer field is required!"],
          },
          release_date: {
            type: String,
            required: [true, "Release date field is required!"],
          },
        },
      ],
      required: [true, "Games field is required!"],
    },
  },
  { timestamps: true }
);

const GameByCategory = mongoose.model("gameByCategory", gameByCategorySchema);

export default GameByCategory;
