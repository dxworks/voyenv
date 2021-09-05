# Specify a base image
FROM node:12

#Install some dependencies

WORKDIR /usr/app
RUN npm install @org.dxworks/voyenv -g
