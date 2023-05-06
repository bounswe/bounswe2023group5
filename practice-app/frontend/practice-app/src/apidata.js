const apidata = {
    "test": {
        name: "Test",
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