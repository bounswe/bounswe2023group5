


import { DealGET, DealPOST } from "./APIFunctions/DealApi";
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
  }
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
