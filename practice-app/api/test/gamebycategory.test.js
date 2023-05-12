import request from "supertest";
import jest from "jest";
import { app } from "../index.js";
import GameByCategory from "../src/features/gamebycategory/schema/gameByCategorySchema.js";
import mongoose from "mongoose";

const seedData = async () => {
  await GameByCategory.insertMany({
    _id: new mongoose.Types.ObjectId(),
    user_email: "test@email.com",
    category: "superhero",
    games: [
      {
        game_id: 525,
        name: "MultiVersus",
        short_description:
          "The Warner Bros lineup meets Smash in Player First Games’ MultiVersus.",
        platform: "PC (Windows)",
        publisher: "Warner Bros. Games",
        developer: "Player First Games",
        release_date: "2022-07-19",
      },
      {
        game_id: 541,
        name: "Marvel Snap",
        short_description:
          "A fast paced strategy card game set in the Marvel universe.",
        platform: "PC (Windows)",
        publisher: "Nuverse",
        developer: "Second Dinner Studios, Inc.",
        release_date: "2022-10-18",
      },
      {
        game_id: 453,
        name: "Gotham City Impostors",
        short_description:
          "A free to play multiplayer FPS that pits vigilantes dressed up like Batman against criminals dressed up like the Joker",
        platform: "PC (Windows)",
        publisher: "Warner Bros. Interactive Entertainment",
        developer: "Monolith Productions, Inc.",
        release_date: "2012-08-31",
      },
      {
        game_id: 260,
        name: "DC Universe Online",
        short_description:
          "A free-to-play, comics based MMORPG set in the popular DC Comics universe.",
        platform: "PC (Windows)",
        publisher: "Daybreak Games",
        developer: "Daybreak Games",
        release_date: "2011-01-11",
      },
      {
        game_id: 288,
        name: "Champions Online",
        short_description:
          "A superhero MMORPG created by the same studio behind City of Heroes.",
        platform: "PC (Windows)",
        publisher: "Perfect World Entertainment",
        developer: "Cryptic Studios",
        release_date: "2009-09-01",
      },
      {
        game_id: 430,
        name: "Urban Rivals",
        short_description:
          "A free to play browser based card-game with a high player base and comic-book inspired world!",
        platform: "Web Browser",
        publisher: "Boostr ",
        developer: "Acute Mobile",
        release_date: "2006-01-17",
      },
    ],
  });
};

const removeData = async () => {
  await GameByCategory.deleteMany({ user_email: "test@email.com" });
};

describe("POST /games/category", function () {
  const url = "/api/v1/games/category";
  const correctPostData = {
    userEmail: "test@email.com",
    category: "superhero",
  };
  const missingEmailData = {
    category: "superhero",
  };
  const missingCategoryData = {
    userEmail: "test@email.com",
  };
  const wrongCategory = {
    userEmail: "test@email.com",
    category: "actionn",
  };

  test("should respond with status code 201 and a success message in json with correct data  ", async function () {
    const response = await request(app).post(url).send(correctPostData);

    expect(response.status).toEqual(201);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("success");
    expect(response.body.message).toEqual(
      "Games are inserted to database successfully"
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

  test("should respond with status code 400 and a error message in json with missing category", async function () {
    const response = await request(app).post(url).send(missingCategoryData);

    expect(response.status).toEqual(400);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("Error");
    expect(response.body.message).toEqual(
      "You should provide all the necessary fields"
    );
  });

  test("should respond with status code 400 and a error message in json with wrong category", async function () {
    const response = await request(app).post(url).send(wrongCategory);

    expect(response.status).toEqual(400);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("Error");
    expect(response.body.message).toEqual(
      "No category found, please check the correct parameters."
    );
  });
  afterAll(removeData);
});

describe("GET /games/category", function () {
  beforeAll(seedData);

  const registeredUserUrl = "/api/v1/games/category?userEmail=test@email.com";

  const nonRegisteredUserUrl =
    "/api/v1/games/category?userEmail=random@email.com";

  test("should respond with status code 200 and a success message in json with correct data  ", async function () {
    const response = await request(app).get(registeredUserUrl);
    const body = response.body[0];
    const games = body.games;
    expect(response.status).toEqual(200);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(games).toBeDefined();
    expect(games.length).toEqual(6);
    expect(body.user_email).toBeDefined();
    expect(body.category).toBeDefined();
    expect(body.createdAt).toBeDefined();
    expect(games[0].name).toBeDefined();
    expect(games[0].game_id).toBeDefined();
    expect(games[0].short_description).toBeDefined();
    expect(games[0].platform).toBeDefined();
    expect(games[0].publisher).toBeDefined();
    expect(games[0].developer).toBeDefined();
    expect(games[0].release_date).toBeDefined();
  });

  test("should return an empty array with a non registered user email ", async function () {
    const response = await request(app).get(nonRegisteredUserUrl);

    expect(response.status).toEqual(200);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.length).toEqual(0);
  });

  afterAll(removeData);
});
