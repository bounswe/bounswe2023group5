import mongoose from "mongoose";
const { Schema } = mongoose;

const rockPaperScissorsSchema = new Schema(
  {
    user_email: {
        type: String,
        required: [true, "Email field is required!"],
      
    },
    user_choice: {
        type: String,
        required: [true, "User choice field is required!"],
    },
    user_beats: {
        type: String,
        required: [true, "User beats field is required!"],
    },
    ai_choice: {
        type: String,
        required: [true, "AI choice field is required!"],
    },
    ai_beats: {
        type: String,
        required: [true, "AI beats field is required!"],
    },
    result: {
        type: String,
        required: [true, "Result field is required!"],
    }
  },
  { timestamps: true }
);

const RockPaperScissors = mongoose.model("rockPaperScissors", rockPaperScissorsSchema);

export default RockPaperScissors;