import mongoose from "mongoose";

async function databaseConnection() {
  mongoose.set("strictQuery", false);
  const uri = await mongoose.connect(process.env.DATABASE_URL, {
    retryWrites: true,
    w: "majority",
  });
}

export default async function connectToDatabase() {
  try {
    await databaseConnection();
    console.log("Database is connected successfully!");
  } catch (error) {
    console.log(error);
  }
}
