import request from "supertest";
import app from "../app.js";
import TopGames from "../src/features/topgames/schema/topGamesSchema.js";
import mongoose from "mongoose";

const seedData = async () => {
    await TopGames.insertMany([
        {
            _id: new mongoose.Types.ObjectId(),
            user_email: "test@mail.com",
            name: "Dota 2",
            developer: "Valve",
            publisher: "Valve",
            owners: "200,000,000 .. 500,000,000",
            average_forever: "37276",
            average_2weeks: "1356",
            median_forever: "917",
            median_2weeks: "712",
            price: "0",
            initialprice: "0",
            discount: "0",
            ccu: "541322",
        },
        {
            _id: new mongoose.Types.ObjectId(),
            user_email: "test@mail.com",
            name: "Counter-Strike: Global Offensive",
            developer: "Valve, Hidden Path Entertainment",
            publisher: "Valve",
            owners: "50,000,000 .. 100,000,000",
            average_forever: "27989",
            average_2weeks: "725",
            median_forever: "5680",
            median_2weeks: "301",
            price: "0",
            initialprice: "0",
            discount: "0",
            ccu: "1364757",
        },
    ]);
};

const removeData = async () => {
    await TopGames.deleteMany({ user_email: "test@mail.com" });
};

describe("POST /games/topgames", function () {
    const url = "/api/v1/games/topgames";
    const correctPostData = {
        userEmail: "test@mail.com",
        number: 2,
    };
    const missingEmailData = {
        number: 2,
    };
    const missingNumberData = {
        userEmail: "test@mail.com",
    };

    test("should respond with status code 201 and a success message in json with correct data  ", async function () {
        const response = await request(app).post(url).send(correctPostData);

        expect(response.status).toEqual(201);
        expect(response.headers["content-type"]).toMatch(/json/);
        expect(response.body.status).toEqual("success");
        expect(response.body.message).toEqual(
            "Info for top games is inserted into DB successfully"
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

    test("should respond with status code 400 and a error message in json with missing number", async function () {
        const response = await request(app).post(url).send(missingNumberData);

        expect(response.status).toEqual(400);
        expect(response.headers["content-type"]).toMatch(/json/);
        expect(response.body.status).toEqual("Error");
        expect(response.body.message).toEqual(
            "You should provide all the necessary fields"
        );
    });

    //afterAll(removeData);
});

describe("GET /games/topgames", function () {
    beforeAll(seedData);

    const registeredUserUrl = "/api/v1/games/topgames?userEmail=test@mail.com";

    const nonRegisteredUserUrl =
        "/api/v1/games/topgames?userEmail=random@mail.com";

    test("should respond with status code 200 and a success message in json with correct data  ", async function () {
        const response = await request(app).get(registeredUserUrl);
        expect(response.status).toEqual(200);
        expect(response.headers["content-type"]).toMatch(/json/);
        expect(response.body.games.length).toEqual(2);
        expect(response.body.games[0].user_email).toBeDefined();
        expect(response.body.games[0].name).toBeDefined();
        expect(response.body.games[0].developer).toBeDefined();
        expect(response.body.games[0].publisher).toBeDefined();
        expect(response.body.games[0].owners).toBeDefined();
        expect(response.body.games[0].average_forever).toBeDefined();
        expect(response.body.games[0].average_2weeks).toBeDefined();
        expect(response.body.games[0].median_forever).toBeDefined();
        expect(response.body.games[0].median_2weeks).toBeDefined();
        expect(response.body.games[0].price).toBeDefined();
        expect(response.body.games[0].initialprice).toBeDefined();
        expect(response.body.games[0].discount).toBeDefined();
        expect(response.body.games[0].ccu).toBeDefined();
        expect(response.body.games[0].createdAt).toBeDefined();
    });

    test("should return an empty array with a non registered user email ", async function () {
        const response = await request(app).get(nonRegisteredUserUrl);

        expect(response.status).toEqual(400);
    });

    afterAll(removeData);
});
