plugins {
    id("tv.radiotherapy.tools.java-library-conventions")
    id("jvm-test-suite")
}

// Define a separate test suite for integration tests.
testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }

        register<JvmTestSuite>("integrationTest") {
            dependencies{
                implementation(project())
            }

            targets {
                targets {
                    all {
                        testTask.configure {
                            shouldRunAfter(test)
                        }
                    }
                }
            }
        }
    }
}

tasks.named("check") {
    dependsOn(testing.suites.named("integrationTest"))
}