import mongoose from "mongoose";
const { Schema } = mongoose;

const gameByDealSchema = new Schema(
  {
    user_email: {
      type: String,
      required: [true, "Email field is required!"],
    },
    title: {
      type: String,
      required: [true, "Title field is required!"],
    },
    upper_price: {
      type: Number,
      required: false,
    },
    deals: [
        {
            title: String,
            sale_price: String,
            normal_price: String,
            steam_rating_text: String,
            rating: String,
            on_sale: Boolean
        }
    ]
  },
  { timestamps: true }
);

const GameByDeal = mongoose.model("gamebydeal", gameByDealSchema);

export default GameByDeal;
