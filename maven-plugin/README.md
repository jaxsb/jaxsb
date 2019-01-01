# OpenJAX XSB Maven Plugin

**Maven Plugin for [XSB][xsb] framework**

### Introduction

The `xsb-maven-plugin` plugin is used to generate XML bindings with the [XSB][xsb] framework.

### Goals Overview

* [`xsb:generate`](#xsbgenerate) generates XSB bindings.

### Usage

#### `xsb:generate`

The `xsb:generate` goal is bound to the `generate-sources` phase, and is used to generate XSB bindings for XSD documents in the `manifest`. To configure the generation of XSB bindings for desired XML Schemas, add a `manifest` element to the plugin's configuration.

##### Example

```xml
<plugin>
  <groupId>org.openjax.xsb</groupId>
  <artifactId>xsb-maven-plugin</artifactId>
  <version>2.1.4-SNAPSHOT</version>
  <configuration>
    <destDir>${project.build.directory}/generated-sources/xsb</destDir>
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

### JavaDocs

JavaDocs are available [here](https://xsb.openjax.org/apidocs/).

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.

### License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

<a href="http://cooltext.com" target="_top"><img src="https://cooltext.com/images/ct_pixel.gif" width="80" height="15" alt="Cool Text: Logo and Graphics Generator" border="0" /></a>

[mvn-plugin]: https://img.shields.io/badge/mvn-plugin-lightgrey.svg
[xsb]: /