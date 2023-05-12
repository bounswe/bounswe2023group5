import axios from "axios";
import EmptyFieldError from "../../../shared/errors/EmptyField.js";
import successfulResponse from "../../../shared/response/successfulResponse.js";
import ExternalApiError from "../../../shared/errors/ExternalApi.js";
import YugiohCardByName from "../schema/yugiohCardByNameSchema.js";

class YugiohCardByNameController {

    // defined function is basically retrieves the card information with the card name provided in url and insert desired values
    // into the desired schema that is provided by our database
    async insertYugiohCardInfo(req, res, next) {
        try {
            // get the single card name
            const { card_name, userEmail } = req.body;

            // check whether the card name field is empty. If it is, then give an empty field error. (You can check errors folder)
            if (!card_name || !userEmail) {
                next(new EmptyFieldError());
                return;
            }

            // external api url
            let url = `https://db.ygoprodeck.com/api/v7/cardinfo.php?name=${card_name}`;

            // send a get request to external url and wait(by using await keyword) until the response is returned
            const response = await axios.get(url);

            // format the response for database insertion
            const cardData = response.data.data[0];
            const formattedCardData = {
                card_id: cardData.id,
                card_name: cardData.name,
                card_type: cardData.type,
                user_email: userEmail
            };

            // insert the values to mongodb database
            await YugiohCardByName.insertMany(formattedCardData);

            // return a response with status code 201
            res
                .status(201)
                .json(
                    successfulResponse("Yugioh card info is inserted to database successfully")
                );
        }
        catch (error) {
            console.log(error);
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
            const addedCards = await YugiohCardByName.find(
                { user_email: userEmail },
                { _id: 0, __v: 0, updatedAt: 0 }
            ).sort({ createdAt: -1 });

            res.status(200).json(addedCards);
        }
        catch (error) { }
    }
}

const yugiohCardByNameController = new YugiohCardByNameController();

export default yugiohCardByNameController;