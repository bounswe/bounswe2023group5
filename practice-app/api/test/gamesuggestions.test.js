import request from "supertest";
import jest from "jest";
import  app  from "../app.js";
import mongoose from "mongoose";
import GameSuggestions from "../src/features/gamesuggestion/schema/gameSuggestionsSchema.js";

const seedData = async () => {
  await GameSuggestions.insertMany([
    {
      _id: new mongoose.Types.ObjectId(),
      user_email: "testmail@testt.com",
      enjoyed_games: 'League of Legends',
      preferred_game_type: "FPS",
      ai_suggestion: "Based on your input, you will like these games: 1",
    },
    {
      _id: new mongoose.Types.ObjectId(),
      user_email: "testmail@testt.com",
      enjoyed_games: 'League of Legends2',
      preferred_game_type: "FPS2",
      ai_suggestion: "Based on your input, you will like these games: 2",
    },
    {
      _id: new mongoose.Types.ObjectId(),
      user_email: "testmail@testt.com",
      enjoyed_games: 'League of Legends3',
      preferred_game_type: "FPS3",
      ai_suggestion: "Based on your input, you will like these games: 3",
    }
  ]);
};

const removeData = async () => {
  await GameSuggestions.deleteMany({ user_email: "testmail@testt.com" });
};

describe("POST /games/suggestion", function () {
  const url = "/api/v1/games/suggestion";
  const correctPostData = {
    userEmail: "testmail@testt.com",
    enjoyedGames: 'test',
    preferredGameType: 'test'
  };
  const missingEmailData = {
    enjoyedGames: 'test',
    preferredGameType: 'test'
  };
  const missingEnjoyedGamesData = {
    userEmail: "testmail@testt.com",
    preferredGameType: 'test'
  };
  const missingPreferredGameData = {
    userEmail: "testmail@testt.com",
    enjoyedGames: 'test'
  };

  test("should respond with status code 201 and a success message in json with correct data  ", async function () {
    const response = await request(app).post(url).send(correctPostData);

    expect(response.status).toEqual(201);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("success");
    expect(response.body.message).toEqual(
      "Game suggestion is inserted to database successfully"
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

  test("should respond with status code 400 and a error message in json with missing enjoyed games", async function () {
    const response = await request(app).post(url).send(missingEnjoyedGamesData);

    expect(response.status).toEqual(400);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("Error");
    expect(response.body.message).toEqual(
      "You should provide all the necessary fields"
    );
  });

  test("should respond with status code 400 and a error message in json with missing preferred game type", async function () {
    const response = await request(app).post(url).send(missingPreferredGameData);

    expect(response.status).toEqual(400);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("Error");
    expect(response.body.message).toEqual(
      "You should provide all the necessary fields"
    );
  });
  afterAll(removeData);
});

describe("GET /games/suggestion", function () {
  beforeAll(seedData);

  const registeredUserUrl = "/api/v1/games/suggestion?userEmail=testmail@testt.com";

  const nonRegisteredUserUrl =
    "/api/v1/games/suggestion?userEmail=random@test.com";

  test("should respond with status code 200 and a success message in json with correct data  ", async function () {
    const response = await request(app).get(registeredUserUrl);
    expect(response.status).toEqual(200);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.length).toEqual(3);
    expect(response.body[0]._id).toBeDefined();
    expect(response.body[0].enjoyed_games).toBeDefined();
    expect(response.body[0].preferred_game_type).toBeDefined();
    expect(response.body[0].ai_suggestion).toBeDefined();
    expect(response.body[0].createdAt).toBeDefined();
  });

  test("should return an empty array with a non registered user email ", async function () {
    const response = await request(app).get(nonRegisteredUserUrl);

    expect(response.status).toEqual(200);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.length).toEqual(0);
  });

  afterAll(removeData);
});
