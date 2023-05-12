import axios from "axios";
import EmptyFieldError from "../../../shared/errors/EmptyField.js";
import successfulResponse from "../../../shared/response/successfulResponse.js";
import ExternalApiError from "../../../shared/errors/ExternalApi.js";
import TopGames from "../schema/topGamesSchema.js";

const theMap = new Map();


class TopGamesController {
    async insertTopGames(req, res, next) {
        try {
            // Get the user email from request
            const { userEmail, number } = req.body;

            // Check whether the user email field is empty
            if (!userEmail || !number) {
                // Send error to the next middleware
                next(new EmptyFieldError());
                return;
            }

            // Put the number into the map
            theMap.set(userEmail, number);

            // Url of the external API
            let url = `https://www.steamspy.com/api.php?request=top100forever`;

            // Get the response from the url
            const response = await axios.get(url);

            let insertValues = [];

            // Each field in response.data represent a game data
            for (let key in response.data) {
                // Filter necessary fields from the response
                const {
                    name,
                    developer,
                    publisher,
                    owners,
                    average_forever,
                    average_2weeks,
                    median_forever,
                    median_2weeks,
                    price,
                    initialprice,
                    discount,
                    ccu
                } = response.data[key];

                insertValues.push({
                    user_email: userEmail,
                    name,
                    developer,
                    publisher,
                    owners,
                    average_forever,
                    average_2weeks,
                    median_forever,
                    median_2weeks,
                    price,
                    initialprice,
                    discount,
                    ccu

                });
            }

            // Insert the values to the mongodb DB
            await TopGames.insertMany(insertValues);

            // Respond with status code 201 indicating the resource is created
            // successfully
            res
                .status(201)
                .json(
                    successfulResponse("Info for top games is inserted into DB successfully")
                );
        }
        catch (error) {
            // Print the error
            console.log(error);

            // If this condition is true, then it means that the external api has returned an error.
            if (error.response) {
                if (error.response.data.status_message) {
                    next(new ExternalApiError(error.response.data.status_message));
                    return;
                }
            }

            // Else send the error to the next middleware
            next(error);
        }
    }

    async getTopGamesByEmail(req, res, next) {
        try {
            const userEmail = req.query.userEmail;

            // If the email field is empty, return error
            if (!userEmail) {
                next(new EmptyFieldError());
                return;
            }

            // Get the number
            const number = theMap.get(userEmail);

            // If the number field is empty, return error
            if (!number) {
                next(new EmptyFieldError());
                return;
            }

            // Get the games (last 100 created )
            const games = await TopGames.find(
                { user_email: userEmail },
                { _id: 0, __v: 0, updatedAt: 0 }
            ).sort({ createdAt: -1 })
                .limit(100);

            // Sort them descendingly acc. to their average_forever values
            games.sort((a, b) => b.average_forever - a.average_forever);

            // response json
            let resJson = {
                games: games.slice(0, number)
            }

            // Respond successfully with games
            res
                .status(200)
                .json(resJson);
        }
        catch (error) { }
    }

}

const topGamesController = new TopGamesController();

export default topGamesController;