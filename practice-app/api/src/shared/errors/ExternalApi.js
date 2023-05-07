import Error from "./Error.js";
import errorIds from "./const.js";

class ExternalApi extends Error {
  constructor(message) {
    super();
    super.id = errorIds.ExternalApi;
    super.reason = message || "External api throws an error!";
    super.statusCode = 400;
  }
}

export default ExternalApi;
