import dotenv from "dotenv";
dotenv.config();
import express from "express";
import bodyParser from "body-parser";
import cors from "cors";
import router from "./src/features/routes.js";
import errorConst from "./src/shared/errors/const.js";
import Error from "./src/shared/errors/Error.js";
import startDatabaseConnection from "./database.js";

export const app = express();

app.use(cors());
app.use(bodyParser.json({ limit: "10mb" }));
app.use(bodyParser.urlencoded({ extended: true, limit: "10mb" }));

await startDatabaseConnection();

const baseUrl = process.env.BASE_URL || "/api/v1";

app.use(baseUrl + "/games", router);

//global error handler
app.use((err, _req, res, _next) => {
  console.log(err);

  if (err instanceof Error) {
    const { id, statusText, statusCode, reason } = err;
    res.status(statusCode).json({ id, status: statusText, message: reason });
    return;
  }

  res.status(400).json({
    id: errorConst.UnknownError,
    status: "Error",
    message: "An error occured!",
  });
});

app.listen(process.env.PORT, () => {
  console.log(`Api is running on port: ${process.env.PORT}`);
});
