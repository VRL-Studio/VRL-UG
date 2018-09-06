# VRL-UG Plugin

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
The `natives.zip` contains the ug library:

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

In addition to the `ug4.dll` the windows archive should contain the redistributable dlls (see Visual Studio documentation), e.g.:

```
vcruntime140.dll
msvcp140.dll
*.dll
``` 

##### Hints for building UG:

To be used as VRL plugin UG should be compiled with the CMake options `-DTARGET=vrl`and `-DEMBEDDED_PLUGINS=ON`.

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

### Generating Java API for UG4

After installing and restarting VRL-Studio the VRL-UG plugin will automatically initiate the code generation and compilation of the Java binding which shows up as separate plugin (VRL-UG-API). If the compilation of the binding API fails, it is most likely an error related to class groups which don't have a common base class. 

As an alternative to the automatic build process, one can manually compile the generated gradle project. To do so, follow these steps:

- install Gradle, e.g. via `brew install gradle`.
- navigate to the generated gradle project inside the tmp folder
- run `gradle installVRLPlugin`
- start VRL-Studio

Here's an example output from the code generator during VRL-Studio start:

```
>> UG-Build-Location (Automatic Compilation): /Users/myname/.vrl/0.4.3/default/tmp/0/4a2b6216-1b1b-4675-a458-c5d4a78b4f3c
>> UG-Build-Location (Gradle Project):        /Users/myname/.vrl/0.4.3/default/tmp/0/e555b170-54de-45d8-ac46-eb40b6602ad5/VRL-UG-API
>> API code generation done.
```

If you keep VRL-Studio running, automatic compilation will continue. As soon as the output shows `>> API code generation done.` you can quit VRL-Studio if you'd prefer to compile the code manually.

Compiling the project:

```
cd /Users/myname/.vrl/0.4.3/default/tmp/0/e555b170-54de-45d8-ac46-eb40b6602ad5/VRL-UG-API
gradle installVRLPlugin
```


  

