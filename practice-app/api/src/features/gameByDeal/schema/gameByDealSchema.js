import mongoose from "mongoose";
const { Schema } = mongoose;

const gameByDealSchema = new Schema(
  {
    userEmail: {
      type: String,
      required: [true, "Email field is required!"],
    },
    title: {
      type: String,
      required: [true, "Title field is required!"],
    },
    upperPrice: {
      type: Number,
      required: false,
    },
    deals: [
        {
            title: String,
            salePrice: String,
            normalPrice: String,
            steamRatingText: String,
            rating: String,
            isOnSale: Boolean
        }
    ]
  },
  { timestamps: true }
);

const GameByDeal = mongoose.model("gamebydeal", gameByDealSchema);

export default GameByDeal;
