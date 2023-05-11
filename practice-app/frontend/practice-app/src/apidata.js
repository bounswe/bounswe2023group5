


import { DealGET, DealPOST } from "./APIFunctions/DealApi";
import { MockGET, MockGET2, MockPOST } from "./APIFunctions/MockAPI";
import { AchievementGET, AchievementPOST } from "./APIFunctions/AchievementApi";
import { GenreGET, GenrePOST } from "./APIFunctions/GenreApi";


const apidata = {
  "deal": {
    name: "Deals",
    postFunction: DealPOST,
    getFunction: DealGET, 
    form: {
      buttonText: "Search Deals",
      inputs: [
        {
          type: "text",
          name: "title",
          label: "Game Title"
        }, 
        {

          type: "number",
          name: "upperPrice",
          label: "Upper Price",          
        },
        {
          type: "number",
          name: "dealCount",
          label: "Deal Count",          
        },
        {

          type: "number",
          name: "minimumRating",
          label: "Minimum Steam Rating",          
        },
        {
          type: "bool",
          name: "onSale",
          label: "Only Include Games On Sale",          
        }
      ]
    }
  },
  "test": {
    name: "Test",
    //jsonUrl: "https://jsonplaceholder.typicode.com/todos",
    postFunction: MockPOST,
    getFunction: MockGET,
    form: {
      buttonText: "Submit",
      inputs: [
        {
          type: "text",
          name: "gameName",
          label: "Name of the Game",
        },
        {

          type: "number",
          name: "gameNumber",
          label: "Number test",
        },
        {
          type: "bool",
          name: "gameBool",
          label: "Bol test",
        },
        {

          type: "select",
          name: "sort",
          label: "Sort By",

          options: [
            {
              name: "Newest",
              value: "newest"
            },
            {
              name: "Most Popular",
              value: "popularity"
            }
          ]
        }
      ]
    }
  },
  "jest": {

    postFunction: MockPOST,
    getFunction: MockGET2,

    name: "Jest",
    form: {
      buttonText: "Simit",
      inputs: [
        {
          type: "text",
          name: "gameName",
          label: "Name of the Game",

        }

      ]
    }
  },
  "achievement": {

    postFunction: AchievementPOST,
    getFunction: AchievementGET,

    name: "Achievement",
    form: {

      buttonText: "POST GAME'S LEAST COMPLETED ACHIEVEMENTS",
      inputs: [
        {
          type: "number",
          name: "gameid",
          label: "Steam Id of the Game (Please provide a valid id)",
        }

      ]
    }
  },
  "genre": {

    postFunction: GenrePOST,
    getFunction: GenreGET,

    name: "Genre",
    form: {

      buttonText: "POST GENRE'S INFO",
      inputs: [
        {

          type: "select",
          name: "genreID",
          label: "Genre ID",

          options: [
            {
              name: "Action",
              value: 4
            },
            {
              name: "Indie",
              value: 51
            },
            {
              name: "Adventure",
              value: 3
            },
            {
              name: "RPG",
              value: 5
            },
            {
              name: "Strategy",
              value: 10
            },
            {
              name: "Shooter",
              value: 2
            },
            {
              name: "Casual",
              value: 40
            },
            {
              name: "Simulation",
              value: 14
            },
            {
              name: "Puzzle",
              value: 7
            },
            {
              name: "Arcade",
              value: 11
            },
            {
              name: "Platformer",
              value: 83
            },
            {
              name: "Massively Multiplayer",
              value: 59
            },
            {
              name: "Racing",
              value: 1
            },
            {
              name: "Sports",
              value: 15
            }
          ]
        }

      ]
    }
  }
}

export default apidata;
