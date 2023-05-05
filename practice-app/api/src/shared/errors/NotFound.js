import Error from "./Error.js";
import errorIds from "./const.js";

class NotFound extends Error {
  constructor(message) {
    super();
    super.id = errorIds.NotFound;
    super.reason = message || "Not Found!";
    super.statusCode = 404;
  }
}

export default NotFound;
