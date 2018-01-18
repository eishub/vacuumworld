# The Vacuum World

<img align="right" src="https://github.com/eishub/vacuumworld/wiki/vacuumworld.png"/>

The vacuum environment is a 2-dimensional grid world, populated with fixed obstacles, dust, and vacuum cleaning robots called *VacBots*. The VacBots are controllable entities that can be controlled by agents. Their task is to move around the grid, avoiding the obstacles, and finding and cleaning the colour-ful dust.

## World Configuration

In the default configuration, the size of the world is 16 x 8 squares; there are 8 randomly-placed obstacles, 32 random dusty squares, and 4 randomly-placed VacBots. Dust, once cleaned, does not regenerate. This default configuration can be changed by editing the file `ita.conf`, which also includes documentation for each configuration option.

You can use the init parameters to set the configuration file. The init parameters are

| parameter name | description |
|:---------------|:------------|
| configfile | name of config file (relative path, from directory containing the environment jar file |
| level | level name. Either relative path, or a string with a number in range [0,8]. |
| generate | "no",  "X s" (Regenerates dust X seconds after it's removed) or "P %"
(generates dust on every square with a probability of P % per second)  |


## VacBots

**Decco**, **Harry**, **Henry**, **Lloyd**, **Keano**, **Stevo**, **Benjy**, and **Darth** are the names of available VacBots in the Vacuum World. Note that, in the default configuration, only the first four of these are available.

### Actions
VacBots can **turn**, **move** around the grid, **clean** dust, and **flash** their warning lights:

* **move(Identifier)**: Moves the VacBot one square in the specified direction, turning it first if necessary. Valid directions can be absolute (**north**, **south**, **east**, or **west**) or relative (**forward**, **left**, **right**, or **back*). The VacBot carries out the requested move on a best-effort basis. If the moving VacBot meets another VacBot, a permanent obstacle, or the edge of the grid, it halts.
* **move(Identifier, Numeral)**: Moves the VacBot in the specified direction, for the specified distance, turning it first if necessary. Valid directions are specified as for the move action above. The distance is a positive integer representing the number of squares to be moved; a distance value of 0 can be used to turn the entity without moving. The VacBot carries out the requested move on a best-effort basis. If the moving VacBot meets another VacBot, a permanent obstacle,
or the edge of the grid, it halts.
* **light(Identifier)**: Turns the warning light on or off.
* **clean**: Cleans the square currently occupied by the VacBot, if it is dusty. If the square is already clean, this action returns immediately.

### Percepts

VacBots receive the following percepts:

* **location(Numeral, Numeral)**: The VacBot’s current location on the grid, in (X,Y) coordinates. The square in the top left corner is numbered (0,0). This percept is only available when the VacBot is not moving between squares.
* **direction(Identifier)**: The VacBot’s current absolute direction, one of **north**, **south**, **east**, or **west**. This percept is only available when the VacBot is not turning or cleaning.
* **light(Identifier)**: The status of the Vacbot’s light, either **on** or **off**.
* **square(Identifier squareName, Identifier squareContents)**: Six instances of this percept represent the VacBot’s field of vision. The square name is one of **left**, **forwardLeft**, **forward**, **forwardRight**, **right**, or **here**. The square contents is one of **obstacle**, **vac**, **dust**, or **empty**. Note that if the VacBot perceives another VacBot occupying a dusty square, the square contents will take the value **vac**. Note also that the VacBot does not perceive itself; the square contents for the square **here** is always either **dust** or **empty**. 
* **task(Identifier)**: The VacBot’s current task. The possible tasks are **turn**, **move**, and **clean**; or **none** if the VacBot is idle.

All of these percepts except for the **task** percept are only available when the VacBot is not moving, turning, or cleaning.


## EIS Interface Details

* The **move** actions will return a busy percept if it is invoked while the VacBot is moving or cleaning. If the moving VacBot meets another VacBot, a permanent obstacle, or the edge of the grid, it halts and returns a busy percept. The percept parameter indicates whether the failure was temporary (caused by another VacBot) or permanent (caused by a fixed obstacle or the edge of the grid).
* The **clean** action will return a busy percept if it is invoked while the VacBot is moving or cleaning.
It will return a bump percept if there is nothing to clean at that spot.
* No environment management commands are supported. The environment runs until it is terminated by the user or by the operating system.

## Acknowledgement
This environment was developed and provided by [Rem Collier](https://www.csi.ucd.ie/users/rem-collier) and [Howell Jordan](https://www.csi.ucd.ie/users/howell-jordan) from [UCD School of Computer Science and Informatics](https://www.csi.ucd.ie/).

Dependency information 
=====================

```
<repository>
 <id>eishub-mvn-repo</id>
 <url>https://raw.github.com/eishub/mvn-repo/master</url>
</repository>
```
	
```	
<dependency>
	<groupId>eishub</groupId>
	<artifactId>vacuumworld</artifactId>
	<version>1.1.0</version>
</dependency>
```

[Documentation](https://goalapl.atlassian.net/wiki/display/ENV/Vacuum+World) for
the Vacuum World.
