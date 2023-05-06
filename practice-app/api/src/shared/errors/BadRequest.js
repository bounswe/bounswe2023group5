import Error from "./Error.js";
import errorIds from "./const.js";

class BadRequest extends Error {
  constructor(message) {
    super();
    super.id = errorIds.BadRequest;
    super.reason = message || "Bad request!";
    super.statusCode = 400;
  }
}

export default BadRequest;
