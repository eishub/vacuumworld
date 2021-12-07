# The Vacuum World

<img align="right" src="https://github.com/eishub/vacuumworld/wiki/vacuumworld.png"/>

The vacuum environment is a 2-dimensional grid world, populated with fixed obstacles, dust, and vacuum cleaning robots called *VacBots*. The VacBots are controllable entities that can be controlled by agents. Their task is to move around the grid, avoiding the obstacles, and finding and cleaning the colourful dust.

## World Configuration

In the default configuration, the size of the world is 16 x 8 squares; there are 8 randomly-placed obstacles, 32 random dusty squares, and 4 randomly-placed VacBots. Dust, once cleaned, does not regenerate. This default configuration can be changed by editing the file `ita.conf`, which also includes documentation for each configuration option.

You can use the init parameters to set the configuration file. The init parameters are

| parameter name | description |
|:---------------|:------------|
| configfile | name of config file (relative path to the directory containing the vacuumworld jar file) |
| level | level name. Either relative path (relative path to the directory containing the vacuumworld jar file), or a string with a number in range [0,8]. If the file is not found, the default 20x12 map is used.|
| generate | A string indicating the required regeneration pattern. Can have the values "no",  "X s" (regenerates dust X seconds after it's removed) or "P %" (generates dust on every square with a probability of P % per second)  |

### Level file
Level files are files with a ".vwl" extension. They are usually in the same directory as the vacuumworld jar file.
The file contains the contents of the map grid, one character per grid point, one row per map row. All rows should be equally long. The lowest grid line in the map is the first row in the file. The characters are as follows:

| character | map element |
|:----------------|:---------------|
| whitespace | empty square (can be filled with dust randomly) |
| . | dust |
| X | wall |
| n, e, s, w, N, E, S, W | initial orientations for bots at this place; you can have at most 8 of these in the map. |
| # as first char | comment line |

### Config File
A config file is basically a Properties file, containing two lines (no commas). One line must be "level = ...", the other "generate = ..." (without the double quotes), just like you would in the MAS file but *without the double quotes*.

## VacBots

**Decco**, **Harry**, **Henry**, **Lloyd**, **Keano**, **Stevo**, **Benjy**, and **Darth** are the names of available VacBots in the Vacuum World. Their entity type is **vacbot**. Note that, in the default configuration, only the first four of these are available.

### Actions
VacBots can **turn**, **move** around the grid, **clean** dust, and **flash** their warning lights:

* **move(Identifier)**: Moves the VacBot one square in the specified direction, turning it first if necessary. Valid directions can be absolute (**north**, **south**, **east**, or **west**) or relative (**forward**, **left**, **right**, or **back**). The VacBot carries out the requested move on a best-effort basis. If the moving VacBot meets another VacBot, a permanent obstacle, or the edge of the grid, it halts.
* **move(Identifier, Numeral)**: Moves the VacBot in the specified direction, for the specified distance, turning it first if necessary. Valid directions are specified as for the move action above. The distance is a positive integer representing the number of squares to be moved; a distance value of 0 can be used to turn the entity without moving. The VacBot carries out the requested move on a best-effort basis. If the moving VacBot meets another VacBot, a permanent obstacle,
or the edge of the grid, it halts.
* **light(Identifier)**: Turns the warning light on or off.
* **clean**: Cleans the square currently occupied by the VacBot, if it is dusty. If the square is already clean, this action returns immediately.

All actions except for **light** take some time to complete, during which any other requested actions are discarded (i.e. ignored).

### Percepts

VacBots receive the following percepts:

* **location(Numeral, Numeral)**: The VacBot’s current location on the grid, in (X,Y) coordinates. The square in the top left corner is numbered (0,0). This percept is only available when the VacBot is not moving between squares.
* **direction(Identifier)**: The VacBot’s current absolute direction, one of **north**, **south**, **east**, or **west**. This percept is only available when the VacBot is not turning or cleaning.
* **light(Identifier)**: The status of the Vacbot’s light, either **on** or **off**.
* **square(Identifier squareName, Identifier squareContents)**: Six instances of this percept represent the VacBot’s field of vision. The square name is one of **left**, **forwardLeft**, **forward**, **forwardRight**, **right**, or **here**. The square contents is one of **obstacle**, **vac**, **dust**, or **empty**. Note that if the VacBot perceives another VacBot occupying a dusty square, the square contents will take the value **vac**. Note also that the VacBot does not perceive itself; the square contents for the square **here** is always either **dust** or **empty**. 
* **task(Identifier)**: The VacBot’s current task. The possible tasks are **turn**, **move**, and **clean**; or **none** if the VacBot is idle.

The **location**, **direction** and **square** percepts are only updated when the VacBot is not currently moving, turning, or cleaning.

## Acknowledgements
This environment was developed and provided by [Rem Collier](https://www.csi.ucd.ie/users/rem-collier) and [Howell Jordan](https://www.csi.ucd.ie/users/howell-jordan) from [UCD School of Computer Science and Informatics](https://www.csi.ucd.ie/).
