import request from "supertest";
import jest from "jest";
import { app } from "../index.js";
import RockPaperScissors from "../src/features/rockpaperscissors/schema/rockPaperScissorsSchema.js";
import mongoose from "mongoose";

const seedData = async () => {
    await RockPaperScissors.insertMany([
      {
        _id: new mongoose.Types.ObjectId(),
        user_email: "test@email.com",
        user_choice: "paper",
        user_beats: "rock",
        ai_choice: "paper",
        ai_beats: "rock",
        result: "Draw",
      },
  
      {
        _id: new mongoose.Types.ObjectId(),
        user_email: "test@email.com",
        user_choice: "scissors",
        user_beats: "paper",
        ai_choice: "rock",
        ai_beats: "scissors",
        result: "You lose",
      },
  
      {
        _id: new mongoose.Types.ObjectId(),
        user_email: "test@email.com",
        user_choice: "rock",
        user_beats: "scissors",
        ai_choice: "scissors",
        ai_beats: "paper",
        result: "You win",
      },

    ]);
  };
  
  const removeData = async () => {
    await RockPaperScissors.deleteMany({ user_email: "test@email.com" });
  };
  
  describe("POST /games/choice", function () {
    const url = "/api/v1/games/choice";
    const correctPostData = {
      userEmail: "test@email.com",
      choice: "paper",
    };
    const missingEmailData = {
      choice: "paper",
    };
    const missingChoiceData = {
      userEmail: "test@email.com",
    };
    const wrongChoice = {
      userEmail: "test@email.com",
      choice: "paperr",
    };
  
    test("should respond with status code 201 and a success message in json with correct data  ", async function () {
      const response = await request(app).post(url).send(correctPostData);
  
      expect(response.status).toEqual(201);
      expect(response.headers["content-type"]).toMatch(/json/);
      expect(response.body.status).toEqual("success");
      expect(response.body.message).toEqual(
        "Game info is inserted to database successfully"
      );
    });
  
    test("should respond with status code 400 and a error message in json with missing email", async function () {
      const response = await request(app).post(url).send(missingEmailData);
  
      expect(response.status).toEqual(400);
      expect(response.headers["content-type"]).toMatch(/json/);
      expect(response.body.status).toEqual("Error");
      expect(response.body.message).toEqual(
        "You should provide all the necessary fields"
      );
    });
  
    test("should respond with status code 400 and a error message in json with missing choice", async function () {
      const response = await request(app).post(url).send(missingChoiceData);
  
      expect(response.status).toEqual(400);
      expect(response.headers["content-type"]).toMatch(/json/);
      expect(response.body.status).toEqual("Error");
      expect(response.body.message).toEqual(
        "You should provide all the necessary fields"
      );
    });
  
    test("should respond with status code 400 and a error message in json with wrong choice", async function () {
      const response = await request(app).post(url).send(wrongChoice);
  
      expect(response.status).toEqual(400);
      expect(response.headers["content-type"]).toMatch(/json/);
      expect(response.body.status).toEqual("Error");

    });
    afterAll(removeData);
  });
  
  describe("GET /games/choice", function () {
    beforeAll(seedData);
  
    const registeredUserUrl = "/api/v1/games/choice?userEmail=test@email.com";
  
    const nonRegisteredUserUrl =
      "/api/v1/games/choice?userEmail=random@email.com";
  
    test("should respond with status code 200 and a success message in json with correct data  ", async function () {
      const response = await request(app).get(registeredUserUrl);
      expect(response.status).toEqual(200);
      expect(response.headers["content-type"]).toMatch(/json/);
      expect(response.body[0].user_email).toBeDefined();
      expect(response.body[0].user_choice).toBeDefined();
      expect(response.body[0].user_beats).toBeDefined();
      expect(response.body[0].ai_choice).toBeDefined();
      expect(response.body[0].ai_beats).toBeDefined();
      expect(response.body[0].result).toBeDefined();
    });
  
    test("should return an empty array with a non registered user email ", async function () {
      const response = await request(app).get(nonRegisteredUserUrl);
  
      expect(response.status).toEqual(200);
      expect(response.headers["content-type"]).toMatch(/json/);
      expect(response.body.length).toEqual(0);
    });
  
    afterAll(removeData);
  });
  