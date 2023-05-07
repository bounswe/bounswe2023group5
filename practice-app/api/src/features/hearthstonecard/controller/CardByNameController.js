import axios from "axios";
import EmptyFieldError from "../../../shared/errors/EmptyField.js";
import successfulResponse from "../../../shared/response/successfulResponse.js";
import ExternalApiError from "../../../shared/errors/ExternalApi.js";
import CardByName from "../schema/cardByNameSchema.js";

class CardByNameController {

  // defined function is basically retrieves the card information with the card name provided in url and insert desired values
  // into the desired schema that is provided by our database
  async insertCardInfo(req, res, next) {
    try {
      // get the single card name
      const { card_name, userEmail } = req.body;

      // check whether the card name field is empty. If it is, then give an empty field error. (You can check errors folder)
      if (!card_name || !userEmail) {
        next(new EmptyFieldError());
        return;
      }

      // external api url
      let url = `https://omgvamp-hearthstone-v1.p.rapidapi.com/cards/${card_name}`;

      // send a get request to external url and wait(by using await keyword) until the response is returned
      const response = await axios.get(url,{headers:{
        'content-type': 'application/octet-stream',
        'X-RapidAPI-Key': '0207b20c50mshd56493d52f8e2fep1804e1jsn0e9267e405fd',
        'X-RapidAPI-Host': 'omgvamp-hearthstone-v1.p.rapidapi.com'}
      });

      // maps from response to database fields
      const rowValues = response.data.map((card) => {
        const {
          cardId,
          name,
          cardSet,
        } = card;
        return {
          card_id: cardId,
          card_name: name,
          card_set : cardSet,
          user_email : userEmail
        };
      });

      // insert the values to mongodb database
      await CardByName.insertMany(rowValues);

      // return a response with status code 201
      res
        .status(201)
        .json(
          successfulResponse("Card info is inserted to database successfully")
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

  async getCardsByEmail(req, res, next) {
    try {
      //get the user email from query parameter
      const userEmail = req.query.userEmail;

      //check whether the email field is empty. If it is, then give a empty field error. (You can check errors folder)
      if (!userEmail) {
        next(new EmptyFieldError());
        return;
      }

      // retrieve the values based on the user email and sort them according to their created times.
      const addedCards = await CardByName.find(
        { user_email: userEmail },
        { _id: 0, __v: 0, updatedAt: 0 }
      ).sort({ createdAt: -1 });

      res.status(200).json(addedCards);
    } 
    catch (error) {}
  }
}

const cardByNameController = new CardByNameController();

export default cardByNameController;
