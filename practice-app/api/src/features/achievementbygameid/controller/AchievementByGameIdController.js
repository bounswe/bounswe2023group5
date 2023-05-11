import axios from "axios";
import EmptyFieldError from "../../../shared/errors/EmptyField.js";
import successfulResponse from "../../../shared/response/successfulResponse.js";
import ExternalApiError from "../../../shared/errors/ExternalApi.js";
import AchievementByGameId from "../schema/achievementByGameIdSchema.js";


class AchievementByGameIdController {
  async insertAchievements(req, res, next) {
    try {
      // get the gameid from the body of the request
      const { gameid, userEmail } = req.body;

      //check whether the gameid or email field is empty. If it is, then give a empty field error. (You can check errors folder)
      if (!(gameid && userEmail)) {
        next(new EmptyFieldError());
        return;
      }

      // external api url
      // this external api returns the achievements based on the given game
      let url = `http://api.steampowered.com/ISteamUserStats/GetGlobalAchievementPercentagesForApp/v0002/?gameid=${gameid}`;

      // send a get request to external url and wait(by using await keyword) until the response is returned
      const response = await axios.get(url);

      //get achievements from response
      const achievements = response.data.achievementpercentages.achievements;
      // take least completed 3 achievements
      const leastCompletedAchievements = achievements.slice(-3);
      //the data that will be sent to the database is stored inside the insertedValues variable
      
      const insertedValues =  {
          game_id: gameid,
          user_email: userEmail,
          achievement_1:{
            name: leastCompletedAchievements[2].name,
            success_rate: leastCompletedAchievements[2].percent,
          },
          achievement_2:{
            name: leastCompletedAchievements[1].name,
            success_rate: leastCompletedAchievements[1].percent,
          },
          achievement_3:{
            name: leastCompletedAchievements[0].name,
            success_rate: leastCompletedAchievements[0].percent,
          }
      }

      //insert the values to the database and wait until they are all inserted
      await AchievementByGameId.insertMany(insertedValues);

      // return a response with status code 201
      res
        .status(201)
        .json(
          successfulResponse("Achievements are inserted to database successfully")
        );
    } catch (error) {
      console.log(error);

      // if this condition is true, then it means that external api has returned an error.
      if (error.response) {
        if (error.response.data.status_message) {
          next(new ExternalApiError(error.response.data.status_message));
          return;
        }
      }
      next(error);
    }
  }

  
  async getAchievementsByEmail(req, res, next) {
    try {
      //get the user email from query parameter
      const userEmail = req.query.userEmail;

      //check whether the email field is empty. If it is, then give a empty field error. (You can check errors folder)
      if (!userEmail) {
        next(new EmptyFieldError());
        return;
      }

      // retrieve the values based on the user email and sort them according to their created times.
      const achievements= await AchievementByGameId.find(
        { user_email: userEmail },
        { _id: 0, __v: 0, updatedAt: 0 }
      ).sort({ createdAt: -1 });

      res.status(200).json(achievements);
    } catch (error) {}
  }
  
}

const achievementByGameIdController = new AchievementByGameIdController();

export default achievementByGameIdController;
