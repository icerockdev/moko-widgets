/*
 * Copyright 2021 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import java.util.Base64

plugins {
    id("javadoc-stub-convention")
    id("org.gradle.maven-publish")
    id("signing")
}

publishing {
    repositories.maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
        name = "OSSRH"

        credentials {
            username = System.getenv("OSSRH_USER")
            password = System.getenv("OSSRH_KEY")
        }
    }

    publications.withType<MavenPublication> {
        // Provide artifacts information requited by Maven Central
        pom {
            name.set("MOKO JavaScript")
            description.set("JavaScript code evaluation from common code for Kotlin Multiplatform Mobile")
            url.set("https://github.com/icerockdev/moko-javascript")
            licenses {
                license {
                    name.set("Apache-2.0")
                    distribution.set("repo")
                    url.set("https://github.com/icerockdev/moko-javascript/blob/master/LICENSE.md")
                }
            }

            developers {
                developer {
                    id.set("Tetraquark")
                    name.set("Vladislav Areshkin")
                    email.set("vareshkin@icerockdev.com")
                }
                developer {
                    id.set("Dorofeev")
                    name.set("Andrey Dorofeev")
                    email.set("adorofeev@icerockdev.com")
                }
                developer {
                    id.set("Alex009")
                    name.set("Aleksey Mikhailov")
                    email.set("aleksey.mikhailov@icerockdev.com")
                }
            }

            scm {
                connection.set("scm:git:ssh://github.com/icerockdev/moko-javascript.git")
                developerConnection.set("scm:git:ssh://github.com/icerockdev/moko-javascript.git")
                url.set("https://github.com/icerockdev/moko-javascript")
            }
        }
    }
}


signing {
    val signingKeyId: String? = System.getenv("SIGNING_KEY_ID")
    val signingPassword: String? = System.getenv("SIGNING_PASSWORD")
    val signingKey: String? = System.getenv("SIGNING_KEY")?.let { base64Key ->
        String(Base64.getDecoder().decode(base64Key))
    }
    if (signingKeyId != null) {
        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
        sign(publishing.publications)
    }
}
