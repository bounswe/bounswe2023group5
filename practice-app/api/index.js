import app from "./app.js";
import dotenv from "dotenv";
dotenv.config();
const baseUrl = process.env.BASE_URL || "/api/v1";

app.listen(process.env.PORT, () => {
  console.log(`Api is running on port: ${process.env.PORT}`);
});
