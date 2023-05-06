import express from "express";
const router = express.Router();
import gameByCategoryController from "./gamebycategory/controller/GameByCategoryController.js";

router.get("/category", gameByCategoryController.getGamesByEmail);
router.post("/category", gameByCategoryController.insertGames);

export default router;
