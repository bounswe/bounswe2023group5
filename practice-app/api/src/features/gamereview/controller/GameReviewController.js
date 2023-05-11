import axios from "axios";
import dotenv from "dotenv";
dotenv.config();
import GameReview from "../schema/gameReviewSchema.js";
import EmptyFieldError from "../../../shared/errors/EmptyField.js";
import NotFoundError from "../../../shared/errors/NotFound.js";
import successfulResponse from "../../../shared/response/successfulResponse.js";
import Error from  "../../../shared/errors/Error.js";
import ExternalApiError from "../../../shared/errors/ExternalApi.js";

class GameReviewController {

    async insertGameReviews(req, resp, next) {

        // Reads the game name, user email and sort selection from the body of the request
        const { userEmail, gameNameCriteria, sort } = req.body;

        // If email or game name is empty returns empty field error 
        if (!(userEmail && gameNameCriteria)) {
            next(new EmptyFieldError());
            return;
        }

        // Prepares the headers needed for two external requests
        const headers = {
            'X-RapidAPI-Key': process.env.API_KEY_GAME_REVIEW,
            'X-RapidAPI-Host': 'opencritic-api.p.rapidapi.com'
        }
        
        // Prepares first external GET request
        const ext1stopts = {
            method: 'GET',
            url: 'https://opencritic-api.p.rapidapi.com/game/search',
            params: {
                criteria: gameNameCriteria
            },
            headers: headers
        };

        try {
            // Issues the request to the first endpoint
            const gameResp = await axios.request(ext1stopts);

            // Checks if external api returns success
            if (gameResp.status == 200) {

                // Checks if body of the response is null 
                if (gameResp.data) {

                    // Gets game id and game name of the game that is on top of the response list
                    const gameId = gameResp.data[0].id;
                    const gameName = gameResp.data[0].name;

                    // Prepares second request using the game id, and request parameter sort 
                    const ext2ndopts = {
                        method: 'GET',
                        url: `https://opencritic-api.p.rapidapi.com/reviews/game/${gameId}`,
                        params: {
                            sort: sort != null ? sort : 'blend'
                        },
                        headers: headers
                    };

                    const reviewResp = await axios.request(ext2ndopts);

                     // Checks if external api returns success
                    if (reviewResp.status == 200) {

                        // Checks if body of the response is null 
                        if (reviewResp.data) {

                            // Gets top 3 reviews with snippet and gameId matching the requested gameId
                            // Creates review list containing review objects having snippets as review_text and publish date
                            const reviews = reviewResp.data
                                .filter(review => review.snippet && review.game.id == gameId)
                                .slice(0, 3)
                                .map(r => {
                                    return {
                                        review_text: r.snippet,
                                        publish_date: r.publishedDate
                                    }
                                });

                            // Creates database objects for the game review table
                            const gameReviewDbObject = {
                                user_email: userEmail,
                                game_name: gameName,
                                game_id: gameId,
                                reviews: reviews,
                            }

                            // Inserts records to the database table
                            await GameReview.create(gameReviewDbObject);

                            // Returns response with 'CREATED' status 
                            resp.status(201).json(
                                successfulResponse(`Game reviews for the game ${gameName} are inserted to the database successfully`)
                            );
                        }
                        else {
                            next(new NotFoundError(`No review is found for the game: ${gameName}`));
                        }
                    }
                    // If 2nd endpoint doesn't return success returns an External Api Error
                    else {
                        next(new ExternalApiError());
                    }

                } else {
                    next(new NotFoundError("No game is found with the given name"));
                }

            }
            // If 1st endpoint doesn't return success returns an External Api Error
            else {
                next(new ExternalApiError());
            }
        } 
        // Catches any error and sends a generic error response
        catch (error) {
            console.error(error);
            next(new Error());
            return;
        }

    }

    async getGameReviewsByEmail(req, resp, next) {

         // Reads the user email from query parameters
         const userEmail = req.query.userEmail;
    
         // Checks if userEmail field is given properly via url parameter. If not, returns an Empty Field error
         if (!userEmail) {
           next(new EmptyFieldError());
           return;
         }

         try {
            // Fetches reviews by user email from database and sorts so that the most recent comes up first
            const reviews = await GameReview.find(
                { user_email: userEmail }
              ).sort({ createdAt: -1 });
            
            // Checks if reviews list is empty 
            if (typeof reviews !== 'undefined' && reviews.length > 0) {
                // Returns the MongoDB data as response to the get request
                resp.status(200).json(reviews);
            }
            else {
                next(new NotFoundError("There is no game you've searched for its reviews before"));
                return;
            }
        }
        // Catches any error and sends a generic error response
        catch (error) {
            console.error(error);
            next(new Error());
            return;
        }

    }

}

const gameReviewController = new GameReviewController();

export default gameReviewController;

