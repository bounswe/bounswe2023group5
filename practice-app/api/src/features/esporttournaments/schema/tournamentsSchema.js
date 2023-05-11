import mongoose from "mongoose";
const { Schema } = mongoose;

const tournamentsSchema = new Schema(
  {
    user_email: {
      type: String,
      required: [true, "Email field is required!"],
    },
    id: {
      type: Number,
      required: [true, "Id field is required"],
    },
    name: {
      type: String,
      required: [true, "Name field is required!"],
    },
    flag: {
      type: String,
      required: [true, "Flag field is required!"],
    }
  },
  { timestamps: true }
);

const tournaments = mongoose.model("tournaments", tournamentsSchema);

export default tournaments;