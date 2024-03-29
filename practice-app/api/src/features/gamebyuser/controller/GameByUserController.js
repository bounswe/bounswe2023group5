import axios from "axios";
import EmptyFieldError from "../../../shared/errors/EmptyField.js";
import successfulResponse from "../../../shared/response/successfulResponse.js";
import ExternalApiError from "../../../shared/errors/ExternalApi.js";
import GameByUser from "../schema/gameByUserSchema.js";

class GameByUserController {
  async insertGames(req, res, next) {
    try {
      // get the steamid from the body of the request
      const { steamid, userEmail } = req.body;

      var minPlaytime = 0;
      var { minPlaytime } = req.body;

      //check whether the steamid or email field is empty. If it is, then give a empty field error. (You can check errors folder)
      if (!(steamid && userEmail)) {
        next(new EmptyFieldError());
        return;
      }

      // external api url
      // this external api returns the games based on the given steamid
      let url = `http://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=${process.env.STEAM_API_KEY}&steamid=${steamid}&format=json`

      // send a get request to external url and wait(by using await keyword) until the response is returned
      const response = await axios.get(url);

      // filters the games that will not be added to the database
      const filtered_result = response.data.response.games.filter(game => {
        const {
          playtime_windows_forever,
        } = game;
        return playtime_windows_forever > minPlaytime * 60;
      })

      // maps from response to related db fields
      const newGameByUser = new GameByUser({
        user_email: userEmail,
        games: filtered_result.map(game => ({
          game_id: game.appid,
          playtime: parseInt(game.playtime_forever / 60),
          playtime_on_windows: parseInt(game.playtime_windows_forever / 60),
        }))
      })

      // insert the values to mongodb database
      await newGameByUser.save();

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
