#URL Shortener
Tags: Scala, Play Framework, Slick, play-slick, PostgreSQL, Backbone, jQuery, Knockout
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

##Project setup guide and tips for beginners:
1. Use ``activator new _<project-name>_ play-scala`` to create a skeleton project in current directory
2. If you want to use ``activator gen-idea`` command to import project into Intellij, you need to add ``addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")`` into __plugins.sbt__ inside __project__ folder
3. Add endpoints in __routes__ file
4. Handle request and JSON in __Controllers__, all application logic should go into __Services__ files. The best practice is not to access database persistance at __Controller__ level.
5. play-slick setup and PostgreSQL connection:

- In __build.sbt__ remove ``jdbc`` from ``libraryDependencies`` (This is an important step, since you will be using postgres jdbc and you will no longer need the default jdbc)
- Add 
```scala
  "com.typesafe.play" %% "play-slick" % "2.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "2.0.0",
  "org.postgresql" % "postgresql" % "9.4.1209.jre7" 
```
to the ``libraryDependencies`` (Note: by adding "play-slick-evolutions" it will enable db evolution)
- In __application.conf__, add the following settings to configure postgresql driver (This only works for __play-slick version 2.0.0__ which uses __slick3__ interface and it is totally different from the previous version aka slick 2.x)
```scala
slick.dbs.default {
  driver = "slick.driver.PostgresDriver$"
  db.driver = "org.postgresql.Driver"
  db.url = "jdbc:postgresql://localhost:5432/<your_db_name>"
  db.user = "postgres"
  db.password = "postgres"
}
```
Note: localhost port, user and password is configurable as long as they are same as the ones in postgres.
Important: You have to manually create a database in postgreSQL under that port with the name _<your_db_name>_. Otherwise, you will get a SQLTimeout exception when you start your server.
- In your persistance level code, use dependency injection to get ``DatabaseConfigProvider`` which will be your __default__ setting as above.
- If you came from slick2.x, you don't need to provide ``implicit session: Session`` anymore when you call db
- In order to setup table and columns in code, you need to import the following packages:
```scala
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.driver.JdbcProfile
import slick.driver.PostgresDriver.api._
```
- After you have done the persistance level code, you need to create a SQL evolution script under __conf/evolutions/default__ (create these folders if you don't have) before you start the server. The evolution script should contain twos part, up script and down script. Play will automatically detect the change of the evolution script and ask you to apply the up script when you go to your localhost page.

If you have any questions or if you find any errors in my code or documentation, don't hesitate to fire an issue :)
