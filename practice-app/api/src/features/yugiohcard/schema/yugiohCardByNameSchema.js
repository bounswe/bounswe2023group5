import mongoose from "mongoose";
const { Schema } = mongoose;

const yugiohCardByNameSchema = new Schema(
    {
        card_id: {
            type: String,
            required: [true, "CardID field is required!"],
        },
        card_name: {
            type: String,
            required: [true, "Card name field is required!"],
        },
        card_type: {
            type: String,
            required: [true, "Card type field is required!"],
        },
        user_email: {
            type: String,
            required: [true, "Email field is required!"],
        }
    },
    { timestamps: true }
);


const YugiohCardByName = mongoose.model("yugiohCardByName", yugiohCardByNameSchema);

export default YugiohCardByName;