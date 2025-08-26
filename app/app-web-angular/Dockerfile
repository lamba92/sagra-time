FROM node:22.18.0-alpine3.22

COPY browser/ browser/
COPY server/ server/
COPY /prerendered-routes.json /prerendered-routes.json

CMD ["node", "/server/server.mjs"]
