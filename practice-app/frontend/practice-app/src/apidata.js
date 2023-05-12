import { CategoryGET, CategoryPOST } from "./APIFunctions/GameByCategoryApi";
import { DealGET, DealPOST } from "./APIFunctions/DealApi";
import { SuggestionGet, SuggestionPost } from "./APIFunctions/SuggestionApi";
import { UserGET, UserPOST } from "./APIFunctions/UserApi";
import { MockGET, MockGET2, MockPOST } from "./APIFunctions/MockAPI";
import { AchievementGET, AchievementPOST } from "./APIFunctions/AchievementApi";
import { GenreGET, GenrePOST } from "./APIFunctions/GenreApi";
import { GameReviewGET, GameReviewPOST } from "./APIFunctions/GameReviewApi";
import { YugiohcardGET, YugiohcardPOST } from "./APIFunctions/YugiohcardApi";



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
          type: "number",
          name: "steamid",
          label: "Steam ID (Please provide a valid id.)"
        },
        {
          type: "number",
          name: "minPlaytime",
          label: "Minimum Playtime"
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
  },

  "review": {
    postFunction: GameReviewPOST,
    getFunction: GameReviewGET,
    
    name: "Game Reviews",
    form: {
      buttonText : "Search Reviews",
      inputs: [
        {
          type: "text",
          name: "gameNameCriteria",
          label: "Name of the Game",
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
              name: "Oldest",
              value: "oldest"
            },
            {
              name: "Popularity",
              value: "popularity"
            },
            {
              name: "Highest Score",
              value: "score-high"
            },
            {
              name: "Lowest Score",
              value: "score-low"
            }
          ]
        }
      ] 
    }
  },
  "yugiohcard": {

    postFunction: YugiohcardPOST,
    getFunction: YugiohcardGET,

    name: "Yugioh Card",
    form: {

      buttonText: "Search Card",
      inputs: [
        {
          type: "text",
          name: "card_name",
          label: "Card Name"
        }
      ]
    }
  }
}


export default apidata;
