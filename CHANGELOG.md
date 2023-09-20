# Changes by Version

## [v3.0.0-SNAPSHOT](https://github.com/libj/util/compare/74a5114a561297e30fe1b8a2d3fa77666071d58d..HEAD)

## [v2.2.0](https://github.com/jaxsb/jaxsb/compare/c5841176083e1f68b6adebf5e3b20e492dd55bcc..74a5114a561297e30fe1b8a2d3fa77666071d58d) (2023-09-20)
* #15 Support HTTP 30X Follow Redirect
* #14 Don't load same package twice
* #13 Upgrade Maven Dependencies
* #12 Remove NULL facet
* #10 XML string and DOM caching
* #9 Explicit <simpleType>(s) in <union> are ignored
* #7 No value present for missing lastModified timestamp
* #6 Transition to GitHub Actions
* #5 Conflicting associations amongst same-named types and groups
* #4 attribute.form not overriding schema.attributeFormDefault
* #3 Support classpath:// scheme for schema references in jaxsb-maven-plugin
* #2 Support defaultNamespace in Bindings.parse(...)
* #1 Inherit Maven dependency versions from root POM

## [v2.1.6](https://github.com/jaxsb/jaxsb/compare/b2173ee18fed62e354378733903324ce83c6be56..c5841176083e1f68b6adebf5e3b20e492dd55bcc) (2020-05-23)
* Improve handling of `InvocationTargetException`.
* General API improvements across the codebase.
* Add `Objects.requireNonNull` checks to most APIs.
* Improve `SchemaReferenceProcessor` and `SchemaDocumentProcessor`.
* Add `skipXsd` condition to `BundleProcessor`.
* Improve binding API of `NotationWriter`.
* Support XInclude.
* Improve tests.
* Improve javadocs and xmldocs.

## [v2.1.5](https://github.com/jaxsb/jaxsb/compare/e2ffa1b50e5d95d5bad2cf78b8efb80605a0e2cd..b2173ee18fed62e354378733903324ce83c6be56) (2019-07-21)
* Expand `Bindings` API.
* Upgrade `org.openjax.xml:sax:0.9.2` to `0.9.3`.
* Upgrade `org.libj:io:0.7.5` to `0.7.6`.
* Upgrade `org.libj:jci:0.8.3` to `0.8.4`.
* Upgrade `org.libj:test:0.6.9` to `0.7.0`.
* Upgrade `org.libj:lang:0.7.4` to `0.7.5`.
* Upgrade `org.openjax.xml:datatype:0.9.2` to `0.9.3`.
* Upgrade `org.openjax.xml:dom:0.9.2` to `0.9.3`.
* Upgrade `org.openjax.maven:mojo:0.3.5` to `0.4.1`.
* Remove unlinked classes: `BindingErrorHandler`, `BindingDocument`.

## [v2.1.4](https://github.com/entinae/pom/compare/834c0404b4946b64e3aef5050507a6a7cc3d229e..e2ffa1b50e5d95d5bad2cf78b8efb80605a0e2cd) (2019-05-13)
* Initial public release.