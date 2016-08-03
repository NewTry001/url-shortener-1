#URL Shortener
Tags: Scala, Play Framework, Slick, play-slick, Backbone, jQuery, Knockout
![url-shortener](http://tanghaoji.github.io/images/url-shortener.png)
##Description:
A sample project to demonstrate how to create RESTful API using Play Framework combined with external WebService, Slick3 repository, PostgreSQL database, and front-end framework

##Versions:
play 2.5.4, play-slick 2.0.0, scala 2.11.7, PostgreSQL 9.5.3

##Installation:
### Requirement
- Install Java
- Download [Activator](https://www.lightbend.com/activator/download)
- Make sure you have added ``activator`` into your __PATH__

### Database connection
- Install [PostgreSQL](https://www.postgresql.org/download/)
- Open pgAdim and create a local host port 5432 with username __postgres__ and password __postgres__
- Under this port, create a new database named __url_shortener__

### Start server
- Open Command/Terminal, ``cd`` to the project root
- Enter ``activator run``, it will take some time to download the required libraries and resolve dependencies
- When you see ``(Server started, use Ctrl+D to stop and go back to the console...)``, go to URL http://localhost:9000
- It will compile all the source code, and go to homepage
![db-evolution](http://tanghaoji.github.io/images/url-shortener-evolution.png)
- You will see this page if you start the server for the first time. Don't be paninc, just click __Apply this script now!__

##Tests:
_TODO_

##Project setup tips and tutorial:
_Under construction_
