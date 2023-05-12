import request from "supertest";
import jest from "jest";
import { app } from "../index.js";
import Tournaments from "../src/features/tournaments/schema/tournamentsSchema.js";
import mongoose from "mongoose";

const seedData = async () => {
  await Tournaments.insertMany([
    {
      _id: new mongoose.Types.ObjectId(),
      user_email: "test@email.com",
      id: 1890,
      name: "Sims",
      flag: "sims",
    },

    {
      _id: new mongoose.Types.ObjectId(),
      user_email: "test@email.com",
      id: 1785,
      name: "Fall Guys",
      flag: "fallguys",
    },
  ]);
};

const removeData = async () => {
  await Tournaments.deleteMany({ user_email: "test@email.com" });
};

describe("POST /games/tournaments", function () {
  const url = "/api/v1/games/tournaments";
  const correctPostData = {
    userEmail: "test@email.com",
  };
  const missingEmailData = {
    userEmail: "",
  };

  test("should respond with status code 201 and a success message in json with correct data  ", async function () {
    const response = await request(app).post(url).send(correctPostData);

    expect(response.status).toEqual(201);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("success");
    expect(response.body.message).toEqual(
      "tournaments for esports are inserted to database successfully"
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
  afterAll(removeData);
});

describe("GET /games/tournaments", function () {
  beforeAll(seedData);

  const registeredUserUrl =
    "/api/v1/games/tournaments?userEmail=test@email.com";

  const nonRegisteredUserUrl =
    "/api/v1/games/tournaments?userEmail=random@email.com";

  test("should respond with status code 200 and a success message in json with correct data  ", async function () {
    const response = await request(app).get(registeredUserUrl);
    expect(response.status).toEqual(200);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.length).toEqual(2);
    expect(response.body[0].user_email).toBeDefined();
    expect(response.body[0].id).toBeDefined();
    expect(response.body[0].name).toBeDefined();
    expect(response.body[0].flag).toBeDefined();
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
