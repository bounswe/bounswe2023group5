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
    type: {
      type: String,
      required: [true, "Type field is required!"],
    },
    rarity: {
      type: String,
      required: [true, "Rarity field is required!"],
    },
    cost: {
      type: Number,
      required: [true, "Cost field is required!"],
    },
    attack_point: {
      type: Number,
      required: [true, "Attack Point field is required!"],
    },
    health_point: {
      type: Number,
      required: [true, "Health Point field is required!"],
    },
    race: {
      type: String,
      required: [true, "Race field is required!"],
    },
    card_image: {
        type: String,
        required: [true, "Image field is required!"],
    },
  },
  { timestamps: true }
);

const CardByName = mongoose.model("cardByName", cardByNameSchema);

export default CardByName;
