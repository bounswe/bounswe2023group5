import axios from "axios";
import EmptyFieldError from "../../../shared/errors/EmptyField.js";
import successfulResponse from "../../../shared/response/successfulResponse.js";
import ExternalApiError from "../../../shared/errors/ExternalApi.js";
import GameByDeal from "../schema/gameByDealSchema.js";

class GameByDealController {
    async getDeals(req, res, next) {
        try {
            const { userEmail, upperPrice, title, dealCount, minimumRating, onSale } = req.body;
            if (!(userEmail)) {
                next(new EmptyFieldError());
                return;
            }

            const externalResponse = await axios.get("https://www.cheapshark.com/api/1.0/deals", {
                params: {
                    storeID: 1,
                    pageSize: 10,
                    upperPrice: upperPrice,
                    title: title,
                    pageSize: dealCount,
                    steamRating: minimumRating,
                    onSale: onSale
                }
            })


            const newGameByDeal = new GameByDeal({
                user_email: userEmail,
                title: title,
                upper_price: upperPrice,
                deals: externalResponse.data.map(item => ({
                    title: item.title,
                    sale_price: item.salePrice,
                    normal_price: item.normalPrice,
                    steam_rating_text: item.steamRatingText,
                    rating: item.steamRatingPercent+"%",
                    mon_sale: item.isOnSale
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
                { user_email: userEmail }
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