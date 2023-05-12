import axios from "axios";
import EmptyFieldError from "../../../shared/errors/EmptyField.js";
import successfulResponse from "../../../shared/response/successfulResponse.js";
import ExternalApiError from "../../../shared/errors/ExternalApi.js";
import RockPaperScissors from "../schema/rockPaperScissorsSchema.js";

class RockPaperScissorsController{

  async insertChoice(req, res, next) {
    try {
        // get the single choice
        const { choice, userEmail } = req.body;
  

        if (!choice || !userEmail) {
          next(new EmptyFieldError());
          return;
        }
  
        // external api url
        let url = 'https://rock-paper-scissors13.p.rapidapi.com/';
  
        // send a get request to external url and wait(by using await keyword) until the response is returned
        const response = await axios.get(url,{headers:{
          'X-RapidAPI-Key': 'cdf27e3cb6msh0ec2bfa0aa992afp1ecfb2jsna24998faf63e',
          'X-RapidAPI-Host': 'rock-paper-scissors13.p.rapidapi.com' },params:{
              'choice': choice
          }
      });
  
      const responseData = response.data;
      const formattedData = {
        user_email : userEmail,
        user_choice : responseData.user.name,
        user_beats : responseData.user.beats,
        ai_choice : responseData.ai.name,
        ai_beats : responseData.ai.beats,
        result : responseData.result
        };

      // insert the values to mongodb database
      await RockPaperScissors.insertMany(formattedData);

      // return a response with status code 201
      res
        .status(201)
        .json(
          successfulResponse("Game info is inserted to database successfully")
        );
    } 
    catch (error) {
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

  async getChoice(req, res, next) {
    try {
      //get the user email from query parameter
      const userEmail = req.query.userEmail;

      //check whether the email field is empty. If it is, then give a empty field error. (You can check errors folder)
      if (!userEmail) {
        next(new EmptyFieldError());
        return;
      }

      // retrieve the values based on the user email and sort them according to their created times.
      const addedChoices = await RockPaperScissors.find(
        { user_email: userEmail },
        { _id: 0, __v: 0, updatedAt: 0 }
      ).sort({ createdAt: -1 });

      res.status(200).json(addedChoices);
    } 
    catch (error) {}
  }

}

const rockPaperScissorsController = new RockPaperScissorsController();

export default rockPaperScissorsController;