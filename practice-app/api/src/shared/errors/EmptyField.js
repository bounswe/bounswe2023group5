import Error from "./Error.js";
import errorIds from "./const.js";

class EmptyField extends Error {
  constructor(message) {
    super();
    super.id = errorIds.EmptyField;
    super.reason = message || "You should provide all the necessary fields";
    super.statusCode = 400;
  }
}

export default EmptyField;
