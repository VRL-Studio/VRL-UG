# VRL UG Project

## How To Build This Project

### 1. Dependencies

- JDK >= 1.8
- UG4 binaries
- Internet Connection (other dependencies will be downloaded automatically)
- Optional: IDE with [Gradle](http://www.gradle.org/) support


### 2. Configuration (Optional)

If the plugin shall be installed to a custom destination, specify the path in `build.properties`, e.g.,
    
    # vrl property folder location (plugin destination)
    vrldir=/path/to/.vrl/0.4.3/myvrl
    
Otherwise, the plugin will be installed to the default location (depends on VRL version that is specified in the gradle dependencies).

### 3. Build & Install

#### Adding UG4 Binaries 

UG4 binaries should be placed in the resource folder as follows

```
VRL-UG/src/main/resources/eu/mihosoft/vrl/plugin/content/natives/osx/natives.zip
VRL-UG/src/main/resources/eu/mihosoft/vrl/plugin/content/natives/linux/x64/natives.zip
VRL-UG/src/main/resources/eu/mihosoft/vrl/plugin/content/natives/windows/x64/natives.zip
```
The natives.zip contains the ug library:

##### macOS:

```
libug4.dylib
```

##### Linux:

```
libug4.so
```

##### Windows:

```
ug4.dll
```

In addition to the `ug4.dll` the windows archive should contain the redistributable dlls, e.g.:

```
vcruntime140.dll
msvcp140.dll
*.dll
``` 

#### IDE

To build the project from an IDE do the following:

- open the  [Gradle](http://www.gradle.org/) project
- call the `installVRLPlugin` Gradle task to build and install the plugin
- restart VRL-Studio

#### Command Line

Building the project from the command line is also possible.

Navigate to the project folder and call the `installVRLPlugin` [Gradle](http://www.gradle.org/)
task to build and install the plugin.

##### Bash (Linux/OS X/Cygwin/other Unix-like OS)

    cd Path/To/VRL-UG/
    ./gradlew installVRLPlugin
    
##### Windows (CMD)

    cd Path\To\VRL-UG\
    gradlew installVRLPlugin

Finally, restart VRL-Studio
