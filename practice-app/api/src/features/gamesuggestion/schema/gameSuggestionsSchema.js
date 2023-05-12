import mongoose from "mongoose";
const { Schema } = mongoose;

const gameSuggestionsSchema = new Schema(
  {
    user_email: {
      type: String,
      required: [true, "Email field is required!"],
    },
    enjoyed_games: {
      type: String,
      required: [true, "User prompt field is required!"],
    },
    preferred_game_type: {
      type: String,
      required: [true, "User prompt field is required!"],
    },
    ai_suggestion: {
      type: String,
      required: [true, "AI suggestion field is required!"],
    },
  },
  { timestamps: true }
);

const GameSuggestions = mongoose.model("gamesuggestions", gameSuggestionsSchema);

export default GameSuggestions;
