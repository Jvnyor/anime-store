# anime-store

## SQL Querys

```
CREATE DATABASE `anime_store_2`;
```
```
CREATE TABLE `producer` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
)
```
```
CREATE TABLE `anime` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(300) NOT NULL,
  `episodes` int NOT NULL,
  `producer_id` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `producer_id` FOREIGN KEY (`producer_id`) REFERENCES `producer` (`id`)
)
```
