# Development Stage
FROM node:21-slim AS development

WORKDIR /springboot_react_mysql/client

COPY package*.json /springboot_react_mysql/client
COPY public/ /springboot_react_mysql/client/public
COPY src/ /springboot_react_mysql/client/src

RUN npm install

EXPOSE 3000

CMD ["npm", "start"]

# Production Stage
FROM node:21-slim AS production

WORKDIR /springboot_react_mysql/client

COPY package*.json /springboot_react_mysql/client/
COPY public/ /springboot_react_mysql/client/public
COPY src/ /springboot_react_mysql/client/src

RUN npm install && npm run build

EXPOSE 3000

CMD ["npm", "start"]
