import request from "supertest";
import jest from "jest";
import  app  from "../app.js";
import GameByGenre from "../src/features/gameByGenre/schema/gameByGenreSchema.js";
import mongoose from "mongoose";

const seedData = async () => {
    await GameByGenre.insertMany([
    {
        _id: new mongoose.Types.ObjectId(),
        genre_id: 4,
        genre_name: "Action",
        genre_slug: "action",
        games_count: 172403,
        image_background: "https://media.rawg.io/media/games/fc1/fc1307a2774506b5bd65d7e8424664a7.jpg",
        game_description: "<p>The action game is a genre that includes fights, puzzles, and strategies emphasizing coordination and reaction. It includes a large variety of sub-genres like fighting, beat &#39;em ups, shooters, survivals, mazes, and platforms; sometimes even multiplayer online battles and real-time strategies. Usually, the player performs as the protagonist with its unique abilities; some games add power-ups along the way. The character aims to complete levels, collect items, avoid obstacles, and battle against antagonists. It&#39;s necessary to avoid severe injuries during fights; if the health bar goes low, the player loses. Some games have an unbeatable number of enemies, and the only goal is to maximize score and survive for as long as possible. There might be a boss enemy who appears at the last level; he has unique abilities and a longer health bar. Pong is one of the first action games, released in 1972; the latest include Battlefield, Assasin&#39;s Creed, Fortnite and Dark Souls.</p>",
        user_email: "test@email.com",
    },

    {
        _id: new mongoose.Types.ObjectId(),
        genre_id: 1,
        genre_name: "Racing",
        genre_slug: "racing",
        games_count: 23957,
        image_background: "https://media.rawg.io/media/games/ff6/ff66ce127716df74175961831ad3a23a.jpg",
        game_description: "<p>Racing games is the video game genre with a high focus on driving competition. Such games are usually presented from the first-person or a third-person perspective. It is noted that gamepads are generally more suited to control the vehicle than keyboard/mouse pair. Although car avatars may render real-life examples, there are many instances where spaceships, formless mechanisms and other fantastical entities take the role of the avatar. Grand Prix released in 1969 is considered to be the first racing game ever made. Racings is a defining genre of a video game which is, in turn, can be divided into a lot of sub-genres: for instance, a primary focus may be made on destroying enemies&#39; vehicles (FlatOut, Twisted Metal) or crushing as many environments as possible (Split/Second). Those mechanics can alternatively be mixed with open world structure or set in the defined assortment of separate racing events.</p>",
        user_email: "test@email.com",
    },

    {
        _id: new mongoose.Types.ObjectId(),
        genre_id: 2,
        genre_name: "Shooter",
        genre_slug: "shooter",
        games_count: 59319,
        image_background: "https://media.rawg.io/media/games/9fa/9fa63622543e5d4f6d99aa9d73b043de.jpg",
        game_description: "<p>A shooter is a sub-genre of action video games the gameplay of which is thoroughly centered around shooting targets. Such games can be presented from first and the third perspectives with the latter being mostly twin-stick platforming shooters. Mouse and keyboard are widely regarded as the best controllers for shooters, as the firing demands high precision achieved only with manual aiming. The primary goal of shooters is to defeat enemies by discharging loads of bullets into them. Shooters are the most discussable video game genre when it comes to judging violence in games, as the gunfire process involves realistic scenes of killing quite often. Sub-genre of shooters is also divided by sub-subgenres such as shoot&#39;em ups, tactical shooters, and hero shooters.  The former involves changing a direction of movement and shooting forward while the latter focuses on wiping out tons of enemies by one protagonist. Notable games of the genre are Resogun, Bulletstorm and Call of Duty.</p>",
        user_email: "test@email.com",
    },
  ]);
};

const removeData = async () => {
  await GameByGenre.deleteMany({ user_email: "test@email.com" });
};

describe("POST /games/genre", function () {
  const url = "/api/v1/games/genre";
  const correctPostData = {
    userEmail: "test@email.com",
    genreID: 4,
  };
  const missingEmailData = {
    genreID: 4,
  };
  const missingGenreData = {
    userEmail: "test@email.com",
  };
  const wrongGenre = {
    userEmail: "test@email.com",
    genreID: "adventuree",
  };

  test("should respond with status code 201 and a success message in json with correct data  ", async function () {
    const response = await request(app).post(url).send(correctPostData);

    expect(response.status).toEqual(201);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("success");
    expect(response.body.message).toEqual(
      "Games by genre info is inserted into DB successfully"
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

  test("should respond with status code 400 and a error message in json with missing Genre", async function () {
    const response = await request(app).post(url).send(missingGenreData);

    expect(response.status).toEqual(400);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("Error");
    expect(response.body.message).toEqual(
      "You should provide all the necessary fields"
    );
  });

  test("should respond with status code 400 and a error message in json with wrong genre", async function () {
    const response = await request(app).post(url).send(wrongGenre);

    expect(response.status).toEqual(400);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.status).toEqual("Error");

  });
  afterAll(removeData);
});

describe("GET /games/genre", function () {
  beforeAll(seedData);

  const registeredUserUrl = "/api/v1/games/genre?userEmail=test@email.com";

  const nonRegisteredUserUrl =
    "/api/v1/games/genre?userEmail=random@email.com";

  test("should respond with status code 200 and a success message in json with correct data  ", async function () {
    const response = await request(app).get(registeredUserUrl);
    expect(response.status).toEqual(200);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body[0].user_email).toBeDefined();
    expect(response.body[0].genre_id).toBeDefined();
    expect(response.body[0].genre_name).toBeDefined();
    expect(response.body[0].games_count).toBeDefined();
    expect(response.body[0].image_background).toBeDefined();
    expect(response.body[0].game_description).toBeDefined();
    expect(response.body[0].user_email).toBeDefined();
  });

  test("should return an empty array with a non registered user email ", async function () {
    const response = await request(app).get(nonRegisteredUserUrl);

    expect(response.status).toEqual(200);
    expect(response.headers["content-type"]).toMatch(/json/);
    expect(response.body.length).toEqual(0);
  });

  afterAll(removeData);
});
