import request from "supertest";
import jest from "jest";
import  app  from "../app.js";
import AchievementByGameId from "../src/features/achievementbygameid/schema/achievementByGameIdSchema.js";
import mongoose from "mongoose";

const seedData = async () => {
  await AchievementByGameId.insertMany([
    {
      _id: new mongoose.Types.ObjectId(),
      user_email: "test@email.com",
      game_id: 300,
      achievement_1:{
        name: "DOD_ALL_PACK_1",
        success_rate:"2.4000000953674316"
      },
      achievement_2:{
        name: "DOD_BOMBS_DEFUSED_GRIND",
        success_rate:"2.799999952316284"
      },
      achievement_3:{
        name: "DOD_WEAPON_MASTERY",
        success_rate:"3"
      },
      game_name: "Day of Defeat: Source"
      
    },

   
    {
      _id: new mongoose.Types.ObjectId(),
      user_email: "test@email.com",
      game_id: 730,
      achievement_1:{
        name: "GUN_GAME_ROUNDS_HIGH",
        success_rate:"1.7000000476837158"
      },
      achievement_2:{
        name: "WIN_GUN_GAME_ROUNDS_EXTREME",
        success_rate:"1.7000000476837158"
      },
      achievement_3:{
        name: "WIN_GUN_GAME_ROUNDS_ULTIMATE",
        success_rate:"1.7000000476837158"
      },
      game_name: "Counter-Strike: Global Offensive"
      
    },

   
    {
      _id: new mongoose.Types.ObjectId(),
      user_email: "test@email.com",
      game_id: 1593500,
      achievement_1:{
        name: "ACHIEVEMENT_ALL",
        success_rate:"5.099999904632568"
      },
      achievement_2:{
        name: "ACHIEVEMENT_24",
        success_rate:"6.900000095367432"
      },
      achievement_3:{
        name: "ACHIEVEMENT_25",
        success_rate:"6.900000095367432"
      },
      game_name: "God of War"
      
    }]);
};

const removeData = async () => {
  await AchievementByGameId.deleteMany({ user_email: "test@email.com" });
};

describe("POST /games/achievement", function () {
  const url = "/api/v1/games/achievement";
  const emptyData = {

  }
  const correctPostData = {
    userEmail: "test@email.com",
    gameid: "440",
  };
  const missingEmailData = {
    gameid: "440",
  };
  const missingGameId = {
    userEmail: "test@email.com",
  };
  const wrongGameId = {
    userEmail: "test@email.com",
    game_id: "200",
  };

  test("should respond with status code 201 and a success message in json with correct data  ", async function () {
    const response = await request(app).post(url).send(correctPostData);
    expect(response.status).toEqual(201);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("success");
    expect(response.body.message).toEqual(
      "Least completed achievements are inserted to database successfully"
    );
  },60000);

  test("should respond with status code 400 and a error message in json with missing email and game id", async function () {
    const response = await request(app).post(url).send(emptyData);

    expect(response.status).toEqual(401);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("Error");
    expect(response.body.message).toEqual(
      "You should provide all the necessary fields"
    );
  });

  test("should respond with status code 401 and a error message in json with missing email", async function () {
    const response = await request(app).post(url).send(missingEmailData);

    expect(response.status).toEqual(401);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("Error");
  });

  test("should respond with status code 400 and a error message in json with missing game id", async function () {
    const response = await request(app).post(url).send(missingGameId);

    expect(response.status).toEqual(400);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("Error");
    expect(response.body.message).toEqual(
      "You should provide all the necessary fields"
    );
  });

  test("should respond with status code 400 and a error message in json with wrong gameid", async function () {
    const response = await request(app).post(url).send(wrongGameId);

    expect(response.status).toEqual(400);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("Error");
  });
  afterAll(removeData);
});

describe("GET /games/category", function () {
  beforeAll(seedData);

  const registeredUserUrl = "/api/v1/games/achievement?userEmail=test@email.com";

  const nonRegisteredUserUrl =
    "/api/v1/games/achievement?userEmail=random@email.com";

  //test whether the data comes with correct order and correct data
  test("should respond with status code 200 and a success message in json with correct data  ", async function () {
    const response = await request(app).get(registeredUserUrl);
    expect(response.status).toEqual(200);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body[0].user_email).toBeDefined();
    expect(response.body[0].game_id).toBeDefined();
    expect(response.body[0].achievement_1).toBeDefined();
    expect(response.body[0].achievement_2).toBeDefined();
    expect(response.body[0].achievement_3).toBeDefined();
    expect(response.body[0].game_name).toBeDefined();
    expect(response.body[0].user_email).toEqual("test@email.com")
  });
  test("should respond with status code 200 and a success message in json with correct data  ", async function () {
    const response = await request(app).get(registeredUserUrl);
    expect(response.status).toEqual(200);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body[1].user_email).toBeDefined();
    expect(response.body[1].game_id).toBeDefined();
    expect(response.body[1].achievement_1).toBeDefined();
    expect(response.body[1].achievement_2).toBeDefined();
    expect(response.body[1].achievement_3).toBeDefined();
    expect(response.body[1].game_name).toBeDefined();
    expect(response.body[1].user_email).toEqual("test@email.com")
  });  
  test("should respond with status code 200 and a success message in json with correct data  ", async function () {
    const response = await request(app).get(registeredUserUrl);
    expect(response.status).toEqual(200);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body[2].user_email).toBeDefined();
    expect(response.body[2].game_id).toBeDefined();
    expect(response.body[2].achievement_1).toBeDefined();
    expect(response.body[2].achievement_2).toBeDefined();
    expect(response.body[2].achievement_3).toBeDefined();
    expect(response.body[2].game_name).toBeDefined();
    expect(response.body[2].user_email).toEqual("test@email.com")
  });
  // if we have a real real authentiacation system it should return 401.
  test("should return an empty array with a non registered user email ", async function () {
    const response = await request(app).get(nonRegisteredUserUrl);

    expect(response.status).toEqual(200);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.length).toEqual(0);
  });

  afterAll(removeData);
});
