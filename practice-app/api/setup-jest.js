import mongoose from "mongoose";
afterAll(async () => {
    console.log("Disconnecting to DB");
    await mongoose.connection.close();
});