import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport
import org.gradle.api.tasks.testing.Test

apply(plugin = "jacoco")

tasks.named<Test>("test") {
    useJUnitPlatform()

    testLogging {
        events("FAILED", "SKIPPED", "PASSED")
        exceptionFormat = TestExceptionFormat.FULL
        showStandardStreams = false
    }

    finalizedBy("jacocoTestReport")
}

extensions.configure<JacocoPluginExtension>("jacoco") {
    toolVersion = "0.8.12"
}

tasks.named<JacocoReport>("jacocoTestReport") {
    dependsOn("test")
    reports {
        html.required.set(true)
        xml.required.set(true)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("reports/jacoco"))
    }
}

// Cấu hình rule coverage
tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    violationRules {
        rule {
            element = "CLASS"
            excludes = listOf(
                "com.sope.sope_ecommnerce_backend.dto.**",
                "com.sope.sope_ecommnerce_backend.configuration.**",
                "com.sope.sope_ecommnerce_backend.exception.**",
                "com.sope.sope_ecommnerce_backend.SopeEcommerBackendApplication"
            )
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.00".toBigDecimal()
            }
        }
    }
}

tasks.named("check") {
    dependsOn("jacocoTestCoverageVerification")
}
