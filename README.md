<img alt="logo" src="https://www.objectionary.com/cactus.svg" height="100px" />

[![EO principles respected here](https://www.elegantobjects.org/badge.svg)](https://www.elegantobjects.org)
[![DevOps By Rultor.com](http://www.rultor.com/b/objectionary/eo-files)](http://www.rultor.com/p/objectionary/aoi)
[![We recommend IntelliJ IDEA](https://www.elegantobjects.org/intellij-idea.svg)](https://www.jetbrains.com/idea/)

[![mvn](https://github.com/objectionary/ddr/actions/workflows/build.yml/badge.svg?branch=master)](https://github.com/objectionary/aoi/actions/workflows/build.yml)
[![Hits-of-Code](https://hitsofcode.com/github/objectionary/aoi)](https://hitsofcode.com/view/github/objectionary/aoi)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](https://github.com/objectionary/aoi/blob/master/LICENSE.txt)

AOI is a project aiming at performing Abstract Object Inference for [EO](https://www.eolang.org) Programs.

### Launch

#### Using Maven
Just add this to your `pom.xml`

```
<dependency>
  <groupId>org.eolang</groupId>
  <artifactId>aoi</artifactId>
  <version>0.0.2</version>
</dependency>
```

And then you will be able to use the tool like this:
```
import org.objectionary.aoi.launch.launch
...
launch(${path_to_your_directory})
```

`launch` method takes path to your directory as an input. After `launch` completes - a new directory
with a name `$(path_to_your_directory}_aoi` will be created near your source directory, containing modified xmirs.

### Pipeline

The tool receives a path to the directory with .xmir files as an input.
AOI generates a new `${PATH}_aoi` directory, located near your input directory, with modified xmir files.

Let's see how it modifies .xmir files on an example.
Consider the following EO program:
```
[] > cat
  [] > talk
    QQ.io.stdout > @
      "Meow!"

[] > dog
  [] > talk
    QQ.io.stdout > @
      "Woof!"
  [] > eat
    QQ.io.stdout > @
      "I am eating"

[x] > pet1
  x.talk > @

[x] > pet2
  seq > @
    x.talk
    x.eat
```

The following block in the corresponding .xmir file will be generated from this program:

```
<aoi>
    <obj fqn="pet1.x">
       <inferred>
          <obj fqn="cat"/>
          <obj fqn="dog"/>
       </inferred>
    </obj>
    <obj fqn="pet2.x">
       <inferred>
          <obj fqn="dog"/>
       </inferred>
    </obj>
</aoi>
```

As we can see, object `x` from `pet1` is only used with its `talk` attribute, therefore it can either be
an instance of `cat` or `dog`. Whereas `x` located `pet2` is used with both `talk` and `eat`, which
lets us determine that `x` can only be an instance of `dog`.

Each free attribute and each object in the program have unique fully qualified names, so these names
are used in the aoi section to unequivocally define each object.

### How to Contribute
Fork repository, make changes, send us a pull request. We will review your changes and apply them to the master branch shortly, provided they don't violate our quality standards. To avoid frustration, before sending us your pull request please run full Maven build:
```
$ mvn clean install -Pqulice
```
You will need Maven 3.3+ and Java 8+.
