import request from "supertest";
import jest from "jest";
import app from "../app.js";
import GameByUser from "../src/features/gamebyuser/schema/gameByUserSchema.js";

const seedData = async () => {
  await GameByUser.insertMany({
    "user_email": "test@test.com",
    "games": [
      {
        "game_id": "238960",
        "playtime": "420",
        "playtime_on_windows": "69",
      }
    ]
  });
};

const removeData = async () => {
  await GameByUser.deleteMany({ user_email: "test@email.com" });
};

describe("POST /games/user", function() {
  const url = "/api/v1/games/user";
  const correctPostData = {
    userEmail: "test@email.com",
    steamid: "76561198310035929",
  };
  const missingEmailData = {
    steamid: "76561198310035929",
  };
  const missingSteamidData = {
    userEmail: "test@email.com",
  };

  test("should respond with status code 201 and a success message in json with correct data  ", async function() {
    const response = await request(app).post(url).send(correctPostData);

    expect(response.status).toEqual(201);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("success");
    expect(response.body.message).toEqual(
      "Games are inserted to database successfully"
    );
  });

  test("should respond with status code 400 and a error message in json with missing email", async function() {
    const response = await request(app).post(url).send(missingEmailData);

    expect(response.status).toEqual(400);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("Error");
    expect(response.body.message).toEqual(
      "You should provide all the necessary fields"
    );
  });

  test("should respond with status code 400 and a error message in json with missing steamid", async function() {
    const response = await request(app).post(url).send(missingSteamidData);

    expect(response.status).toEqual(400);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("Error");
    expect(response.body.message).toEqual(
      "You should provide all the necessary fields"
    );
  });

  afterAll(removeData);
});

describe("GET /games/user", function() {
  beforeAll(seedData);

  const registeredUserUrl = "/api/v1/games/user?userEmail=test@email.com";

  const nonRegisteredUserUrl =
    "/api/v1/games/user?userEmail=random@email.com";

  test("should return an empty array with a non registered user email ", async function() {
    const response = await request(app).get(nonRegisteredUserUrl);
    expect(response.status).toEqual(200);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.length).toEqual(0);
  });

  afterAll(removeData);
});
