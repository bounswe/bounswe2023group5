import axios from "axios";
import EmptyFieldError from "../../../shared/errors/EmptyField.js";
import successfulResponse from "../../../shared/response/successfulResponse.js";
import ExternalApiError from "../../../shared/errors/ExternalApi.js";
import gameByGenre from "../schema/gameByGenreSchema.js";

class GameByGenreController {

    // defined function is basically retrieves the card information with the card name provided in url and insert desired values
    // into the desired schema that is provided by our database
    async insertGameByGenre(req, res, next) {
        try {
            // get the single card name
            const { genreID, userEmail } = req.body;

            // check whether the card name field is empty. If it is, then give an empty field error. (You can check errors folder)
            if (!genreID || !userEmail) {
                next(new EmptyFieldError());
                return;
            }

            // external api url
            let url = `https://api.rawg.io/api/genres/${genreID}?key=${process.env.RAWG_API_KEY}`;

            // send a get request to external url and wait(by using await keyword) until the response is returned
            const response = await axios.get(url);

            // format the response for database insertion
            const genreData = response.data;
            const formattedGenreData = {
                genre_id: genreData.id,
                genre_name: genreData.name,
                games_count: genreData.games_count,
                image_background: genreData.image_background,
                game_description: genreData.description.slice(3,-4),
                user_email: userEmail
            };

            // insert the values to mongodb database
            await gameByGenre.insertMany(formattedGenreData);

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

    async getGameByGenre(req, res, next) {
        try {
            //get the user email from query parameter
            const userEmail = req.query.userEmail;

            //check whether the email field is empty. If it is, then give a empty field error. (You can check errors folder)
            if (!userEmail) {
                next(new EmptyFieldError());
                return;
            }

            // retrieve the values based on the user email and sort them according to their created times.
            const addedGenres = await gameByGenre.find(
                { user_email: userEmail },
                { _id: 0, __v: 0, updatedAt: 0 }
            ).sort({ createdAt: -1 });

            res.status(200).json(addedGenres);
        }
        catch (error) { }
    }
}

const gameByGenreController = new GameByGenreController();

export default gameByGenreController;