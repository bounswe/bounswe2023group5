import express from "express";
const router = express.Router();
import gameByCategoryController from "./gamebycategory/controller/GameByCategoryController.js";
import gameReviewController from "./gamereview/controller/GameReviewController.js";
import GameReview from "./gamereview/schema/gameReviewSchema.js";
import cardByNameController from "./hearthstonecard/controller/CardByNameController.js";

router.get("/category", gameByCategoryController.getGamesByEmail);
router.post("/category", gameByCategoryController.insertGames);

router.get("/card", cardByNameController.getCardsByEmail);
router.post("/card", cardByNameController.insertCardInfo);

router.post("/review", gameReviewController.insertGameReviews);

export default router;
