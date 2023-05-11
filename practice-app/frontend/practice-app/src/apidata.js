import { DealGET, DealPOST } from "./APIFunctions/DealApi";
import { MockGET, MockPOST } from "./APIFunctions/MockAPI";

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
}

export default apidata;