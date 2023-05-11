FROM node:18

WORKDIR /app/frontend

COPY package*.json ./

RUN npm install

COPY . .

ENV PORT=3000

EXPOSE 3000

CMD [ "npm", "start"]
