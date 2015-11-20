# Play Framework with Scala.js

This is a simple example application showing how you can integrate a Play project with a Scala.js project.

Based on the original ["Play Framework with Scala.js" project](https://github.com/vmunier/play-with-scalajs-example), this version focuses on examples showing common patterns of *communication* between the client (Scala.js) and the server (Play).

Examples:

* Sending and receiving plain text via WebSockets
* Sending and receiving plain text via AJAX calls (GET and POST)
* Sending and receiving serialized Scala objects
* Leveraging Play (JavaScript) routes from within Scala.js

## Modules

The application contains three directories:
* `server` Play application (server side)
* `client` Scala.js application (client side)
* `shared` Scala code that you want to share between the server and the client

## Run the application
```shell
$ sbt
> run
$ open http://localhost:9000
```

## Features

The application uses the [sbt-play-scalajs](https://github.com/vmunier/sbt-play-scalajs) sbt plugin and the [play-scalajs-scripts](https://github.com/vmunier/play-scalajs-scripts) library.

- Run your application like a regular Play app
  - `compile` simply triggers the Scala.js compilation
  - `run` triggers the Scala.js fastOptJS command on page refresh
  - `~compile`, `~run`, continuous compilation is also available
  - `start`, `stage` and `dist` generate the optimised javascript
  - [`playscalajs.html.scripts`](https://github.com/vmunier/play-with-scalajs-example/blob/c5fa9ce35954278bea903823a7f0528b1d68b5db/server/app/views/main.scala.html#L14) selects the optimised javascript file when the application runs in prod mode (`start`, `stage`, `dist`).
- Source maps
  - Open your browser dev tool to set breakpoints or to see the guilty line of code when an exception is thrown
  - Source Maps is _disabled in production_ by default to prevent your users from seeing the source files. But it can easily be enabled in production too by setting `emitSourceMaps in fullOptJS := true` in the Scala.js projects.

## IDE integration

### Eclipse

1. `$ sbt eclipse`
2. Inside Eclipse, `File/Import/General/Existing project...`, choose the root folder. Uncheck the second and the last checkboxes to only import client, server and one shared, click `Finish`. ![Alt text](screenshots/eclipse-play-with-scalajs-example.png?raw=true "eclipse play-with-scalajs-example screenshot")

### IntelliJ

In IntelliJ, open Project wizard, select `Import Project`, choose the root folder and click `OK`.
Select `Import project from external model` option, choose `SBT project` and click `Next`. Select additional import options and click `Finish`.
Make sure you use the IntelliJ Scala Plugin v1.3.3 or higher. There are known issues with prior versions of the plugin.
