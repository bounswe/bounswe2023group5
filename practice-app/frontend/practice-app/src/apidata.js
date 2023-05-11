


import { DealGET, DealPOST } from "./APIFunctions/DealApi";
import { SuggestionGet, SuggestionPost } from "./APIFunctions/SuggestionApi";
import { MockGET, MockGET2, MockPOST } from "./APIFunctions/MockAPI";


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
  "suggestion": {
    name: "Game Suggestion",
    postFunction: SuggestionPost,
    getFunction: SuggestionGet,
    form: {
      buttonText: "Submit",
      inputs: [
        {
          type: "text",
          name: "enjoyedGames",
          label: "Please type some examples of games you have enjoyed.",
        },
        {

          type: "select",
          name: "preferredGameType",
          label: "Preferred Game Type",

          options: [
            {
              name: "Role-Playing Games (RPG)",
              value: "Role-Playing Games (RPG)"
            },
            {
              name: "Sandbox",
              value: "Sandbox"
            },
            {
              name: "Action-Adventure",
              value: "Action-Adventure"
            },
            {
              name: "First-Person Shooter (FPS)",
              value: "First-Person Shooter (FPS)"
            },
            {
              name: "Open World",
              value: "Open World"
            },
            {
              name: "Platformer",
              value: "Platformer"
            },
            {
              name: "Fighting",
              value: "Fighting"
            },
            {
              name: "Real-Time Strategy (RTS)",
              value: "Real-Time Strategy (RTS)"
            },
            {
              name: "Survival Horror",
              value: "Survival Horror"
            },
            {
              name: "Racing",
              value: "Racing"
            },
            {
              name: "MMORPG",
              value: "MMORPG"
            }
          ]
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
  }
}

export default apidata;
