import express from "express";
const router = express.Router();
import gameByCategoryController from "./gamebycategory/controller/GameByCategoryController.js";
import cardByNameController from "./hearthstonecard/controller/CardByNameController.js";
import tournamentsController from "./tournaments/controller/tournamentsController.js";

router.get("/category", gameByCategoryController.getGamesByEmail);
router.post("/category", gameByCategoryController.insertGames);

router.get("/card", cardByNameController.getCardsByEmail);
router.post("/card", cardByNameController.insertCardInfo);

router.get("/tournaments", tournamentsController.getTournamentsByEmail);
router.post("/tournaments", tournamentsController.insertTournaments);

export default router;
