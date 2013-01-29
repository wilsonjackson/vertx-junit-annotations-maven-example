# Example Maven project using [vertx-junit-annotations](https://github.com/vert-x/vertx-junit-annotations) for integration testing

This project makes use of the Maven Failsafe plugin to run integration tests in the standard integration-tests phase,
after packaging occurs.

For those unfamiliar with Failsafe:

Integration test classes live in `src/test/java`, and are differentiated from unit tests by naming convention:
unit tests typically end in `Test.java` or `TestCase.java` while integration tests end in `IT.java`. (There may be a
little more to it than that, but you get the idea.)

Failsafe ensures no classloader shenanigans occur, so Vert.x won't ever yell at you for putting user libraries in its
classpath. Vert.x hates it when you do that, and so does Failsafe, so they get along pretty well.

## Configuring the POM

If you're testing a module, there are two steps required to properly configure the POM:

- Packaging the module according to Vert.x conventions, and
- Placing the exploded module into a working directory (`test-mods` in this example) where the Vert.x runtime will find it during tests.

The working directory is particularly important if your module depends on other modules, as Vert.x will need a place to
download your dependencies.

If you're only testing a Verticle, things aren't so complicated, though any reliance on other modules still require a
working directory.

## Writing tests

Tests are written precisely in accordance with the documentation of vertx-junit-annotations. In this example project I
use a `CountDownLatch` to force each test method to wait for handlers to execute, coupled with a `timeout` on the
`@Test` annotation in case they never execute at all. In a more complicated scenario, this would probably get pretty
unwieldy pretty quickly, but the same basic principle would apply: you need to request, wait, verify.

One difficulty I've found is that `VertxJUnit4ClassRunner` currently executes tests immediately, without waiting for the
Vert.x instance to be ready. This means if your tested code takes awhile to start (for instance, if it depends on other
modules that Vert.x has to download and install) your first test may time out before Vert.x is ready. Nothing in this
example project will prevent that from happening; hopefully it willbe addressed in vertx-junit-annotations soon.
