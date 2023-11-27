FROM node:18

WORKDIR /frontend


COPY . ./

RUN npm install -g pnpm && \
    pnpm install

RUN pnpm build

EXPOSE 5173


CMD ["pnpm", "dev", "--host"]
