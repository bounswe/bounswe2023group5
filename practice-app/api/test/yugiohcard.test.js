import request from "supertest";
import jest from "jest";
import { app } from "../index.js";
import YugiohCardByName from "../src/features/yugiohcard/schema/yugiohCardByNameSchema.js";
import mongoose from "mongoose";

const seedData = async () => {
    await YugiohCardByName.insertMany([
        {
            _id: new mongoose.Types.ObjectId(),
            user_email: "test@email.com",
            card_id: 46986421,
            card_name: "Dark Magician",
            card_type: "Normal Monster"
        },

        {
            _id: new mongoose.Types.ObjectId(),
            user_email: "test@email.com",
            card_id: 55144522,
            card_name: "Pot of Greed",
            card_type: "Spell Card"
        },

        {
            _id: new mongoose.Types.ObjectId(),
            user_email: "test@email.com",
            card_id: 89943724,
            card_name: "Elemental HERO Neos",
            card_type: "Normal Monster"
        }
    ]);
};

const removeData = async () => {
    await YugiohCardByName.deleteMany({ user_email: "test@email.com" });
};


describe("POST /games/yugiohcard", function () {
    const url = "/api/v1/games/yugiohcard";
    const correctPostData = {
        userEmail: "test@email.com",
        card_name: "Dark Magician",
    };
    const missingEmailData = {
        card_name: "Dark Magician",
    };
    const missingNameData = {
        userEmail: "test@email.com",
    };
    const wrongName = {
        userEmail: "test@email.com",
        card_name: "Dark Magiciannnnn",
    };

    test("should respond with status code 201 and a success message in json with correct data  ", async function () {
        const response = await request(app).post(url).send(correctPostData);

        expect(response.status).toEqual(201);
        expect(response.headers["content-type"]).toMatch(/json/);
        expect(response.body.status).toEqual("success");
        expect(response.body.message).toEqual(
            "Yugioh card info is inserted to database successfully"
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
        const response = await request(app).post(url).send(missingNameData);

        expect(response.status).toEqual(400);
        expect(response.headers["content-type"]).toMatch(/json/);
        expect(response.body.status).toEqual("Error");
        expect(response.body.message).toEqual(
            "You should provide all the necessary fields"
        );
    });

    test("should respond with status code 400 and a error message in json with wrong category", async function () {
        const response = await request(app).post(url).send(wrongName);

        expect(response.status).toEqual(400);
        expect(response.headers["content-type"]).toMatch(/json/);
        expect(response.body.status).toEqual("Error");
        expect(response.body.message).toEqual(
            "An error occured!"
        );
    });
    afterAll(removeData);
});


describe("GET /games/yugiohcard", function () {
    beforeAll(seedData);

    const registeredUserUrl = "/api/v1/games/yugiohcard?userEmail=test@email.com";

    const nonRegisteredUserUrl =
        "/api/v1/games/yugiohcard?userEmail=random@email.com";

    test("should respond with status code 200 and a success message in json with correct data  ", async function () {
        const response = await request(app).get(registeredUserUrl);
        expect(response.status).toEqual(200);
        expect(response.headers["content-type"]).toMatch(/json/);
        expect(response.body.length).toEqual(3);
        expect(response.body[0].user_email).toBeDefined();
        expect(response.body[0].card_id).toBeDefined();
        expect(response.body[0].card_name).toBeDefined();
        expect(response.body[0].card_type).toBeDefined();
    });

    test("should return an empty array with a non registered user email ", async function () {
        const response = await request(app).get(nonRegisteredUserUrl);

        expect(response.status).toEqual(200);
        expect(response.headers["content-type"]).toMatch(/json/);
        expect(response.body.length).toEqual(0);
    });

    afterAll(removeData);
});
