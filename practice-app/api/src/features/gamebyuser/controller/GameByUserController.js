import axios from "axios";
import EmptyFieldError from "../../../shared/errors/EmptyField.js";
import successfulResponse from "../../../shared/response/successfulResponse.js";
import ExternalApiError from "../../../shared/errors/ExternalApi.js";
import GameByUser from "../schema/gameByUserSchema.js";

class GameByUserController {
  async insertGames(req, res, next) {
    try {
      // get the category from the body of the request
      const { steamid, userEmail } = req.body;
      const key = "7CCE50D6D5D4929E15BF3108C3436CB0";

      //check whether the category or email field is empty. If it is, then give a empty field error. (You can check errors folder)
      if (!(steamid && userEmail)) {
        next(new EmptyFieldError());
        return;
      }

      // external api url
      // this external api returns the games based on the given category
      let url = `http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=${key}&steamid=${steamid}&format=json`

      // send a get request to external url and wait(by using await keyword) until the response is returned
      const get_result = await axios.get(url);

      // maps from response to database fields
      const insertedValues = get_result.data.response.games.map((game) => {
        const {
          appid,
          playtime_forever,
          playtime_windows_forever,
          playtime_mac_forever,
          playtime_linux_forever,
          rtime_last_played,
        } = game;
        return {
          game_id: appid,
          user_email: userEmail,
          playtime_forever,
          playtime_windows_forever,
          playtime_mac_forever,
          playtime_linux_forever,
          rtime_last_played,
        };
      });

      // insert the values to mongodb database
      await GameByUser.insertMany(insertedValues);

      // return a response with status code 201
      res
        .status(201)
        .json(
          successfulResponse("Games are inserted to database successfully")
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

  async getGamesByEmail(req, res, next) {
    try {
      //get the user email from query parameter
      const userEmail = req.query.userEmail;

      //check whether the email field is empty. If it is, then give a empty field error. (You can check errors folder)
      if (!userEmail) {
        next(new EmptyFieldError());
        return;
      }

      // retrieve the values based on the user email and sort them according to their created times.
      const games = await GameByUser.find(
        { user_email: userEmail },
        { _id: 0, __v: 0, updatedAt: 0 }
      ).sort({ createdAt: -1 });

      res.status(200).json(games);
    } catch (error) { }
  }
}

const gameByUserController = new GameByUserController();

export default gameByUserController;
