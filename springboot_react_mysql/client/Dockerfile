# Development Stage
FROM node:21-slim AS development

WORKDIR /app

COPY package*.json /app/
COPY public/ /app/public
COPY src/ /app/src

RUN npm install

EXPOSE 3000

CMD ["npm", "start"]

# Production Stage
FROM node:21-slim AS production

WORKDIR /app

COPY package*.json /app/
COPY public/ /app/public
COPY src/ /app/src

RUN npm install && npm run build

EXPOSE 3000

CMD ["npm", "start"]
