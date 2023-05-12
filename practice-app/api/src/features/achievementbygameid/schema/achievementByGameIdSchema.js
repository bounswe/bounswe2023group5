import mongoose from "mongoose";
const {Schema} = mongoose

const achievementByGameIdSchema = new Schema({
    game_id: {
        type: Number,
        required: [true, "GameId field is required!"],
      },
    user_email: {
        type: String,
        required: [true, "Email field is required!"],
    },
    achievement_1:{
        name:{
            type: String,
            required: [true, "Name field is required!"],
        },
        success_rate:{
            type: Number,
            required: [true, "Success Rate field is required!"],
        }
    },
    achievement_2:{
        name:{
            type: String,
            required: [true, "Name field is required!"],
        },
        success_rate:{
            type: Number,
            required: [true, "Success Rate field is required!"],
        }
    },
    achievement_3:{
        name:{
            type: String,
            required: [true, "Name field is required!"],
        },
        success_rate:{
            type: Number,
            required: [true, "Success Rate field is required!"],
        }
    },
    game_name:{
        type:String,
        required: [true, "Game name field is required!"],
    }
},
{ timestamps: true }
)

const AchievementByGameId = mongoose.model("achievementsByGameId", achievementByGameIdSchema);

export default AchievementByGameId;
