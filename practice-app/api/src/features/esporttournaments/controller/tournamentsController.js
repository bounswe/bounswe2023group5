import axios from "axios";
import EmptyFieldError from "../../../shared/errors/EmptyField.js";
import successfulResponse from "../../../shared/response/successfulResponse.js";
import ExternalApiError from "../../../shared/errors/ExternalApi.js";
import Tournaments from "../schema/tournamentsSchema.js";

class TournamentsController {
  async insertTournaments(req, res, next) {
    try {
      const {userEmail } = req.body;

      //check whether the category or email field is empty. If it is, then give a empty field error. (You can check errors folder)
      if (!(userEmail)) {
        next(new EmptyFieldError());
        return;
      }

      // external api url
      // this external api returns the games based on the given category
      let url = 'https://esportapi1.p.rapidapi.com/api/esport/tournament/categories';

      // send a get request to external url and wait(by using await keyword) until the response is returned
      const response = await axios.get(url,{headers:{
        'X-RapidAPI-Key': process.env.apikey,
        'X-RapidAPI-Host': 'esportapi1.p.rapidapi.com'}
      });      
      // maps from response to database fields
      const categories = response.data.categories.map((category) => {
        const {
          flag,
          id,
          name
        } = category;
        return {
          user_email: userEmail,
          flag: flag,
          id: id,
          name: name
        };
      });
      // insert the values to mongodb database
      await Tournaments.insertMany(categories);
      
      // return a response with status code 201
      res
        .status(201)
        .json(
          successfulResponse("tournaments are inserted to database successfully")
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

  async getTournamentsByEmail(req, res, next) {
    try {
      //get the user email from query parameter
      const userEmail = req.query.userEmail;

      //check whether the email field is empty. If it is, then give a empty field error. (You can check errors folder)
      if (!userEmail) {
        next(new EmptyFieldError());
        return;
      }

      // retrieve the values based on the user email and sort them according to their created times.
      const tournaments = await Tournaments.find(
        { user_email: userEmail },
        { _id: 0, __v: 0, updatedAt: 0 }
      ).sort({ createdAt: -1 });

      res.status(200).json(tournaments);
    } catch (error) {}
  }
}

const tournamentsController = new TournamentsController();

export default tournamentsController;