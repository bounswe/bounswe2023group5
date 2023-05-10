import mongoose from "mongoose";
const { Schema } = mongoose;

const topGamesSchema = new Schema(
  {
    user_email: {
        type: String,
        required: [true, "Email field is required"],
    },
    name: {
      type: String,
      required: [true, "Name field is required!"],
    },
    developer: {
      type: String,
      required: [true, "Developer field is required!"],
    },
    publisher: {
      type: String,
      required: [true, "Publisher field is required!"],
    },
    owners: {
      type: String,
      required: [true, "Owners field is required!"],
    },
    average_forever: {
      type: String,
      required: [true, "Average_forever field is required!"],
    },
    average_2weeks: {
      type: String,
      required: [true, "Average_2weeks field is required!"],
    },
    median_forever: {
      type: String,
      required: [true, "Median_forever field is required!"],
    },
    median_2weeks: {
      type: String,
      required: [true, "Median_2weeks field is required!"],
    },
    price: {
      type: String,
      required: [true, "Price field is required!"],
    },
    initialprice: {
      type: String,
      required: [true, "Initialprice field is required!"],
    },
    discount: {
      type: String,
      required: [true, "Discount field is required!"],
    },
    ccu: {
      type: String,
      required: [true, "Ccu field is required!"],
    },
  },
  { timestamps: true }
);

const TopGames = mongoose.model("topGames", topGamesSchema);

export default TopGames;
