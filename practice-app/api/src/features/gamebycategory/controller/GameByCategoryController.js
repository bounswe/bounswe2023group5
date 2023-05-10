import axios from "axios";
import EmptyFieldError from "../../../shared/errors/EmptyField.js";
import successfulResponse from "../../../shared/response/successfulResponse.js";
import ExternalApiError from "../../../shared/errors/ExternalApi.js";
import GameByCategory from "../schema/gameByCategorySchema.js";

class GameByCategoryController {
  async insertGames(req, res, next) {
    try {
      // get the category from the body of the request
      const { category, userEmail } = req.body;

      //check whether the category or email field is empty. If it is, then give a empty field error. (You can check errors folder)
      if (!(category && userEmail)) {
        next(new EmptyFieldError());
        return;
      }

      // external api url
      // this external api returns the games based on the given category
      let url = `https://www.freetogame.com/api/games?category=${category}`;

      // send a get request to external url and wait(by using await keyword) until the response is returned
      const response = await axios.get(url);

      // maps from response to database fields
      const insertedValues = response.data.map((game) => {
        const {
          id,
          title,
          thumbnail,
          short_description,
          platform,
          publisher,
          developer,
          release_date,
        } = game;
        return {
          game_id: id,
          user_email: userEmail,
          name: title,
          thumbnail,
          short_description,
          platform,
          publisher,
          developer,
          release_date,
        };
      });

      // insert the values to mongodb database
      await GameByCategory.insertMany(insertedValues);

      // return a response with status code 200
      res
        .status(201)
        .json(
          successfulResponse("Games are inserted to database successfully")
        );
    } catch (error) {
      console.log(error);
      // if this condition is true, then it means that external api is returned an error.
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
      const games = await GameByCategory.find(
        { user_email: userEmail },
        { _id: 0, __v: 0, updatedAt: 0 }
      ).sort({ createdAt: -1 });

      res.status(200).json(games);
    } catch (error) {}
  }
}

const gameByCategoryController = new GameByCategoryController();

export default gameByCategoryController;
