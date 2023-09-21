# JAX-SB Maven Plugin

[![Build Status](https://github.com/jaxsb/jaxsb/actions/workflows/build.yml/badge.svg)](https://github.com/jaxsb/jaxsb/actions/workflows/build.yml)
[![Coverage Status](https://coveralls.io/repos/github/jaxsb/jaxsb/badge.svg)](https://coveralls.io/github/jaxsb/jaxsb)
[![Javadocs](https://www.javadoc.io/badge/org.jaxsb/jaxsb-maven-plugin.svg)](https://www.javadoc.io/doc/org.jaxsb/jaxsb-maven-plugin)
[![Released Version](https://img.shields.io/maven-central/v/org.jaxsb/jaxsb-maven-plugin.svg)](https://mvnrepository.com/artifact/org.jaxsb/jaxsb-maven-plugin)
![Snapshot Version](https://img.shields.io/nexus/s/org.jaxsb/jaxsb-maven-plugin?label=maven-snapshot&server=https%3A%2F%2Foss.sonatype.org)

## Introduction

The `jaxsb-maven-plugin` plugin is used to generate XML bindings with the [JAX-SB][jaxsb] framework.

## Goals Overview

* [`jaxsb:generate`](#jaxsbgenerate) generates <ins>JAX-SB</ins> bindings.

## Usage

### `jaxsb:generate`

The `jaxsb:generate` goal is bound to the `generate-sources` phase, and is used to generate <ins>JAX-SB</ins> bindings for XSD documents in the `manifest`. To configure the generation of <ins>JAX-SB</ins> bindings for desired XML Schemas, add a `manifest` element to the plugin's configuration.

#### Example

```xml
<plugin>
  <groupId>org.jaxsb</groupId>
  <artifactId>jaxsb-maven-plugin</artifactId>
  <version>2.2.0</version>
  <configuration>
    <destDir>${project.build.directory}/generated-sources/jaxsb</destDir>
    <schemas>
      <schema>src/main/resources/config.xsd</schema>
    </schemas>
  </configuration>
</plugin>
```

### Configuration Parameters

| Name                               | Type              | Use                | Description                                                                                   |
|:-----------------------------------|:------------------|:-------------------|:----------------------------------------------------------------------------------------------|
| <samp>/overwrite¹</samp><br>&nbsp; | Boolean<br>&nbsp; | Optional<br>&nbsp; | Whether existing files are to be overwritten.<br>&nbsp;&nbsp;&nbsp;&nbsp;**Default:** `true`. |
| <samp>/destDir¹</samp>             | String            | Required           | Destination path of generated bindings.                                                       |
| <samp>/schemas¹</samp>             | List              | Required           | List of `schema` elements.                                                                    |
| <samp>/schemas/schemaⁿ</samp>      | String            | Required           | File path of XML Schema.                                                                      |
| <samp>/includes¹</samp>            | List              | Optional           | List of namespace URIs to include.                                                            |
| <samp>/includes/includeⁿ</samp>    | String            | Optional           | Namespace URI to include during generation of bindings.                                       |
| <samp>/excludes¹</samp>            | List              | Optional           | List of namespace URIs to exclude.                                                            |
| <samp>/excludes/excludeⁿ</samp>    | String            | Optional           | Namespace URI to exclude during generation of bindings.                                       |

## Contributing

Pull requests are welcome. For major changes, please [open an issue](../../issues) first to discuss what you would like to change.

Please make sure to update tests as appropriate.

## License

This project is licensed under the MIT License - see the [LICENSE.txt](LICENSE.txt) file for details.

[jaxsb]: /