#!/usr/bin/env bash

spring init \
--boot-version=3.0.2 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=gallery-service \
--package-name=com.gallery.galleryservice \
--groupId=com.gallery.galleryservice \
--dependencies=web \
--version=1.0.0-SNAPSHOT \
gallery-service

spring init \
--boot-version=3.0.2 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=exhibition-service \
--package-name=com.gallery.exhibitionservice \
--groupId=com.gallery.exhibitionservice \
--dependencies=web \
--version=1.0.0-SNAPSHOT \
exhibition-service

spring init \
--boot-version=3.0.2 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=painting-service \
--package-name=com.gallery.paintingservice \
--groupId=com.gallery.paintingservice \
--dependencies=web \
--version=1.0.0-SNAPSHOT \
painting-service

spring init \
--boot-version=3.0.2 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=sculpture-service \
--package-name=com.gallery.sculptureservice \
--groupId=com.gallery.sculptureservice \
--dependencies=web \
--version=1.0.0-SNAPSHOT \
sculpture-service

spring init \
--boot-version=3.0.2 \
--build=gradle \
--type=gradle-project \
--java-version=17 \
--packaging=jar \
--name=api-gateway \
--package-name=com.gallery.apigateway \
--groupId=com.gallery.apigateway \
--dependencies=web \
--version=1.0.0-SNAPSHOT \
api-gateway

