import mongoose from "mongoose";
const { Schema } = mongoose;

const gameByGenreSchema = new Schema(
    {
        genre_id: {
            type: String,
            required: [true, "CardID field is required!"],
        },
        genre_name: {
            type: String,
            required: [true, "Card name field is required!"],
        },
        games_count: {
            type: String,
            required: [true, "Card type field is required!"],
        },
        image_background: {
            type: String,
            required: [true, "Card type field is required!"],
        },
        game_description: {
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


const gameByGenre = mongoose.model("gamebygenres", gameByGenreSchema);

export default gameByGenre;