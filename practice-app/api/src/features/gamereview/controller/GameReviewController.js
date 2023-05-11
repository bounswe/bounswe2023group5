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

        const { userEmail, gameNameCriteria, sort } = req.body;

        if (!(userEmail && gameNameCriteria)) {
            next(new EmptyFieldError());
            return;
        }

        const headers = {
            'X-RapidAPI-Key': process.env.API_KEY_GAME_REVIEW,
            'X-RapidAPI-Host': 'opencritic-api.p.rapidapi.com'
        }

        const ext1stopts = {
            method: 'GET',
            url: 'https://opencritic-api.p.rapidapi.com/game/search',
            params: {
                criteria: gameNameCriteria
            },
            headers: headers
        };

        try {
            const gameResp = await axios.request(ext1stopts);
            if (gameResp.status == 200) {
                if (gameResp.data) {
                    const gameId = gameResp.data[0].id;
                    const gameName = gameResp.data[0].name;
                    const ext2ndopts = {
                        method: 'GET',
                        url: `https://opencritic-api.p.rapidapi.com/reviews/game/${gameId}`,
                        params: {
                            sort: sort != null ? sort : 'blend'
                        },
                        headers: headers
                    };

                    const reviewResp = await axios.request(ext2ndopts);
                    if (reviewResp.status == 200) {
                        if (reviewResp.data) {
                            const reviews = reviewResp.data
                                .filter(review => review.snippet && review.game.id == gameId)
                                .slice(0, 3)
                                .map(r => {
                                    return {
                                        review_text: r.snippet,
                                        publish_date: r.publishedDate
                                    }
                                });
                            const dbObject = {
                                user_email: userEmail,
                                game_name: gameName,
                                game_id: gameId,
                                reviews: reviews,
                            }
                            await GameReview.create(dbObject);
                            resp.status(201).json(
                                successfulResponse("Game reviews are inserted to the database successfully")
                            );
                        }

                    }

                } else {
                    next(new Error());
                }

            }


        } catch (error) {
            console.error(error);
        }

    }

    async getGameReviewsByEmail(req, resp, next) {

         // read the user email from query parameters
         const userEmail = req.query.userEmail;
    
         // check if userEmail field is given properly via url parameter. If not, returns an error
         if (!userEmail) {
           next(new EmptyFieldError());
           return;
         }

         try {
            const reviews = await GameReview.find(
                { user_email: userEmail }
              ).sort({ createdAt: -1 });
            
            if (typeof reviews !== 'undefined' && reviews.length > 0) {
                // return the MongoDB data as response to the get request
                resp.status(200).json(reviews);
            }
            else {
                next(new NotFoundError("There is no game that you've searched for its reviews before"));
                return;
            }
        } 
         
        catch (error) {
            next(new Error());
        }

    }

}


const gameReviewController = new GameReviewController();

export default gameReviewController;

