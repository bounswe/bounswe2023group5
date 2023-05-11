import { CategoryGET, CategoryPOST } from "./APIFunctions/GameByCategoryApi";
import { DealGET, DealPOST } from "./APIFunctions/DealApi";
import { UserGET, UserPOST } from "./APIFunctions/UserApi";
import { MockGET, MockGET2, MockPOST } from "./APIFunctions/MockAPI";
import { AchievementGET, AchievementPOST } from "./APIFunctions/AchievementApi";


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
  "user": {
    name: "Games Played On Windows",
    postFunction: UserPOST,
    getFunction: UserGET,
    form: {
      buttonText: "Search Games",
      inputs: [
        {
          type: "text",
          name: "steamid",
          label: "Steam ID"
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
              value: "newest",
            },
            {
              name: "Most Popular",
              value: "popularity",
            },
          ],
        },
      ],
    },
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
        },
      ],
    },
  },
  gamebycategory: {
    name: "Game By Category",
    postFunction: CategoryPOST,
    getFunction: CategoryGET,
    form: {
      buttonText: "Submit",
      inputs: [
        {
          type: "select",
          name: "category",
          label: "Category of the Game",
          options: [
            {
              name: "Action",
              value: "action",
            },
            {
              name: "Card",
              value: "card",
            },
            {
              name: "Fighting",
              value: "fighting",
            },
            {
              name: "Horror",
              value: "horror",
            },
            {
              name: "Military",
              value: "military",
            },
            {
              name: "Racing",
              value: "racing",
            },
            {
              name: "Shooter",
              value: "shooter",
            },
            {
              name: "Sports",
              value: "sports",
            },
            {
              name: "Strategy",
              value: "strategy",
            },
            {
              name: "Super Hero",
              value: "superhero",
            },
            {
              name: "Survival",
              value: "survival",
            },
          ],
        },
      ],
    },
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
  }
}


export default apidata;
