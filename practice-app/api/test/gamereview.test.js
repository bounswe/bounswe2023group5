import request from "supertest";
import jest from "jest";
import { app } from "../index.js";
import GameReview from "../src/features/gamereview/schema/gameReviewSchema.js";
import mongoose from "mongoose";

const seedData = async () => {
  await GameReview.insertMany([
    {
      _id: new mongoose.Types.ObjectId(),
      user_email: "test@email.com",
      game_id: 194,
      game_name: "Minecraft",
      reviews  : [
        {   review_text: "All told, this is yet another excellent port of Minecraft, nothing more and nothing less. With that being said, we would strongly recommend that you pick up this version, as the versatile setup of the Switch allows this to be the most easily accessible and playable Minecraft to date. That convenience factor is the only real notable difference, however, so it’s up to you whether that justifies paying for another version of Minecraft. If you want to play the best portable version of the game, however, look no further.",
            publish_date: "2017-05-11T00:00:00.000Z" },
        
        {
            review_text: "It looks good, plays well and hits all the classic Minecraft buttons, but with the bonus that your Minecraft addiction is a little easier to fit in with normal life. Throw in some of the best themed Minecraft content around and you have a must-have for Switch gamers, albeit not the absolutely best version of the game.",
            publish_date: "2017-05-15T00:00:00.000Z"
        },
        {
            review_text: "A revolutionary game developed on a brilliant idea, Minecraft is a sandbox made on exploration and construction, a fresh and rewarding experience for every kind of gamer.",
            publish_date: "2011-11-25T00:00:00.000Z"
        }]
    },
    {
      _id: new mongoose.Types.ObjectId(),
      user_email: "test@email.com",
      game_id: 2906,
      game_name: "Human Fall Flat",
      reviews  : [
        {   review_text: "In summary, one could state that Human: Fall Flat is a perfect puzzle game which doesn’t feature a single puzzle, and while such statement carries a significant amount of truth, it has to be stated that Human: Fall Flat‘s perfection carries a single major blemish, in form of its final level. A lot of the negativity concerning the finale comes from the fact that unlike the rest of the title it is anchored within a questionable setting, and the obstacles which were excellent throughout the earlier stages of the title, are nothing but a nuisance during this particular level. The ingenuity of water level, or the excitement which came with the castle playground, is nowhere to be found within the title’s finale, and it is rather disappointing, as the otherwise great title ends with a rather anti-climactic sequence which may leave a lot of players feeling deflated, and to some extent disappointed.",
            publish_date: "2017-05-08T04:00:00.000Z" },
        
        {
            review_text: "Human Fall Flat is a fantastic physics-based puzzle game that takes a simple concept and executes it perfectly, with a powerful physics engine hiding behind the simple looking façade. Pushing and pulling has never been so much fun.",
            publish_date: "2017-06-06T04:00:00.000Z"
        },
        {
            review_text: "With it’s innovative and clever controls, this simple and charming, yet complicated little gem is definitely worth the $15.",
            publish_date: "2017-05-26T00:00:00.000Z"
        }]
    }
  ]);
};

const removeData = async () => {
  await GameReview.deleteMany({ user_email: "test@email.com" });
};

describe("POST /games/review", function () {
  const url = "/api/v1/games/review";
  const correctPostData = {
    userEmail: "test@email.com",
    gameNameCriteria: "human fall flat",
  };
  const missingEmailData = {
    gameNameCriteria: "human fall flat",
  };
  const missingGameNameData = {
    userEmail: "test@email.com",
  };

  const withSortData = {
    userEmail: "test@email.com",
    gameNameCriteria: "human fall flat",
    sort: "score-high"
  };

  test("should respond with status code 201 and a success message in json with correct data  ", async function () {
    const response = await request(app).post(url).send(correctPostData);

    expect(response.status).toEqual(201);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("success");
    expect(response.body.message).toEqual(
      "Game reviews for the game Human: Fall Flat are inserted to the database successfully"
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

  test("should respond with status code 400 and a error message in json with missing game name", async function () {
    const response = await request(app).post(url).send(missingGameNameData);

    expect(response.status).toEqual(400);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("Error");
    expect(response.body.message).toEqual(
      "You should provide all the necessary fields"
    );
  });

  test("should respond with status code 201 if sort option is provided too", async function () {
    const response = await request(app).post(url).send(withSortData);

    expect(response.status).toEqual(201);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("success");
    expect(response.body.message).toEqual(
        "Game reviews for the game Human: Fall Flat are inserted to the database successfully"
    );
  });
  afterAll(removeData);
});

describe("GET /games/review", function () {
  beforeAll(seedData);

  const registeredUserUrl = "/api/v1/games/review?userEmail=test@email.com";

  const nonRegisteredUserUrl = "/api/v1/games/review?userEmail=random@email.com";

  test("should respond with status code 200 and a success message in json with correct data  ", async function () {
    const response = await request(app).get(registeredUserUrl);
    expect(response.status).toEqual(200);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.length).toEqual(2);
    expect(response.body[0].user_email).toBeDefined();
    expect(response.body[1].user_email).toBeDefined();
    expect(response.body[0].game_id).toBeDefined();
    expect(response.body[1].game_id).toBeDefined();
    expect(response.body[0].game_name).toBeDefined();
    expect(response.body[1].game_name).toBeDefined();
    expect(response.body[0].reviews.length).toEqual(3);
    expect(response.body[1].reviews.length).toEqual(3);
    expect(response.body[0].reviews[0].review_text).toBeDefined();
    expect(response.body[0].reviews[0].publish_date).toBeDefined();
    expect(response.body[1].reviews[0].review_text).toBeDefined();
    expect(response.body[1].reviews[0].publish_date).toBeDefined();
    expect(response.body[0].reviews[1].review_text).toBeDefined();
    expect(response.body[0].reviews[1].publish_date).toBeDefined();
    expect(response.body[1].reviews[1].review_text).toBeDefined();
    expect(response.body[1].reviews[1].publish_date).toBeDefined();
    expect(response.body[0].reviews[2].review_text).toBeDefined();
    expect(response.body[0].reviews[2].publish_date).toBeDefined();
    expect(response.body[1].reviews[2].review_text).toBeDefined();
    expect(response.body[1].reviews[2].publish_date).toBeDefined();
    expect(response.body[0].createdAt).toBeDefined();
  });

    test("should return a not found error with a non registered user email ", async function () {
    const response = await request(app).get(nonRegisteredUserUrl);

    expect(response.status).toEqual(404);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.message).toEqual("There is no game that you've searched for its reviews before");
  });

  afterAll(removeData);
});