FROM node:18

WORKDIR /frontend

COPY package.json pnpm-lock.yaml ./

RUN npm install -g pnpm && \
    pnpm install

COPY . ./

RUN pnpm build

EXPOSE 5173


CMD ["pnpm", "dev", "--host"]
