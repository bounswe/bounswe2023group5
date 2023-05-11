import request from "supertest";
import jest from "jest";
import  app  from "../app.js";
import GameByCategory from "../src/features/gamebycategory/schema/gameByCategorySchema.js";
import mongoose from "mongoose";

const seedData = async () => {
  await GameByCategory.insertMany([
    {
      _id: new mongoose.Types.ObjectId(),
      user_email: "test@email.com",
      game_id: 380,
      name: "Dark Orbit Reloaded",
      thumbnail: "https://www.freetogame.com/g/430/thumbnail.jpg",
      short_description:
        "A browser-based 3D space-combat MMO with a massive playerbase!",
      platform: "Web Browser",
      publisher: "Bigpoint",
      developer: "Bigpoint",
      release_date: "2006-12-11",
    },

    {
      _id: new mongoose.Types.ObjectId(),
      user_email: "test@email.com",
      game_id: 515,
      name: "Halo Infinite",
      thumbnail: "https://www.freetogame.com/g/200/thumbnail.jpg",
      short_description:
        "For the first time ever, a free-to-play Halo experience is available i…",
      platform: "PC (Windows)",
      publisher: "Xbox Game Studios",
      developer: "343 Industries",
      release_date: "2021-11-15",
    },

    {
      _id: new mongoose.Types.ObjectId(),
      user_email: "test@email.com",
      game_id: 5,
      name: "Crossout",
      thumbnail: "https://www.freetogame.com/g/204/thumbnail.jpg",
      short_description: "A post-apocalyptic MMO vehicle combat game! ",
      platform: "PC (Windows)",
      publisher: "Targem",
      developer: "Gaijin",
      release_date: "2017-05-30",
    },
  ]);
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
    expect(response.status).toEqual(200);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.length).toEqual(3);
    expect(response.body[0].user_email).toBeDefined();
    expect(response.body[0].game_id).toBeDefined();
    expect(response.body[0].name).toBeDefined();
    expect(response.body[0].thumbnail).toBeDefined();
    expect(response.body[0].short_description).toBeDefined();
    expect(response.body[0].platform).toBeDefined();
    expect(response.body[0].publisher).toBeDefined();
    expect(response.body[0].developer).toBeDefined();
    expect(response.body[0].release_date).toBeDefined();
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
