## Typesafe Interpolation


![alt text](https://travis-ci.org/afsalthaj/safe-string-interpolation.svg?branch=master)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/safe-string-interpolation/community?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.afsalthaj/safe-string-interpolation_2.12.svg)](http://search.maven.org/#search|gav|1|g%3A%22io.github.afsalthaj%22%20AND%20a%3A%22safe-string_2.12%22)


An insanely simple type driven approach to string interpolation, aiming at consistent, secure,  and only-human-readable logs and console outputs, and for safe string operations ! 

```scala
@ import $ivy.`io.github.afsalthaj::safe-string-interpolation:2.1.1`
import $ivy.$

@ import com.thaj.safe.string.interpolator._
import com.thaj.safe.string.interpolator._

@ case class Port(int: Int) extends AnyVal
defined class Port

@ val port = Port(1)
port: Port = Port(1)

@ ss"Db details: Port is ${port}"
cmd7.sc:1: could not find implicit value for parameter ev: com.thaj.safe.string.interpolator.Safe[ammonite.$sess.cmd5.Port]
val res7 = ss"Db details: Port is ${port}"
           ^
Compilation Failed

@ ss"Db details: Port is ${port.int}"
res7: SafeString = SafeString("Db details: Port is 1")

@ import instances._
import instances._

@ ss"Db details: Port is ${port}"
res9: SafeString = SafeString("Db details: Port is { int : 1 }")

@

```
Checkout the project [website](https://afsalthaj.github.io/safe-string-interpolation/) for more features and possibilities.
