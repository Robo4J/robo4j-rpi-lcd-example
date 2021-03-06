## Robo4J-rpi-lcd-example
Robo4J RasbpberryPi LCD Demo example

Example using Robo4J to send messages between the buttons and LCD in an Adafruit 2x16 LCD shield.

The LCD can also be controlled using REST.

The internal sever is accessible on port 8025 and supports POST requests (no authentication required).

Example: <ROBO4J_IP>:8025

POST request: 
{ 
  "commands" : [
     {"name"  : "up",
     "target" : "lcd"
     }
  ]
}

## Building from Source
The Robo4j framework uses [Gradle][] to build. The easiest way is to simply build a fat jar.

```bash
./gradlew :fatJar
```

## Running 
The built fat jar can simply be run with java -jar. Just make sure you have all the required libraries on the path. 
The example can be run on all platforms where Swing is available by setting the system property com.robo4j.hw.rpi.i2c.adafruitlcd.mock to true.

For example:

```bash
java -Dcom.robo4j.hw.rpi.i2c.adafruitlcd.mock=true -jar build/libs/robo4j-rpi-lcd-example-0.5-SNAPSHOT.jar
```

Or, on a Raspberry Pi with the necessary hardware connected:

```bash
java -cp $ROBO4J_PATH:build/libs/robo4j-rpi-lcd-example-0.5-SNAPSHOT.jar com.robo4j.rpi.lcd.example.LcdExampleMain
```



The easiest way to run the example is to create the fatJar and run it.

## Requirements
* [Java JDK 8][]
* [Robo4j.io][], version alpha-0.3

## Staying in Touch
The official twitter account:
* [@robo4j][]

Author's twitter accounts:
* [@hirt][] 
* [@miragemiko][]
 
The official blog:
* [Robo4j.io][]

Author's blogs:
* [Marcus' blog][]
* [miragemiko blog][]

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
[Marcus' blog]: http://hirt.se/blogs