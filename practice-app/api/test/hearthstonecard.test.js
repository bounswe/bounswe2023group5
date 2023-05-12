import request from "supertest";
import jest from "jest";
import { app } from "../index.js";
import CardByName from "../src/features/hearthstonecard/schema/cardByNameSchema.js";
import mongoose from "mongoose";

const seedData = async () => {
  await CardByName.insertMany([
    {
      _id: new mongoose.Types.ObjectId(),
      card_id: "RLK_516",
      card_name: "Bone Breaker",
      card_set: "Path of Arthas",
      user_email: "tester@mail.com"
    },
    {
        _id: new mongoose.Types.ObjectId(),
        card_id: "RLK_083",
        card_name: "Deathchiller",
        card_set: "Path of Arthas",
        user_email: "tester@mail.com"
    },
    {
        _id: new mongoose.Types.ObjectId(),
        card_id: "RLK_225",
        card_name: "Blightfang",
        card_set: "March of the Lich King",
        user_email: "tester@mail.com"
    },
    {
        _id: new mongoose.Types.ObjectId(),
        card_id: "ETC_522",
        card_name: "Screaming Banshee",
        card_set: "Festival of Legends",
        user_email: "tester@mail.com"
    }
  ]);
};

const removeData = async () => {
  await GameByCategory.deleteMany({ user_email: "tester@mail.com" });
};

describe("POST /games/card", function () {
  const url = "/api/v1/games/card";
  const correctPostData = {
    card_name: "Bone Breaker",
    userEmail: "tester@mail.com"
  };
  const missingEmailData = {
    card_name: "Bone Breaker",
  };
  const missingCardNameData = {
    userEmail: "tester@mail.com",
  };
  const wrongCategory = {
    card_name: "Invalid_Card_Name",
    userEmail: "tester@mail.com"
  };

  test("should respond with status code 201 and a success message in json with correct data  ", async function () {
    const response = await request(app).post(url).send(correctPostData);
    expect(response.status).toEqual(201);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("success");
    expect(response.body.message).toEqual(
        "Card info is inserted to database successfully"
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

  test("should respond with status code 400 and a error message in json with missing card name", async function () {
    const response = await request(app).post(url).send(missingCardNameData);

    expect(response.status).toEqual(400);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("Error");
    expect(response.body.message).toEqual(
      "You should provide all the necessary fields"
    );
  });

  afterAll(removeData);
});

