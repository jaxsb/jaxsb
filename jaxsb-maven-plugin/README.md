# JAX-SB Maven Plugin

> Maven Plugin for [JAX-SB][jaxsb] framework

[![Build Status](https://travis-ci.org/jaxsb/jaxsb.png)](https://travis-ci.org/jaxsb/jaxsb)
[![Coverage Status](https://coveralls.io/repos/github/jaxsb/jaxsb/badge.svg)](https://coveralls.io/github/jaxsb/jaxsb)
[![Javadocs](https://www.javadoc.io/badge/org.jaxsb/jaxsb-maven-plugin.svg)](https://www.javadoc.io/doc/org.jaxsb/jaxsb-maven-plugin)
[![Released Version](https://img.shields.io/maven-central/v/org.jaxsb/jaxsb-maven-plugin.svg)](https://mvnrepository.com/artifact/org.jaxsb/jaxsb-maven-plugin)

### Introduction

The `jaxsb-maven-plugin` plugin is used to generate XML bindings with the [JAX-SB][jaxsb] framework.

### Goals Overview

* [`jaxsb:generate`](#jaxsbgenerate) generates JAX-SB bindings.

### Usage

#### `jaxsb:generate`

The `jaxsb:generate` goal is bound to the `generate-sources` phase, and is used to generate JAX-SB bindings for XSD documents in the `manifest`. To configure the generation of JAX-SB bindings for desired XML Schemas, add a `manifest` element to the plugin's configuration.

##### Example

```xml
<plugin>
  <groupId>org.jaxsb</groupId>
  <artifactId>jaxsb-maven-plugin</artifactId>
  <version>2.1.4-SNAPSHOT</version>
  <configuration>
    <destDir>${project.build.directory}/generated-sources/jaxsb</destDir>
    <schemas>
      <schema>src/main/resources/config.xsd</schema>
    </schemas>
  </configuration>
</plugin>
```

#### Configuration Parameters

| Name              | Type    | Use      | Description                                                                   |
|:------------------|:--------|:---------|:------------------------------------------------------------------------------|
| `/destDir`        | String  | Required | Destination path of generated bindings.                                       |
| `/schemas`        | List    | Required | List of `resource` elements.                                                  |
| `/schemas/schema` | String  | Required | File path of XML Schema.                                                      |

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

### License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

[mvn-plugin]: https://img.shields.io/badge/mvn-plugin-lightgrey.svg
[jaxsb]: /