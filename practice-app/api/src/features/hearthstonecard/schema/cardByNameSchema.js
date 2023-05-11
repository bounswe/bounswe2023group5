import mongoose from "mongoose";
const { Schema } = mongoose;

const cardByNameSchema = new Schema(
  {
    card_id: {
      type: String,
      required: [true, "CardID field is required!"],
    },
    card_name: {
      type: String,
      required: [true, "Card name field is required!"],
    },
    card_set: {
      type: String,
      required: [true, "Card set field is required!"],
    },
    user_email: {
      type: String,
      required: [true, "Email field is required!"],
    }
  },
  { timestamps: true }
);

const CardByName = mongoose.model("cardByName", cardByNameSchema);

export default CardByName;
