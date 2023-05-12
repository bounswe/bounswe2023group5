import request from "supertest";
import { app } from "../index.js";
import GameByDeal from "../src/features/gameByDeal/schema/gameByDealSchema.js";

const seedData = async () => {
  await GameByDeal.insertMany([{
    "userEmail": "test@test.com",
    "title": "al",
    "upperPrice": 5,
    "deals": [
      {
        "title": "Making History: The Calm and the Storm",
        "salePrice": "0.49",
        "normalPrice": "4.99",
        "steamRatingText": "Mostly Positive",
        "rating": "50%",
        "isOnSale": true,

      }
    ]
  }]);
};

const removeData = async () => {
  await GameByDeal.deleteMany({ user_email: "test@test.com" });
};

describe("POST /games/deal", function () {
  const url = "/api/v1/games/deal";
  const correctPostData = {
    userEmail: "test@email.com"
    , upperPrice: 10
    , title: "Warrior"
    , dealCount: 1
    , minimumRating: 50
    , onSale: true
  };
  const missingEmailData = {
    upperPrice: 10
    , title: "Warrior"
    , dealCount: 1
    , minimumRating: 50
    , onSale: true
  };


  test("should respond with status code 201 and a success message in json with correct data  ", async function () {
    const response = await request(app).post(url).send(correctPostData);

    expect(response.status).toEqual(201);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("success");
    expect(response.body.message).toEqual(
      "History is inserted to database successfully"
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

describe("GET /games/category", function () {
  beforeAll(seedData);

  const registeredUserUrl = "/api/v1/games/deal?userEmail=test@test.com";

  const nonRegisteredUserUrl =
    "/api/v1/games/category?userEmail=random@email.com";

  test("should respond with status code 200 and a success message in json with correct data  ", async function () {
    const response = await request(app).get(registeredUserUrl);
    expect(response.status).toEqual(200);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body[0].userEmail).toBeDefined();
    expect(response.body[0].title).toBeDefined();
    expect(response.body[0].upperPrice).toBeDefined();
    expect(response.body[0].deals).toBeDefined();
  });

  test("should return an empty array with a non registered user email ", async function () {
    const response = await request(app).get(nonRegisteredUserUrl);

    expect(response.status).toEqual(200);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.length).toEqual(0);
  });

  afterAll(removeData);
});
