## Robo4J-rpi-lcd-example
Simple RasbpberryPi / Arduino demo client. The client is the tank platform.

The platform supports following movements: forward, backward, left, right.
The client provides http server.

The internal sever is accessiable on the port 8025 and supports POST 
request where no authentication is not required.

example : <CLIENT_BRICK_IP>:8025

POST reuest: 

{ 
  "commands" : [
     {"name"  : "stop",
     "target" : "platform"
     }
  ]
}

POST response: No Information about POST

## Building from Source
The Robo4j framework uses a [Gradle][] building system
It's required to create fatJar file and run.


## Requirements
* [Java JDK 8][]
* [Robo4j.io][] :: version: alfa-0.2

## Staying in Touch
Follow [@robo4j][] or authors: [@miragemiko] , [@hirt][]
on Twitter. In-depth articles can be found at [Robo4j.io][] or [miragemiko blog][]

## License
The Robo4j.io Framework is released under version 3.0 of the [General Public License][].

[Robo4j.io]: http://www.robo4j.io
[miragemiko blog]: http://www.miroslavkopecky.com
[General Public License]: http://www.gnu.org/licenses/gpl-3.0-standalone.html0
[@robo4j]: https://twitter.com/robo4j
[@miragemiko]: https://twitter.com/miragemiko
[@hirt]: https://twitter.com/hirt
[Gradle]: http://gradle.org
[Java JDK 8]: http://www.oracle.com/technetwork/java/javase/downloads
[Git]: http://help.github.com/set-up-git-redirect
[Robo4j documentation]: http://www.robo4j.io/p/documentation.html
