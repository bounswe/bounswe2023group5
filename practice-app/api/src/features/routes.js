import express from "express";
const router = express.Router();
import gameByCategoryController from "./gamebycategory/controller/GameByCategoryController.js";
import cardByNameController from "./hearthstonecard/controller/CardByNameController.js";
import gameByUserController from "./gamebyuser/controller/GameByUserController.js";
import gameSuggestionController from "./gamesuggestion/controller/GameSuggestionController.js";
import yugiohCardByNameController from "./yugiohcard/controller/YugiohCardByNameController.js";

router.get("/category", gameByCategoryController.getGamesByEmail);
router.post("/category", gameByCategoryController.insertGames);

router.get("/card", cardByNameController.getCardsByEmail);
router.post("/card", cardByNameController.insertCardInfo);

router.get("/user", gameByUserController.getGamesByEmail);
router.post("/user", gameByUserController.insertGames);

router.get("/suggestion", gameSuggestionController.getGameSuggestionsByEmail);
router.post("/suggestion", gameSuggestionController.insertGameSuggestions);

router.get("/yugiohcard", yugiohCardByNameController.getCardsByEmail);
router.post("/yugiohcard", yugiohCardByNameController.insertYugiohCardInfo);

export default router;
