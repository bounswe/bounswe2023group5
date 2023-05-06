import Error from "./Error.js";
import errorIds from "./const.js";

class InternalServer extends Error {
  constructor() {
    super();
    super.id = errorIds.InternalServer;
    super.reason = "Internal server error has occured!";
    super.statusCode = 500;
  }
}

export default InternalServer;
