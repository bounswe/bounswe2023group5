import axios from "axios";
import EmptyFieldError from "../../../shared/errors/EmptyField.js";
import successfulResponse from "../../../shared/response/successfulResponse.js";
import ExternalApiError from "../../../shared/errors/ExternalApi.js";
import GameSuggestions from "../schema/gameSuggestionsSchema.js";

class GameSuggestionController {
  async insertGameSuggestions(req, res, next) {
    try {
      // parse category from the body of the request
      const { userPrompt, userEmail } = req.body;

      // validates if userPrompt and userEmail is given in the request body properly
      if (!(userPrompt && userEmail)) {
        next(new EmptyFieldError());
        return;
      }

      // external api url, which takes the API key from environment variables
      let url = `https://api.retool.com/v1/workflows/e405ad45-1b1f-4365-bacb-fd0eedb25a5d/startTrigger?workflowApiKey=${process.env.RETOOL_API_KEY}`;
      var systemPrompt = "As a game recommendation program, I would like you to suggest five games based on a user's input, which may include their interests or games they have enjoyed in the past. Please provide the best recommendations possible based on the information provided in English. Here is the input: "

      var body = { "prompt": systemPrompt + userPrompt }

      // send a post request to third party url
      const response = await axios.post(url, body);

      // creating data object to write into database
      const data_to_insert = {
        user_email: userEmail,
        user_prompt: userPrompt,
        ai_suggestion: response.data.data
      }

      // inserting the data to MongoDB
      await GameSuggestions.insertMany([data_to_insert]);

      // return a response with status code 200
      res
        .status(201)
        .json(
          successfulResponse("Game suggestion is inserted to database successfully")
        );
    } catch (error) {
      console.log(error);
      // if this condition is true, then it means that the external api has returned an error.
      if (error.response) {
        if (error.response.data.status_message) {
          next(new ExternalApiError(error.response.data.status_message));
          return;
        }
      }
      next(error);
    }
  }

  async getGameSuggestionsByEmail(req, res, next) {
    try {
      // read the user email from query parameters
      const userEmail = req.query.userEmail;

      // check if userEmail field is given properly via url parameter. If not, returns an error
      if (!userEmail) {
        next(new EmptyFieldError());
        return;
      }

      // query the values from MongoDB gamesuggestions collection based on the user email and sort them according to their created times.
      const suggestions = await GameSuggestions.find(
        { user_email: userEmail },
        { user_prompt: 1, ai_suggestion:1, createdAt: 1 }
      ).sort({ createdAt: -1 });
      
      // reeturn the MongoDB data as response to the get request
      res.status(200).json(suggestions);
    } catch (error) {}
  }
}

const gameSuggestionController = new GameSuggestionController();

export default gameSuggestionController;
