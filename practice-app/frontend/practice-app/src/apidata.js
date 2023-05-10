import { MockGET, MockPOST } from "./APIFunctions/MockAPI";
import { CategoryGET, CategoryPOST } from "./APIFunctions/GameByCategoryApi";

const apidata = {
  test: {
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
  jest: {
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
};

export default apidata;
