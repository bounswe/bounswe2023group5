import { MockPOST } from "./APIFunctions/MockAPI";

const apidata = {
  "test": {
    name: "Test",
    jsonUrl: "https://jsonplaceholder.typicode.com/todos",
    postFunction: MockPOST,

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
    name: "Jest",
    jsonUrl: "https://jsonplaceholder.typicode.com/todos",
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