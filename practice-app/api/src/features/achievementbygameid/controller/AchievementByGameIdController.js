import axios from "axios";
import EmptyFieldError from "../../../shared/errors/EmptyField.js";
import successfulResponse from "../../../shared/response/successfulResponse.js";
import ExternalApiError from "../../../shared/errors/ExternalApi.js";
import AchievementByGameId from "../schema/AchievementByGameIdSchema.js"


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




}