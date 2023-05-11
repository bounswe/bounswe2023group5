import axios from "axios";
import EmptyFieldError from "../../../shared/errors/EmptyField.js";
import successfulResponse from "../../../shared/response/successfulResponse.js";
import ExternalApiError from "../../../shared/errors/ExternalApi.js";
import GameByDeal from "../schema/gameByDealSchema.js";

class GameByDealController {
    async getDeals(req, res, next) {
        try {
            const { userEmail, upperPrice, title } = req.body;
            if (!(userEmail)) {
                next(new EmptyFieldError());
                return;
            }

            const externalResponse = await axios.get("https://www.cheapshark.com/api/1.0/deals", {
                params: {
                    storeID: 1,
                    pageSize: 10,
                    upperPrice: upperPrice,
                    title: title
                }
            })

            const newGameByDeal = new GameByDeal({
                userEmail: userEmail,
                title: title,
                upperPrice: upperPrice,
                deals: externalResponse.data.map(item => ({
                    title: item.title,
                    salePrice: item.salePrice,
                    normalPrice: item.normalPrice,
                    steamRatingText: item.steamRatingText
                }))
            })

            await newGameByDeal.save();
            res
                .status(201)
                .json(
                    successfulResponse("History is inserted to database successfully")
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

    async getHistory(req, res, next) {
        try {
            const { userEmail} = req.query;
            if (!(userEmail)) {
                next(new EmptyFieldError());
                return;
            }

            
            const games = await GameByDeal.find(
                { userEmail: userEmail }
              ).sort({ createdAt: -1 });
        
              res.status(200).json(games);
        }
        catch (error) {
            console.log(error);
            
            next(error);
        }

    }
}
const gameByDealController = new GameByDealController();
export default gameByDealController;