<div align="center">

# OpManager

A java library and CLI interactive program for parsing and solving first order differential
equations through numerical methods.

Developed throughout 2020 - 2021.

</div>

## Features

- Top-down recursive parser with support for most basic arithmetic operations and functions.
- Multiple built-in procedure for solving parsed equations (Euler, Runge-Kutta, etc).
- Different procedure implementations for user flexibility.

## Compiling

You should have a JDK distribution installed in your system in order to compile,
and javac/jar should be runnable from the command line

```
git clone https://github.com/alcamiz/DiffEq
cd DiffEq
mkdir build
make
```

The jar executable should be located in the build folder, under the name ```release.jar```.

## License

[![GNU GPLv3 Image](https://www.gnu.org/graphics/gplv3-127x51.png)](http://www.gnu.org/licenses/gpl-3.0.en.html)

OpManager is Free Software: You can use, study share and improve it at your
will. Specifically you can redistribute and/or modify it under the terms of the
[GNU General Public License](https://www.gnu.org/licenses/gpl.html) as
published by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
