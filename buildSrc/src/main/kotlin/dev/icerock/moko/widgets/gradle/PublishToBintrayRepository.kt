/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.gradle

import org.gradle.api.publish.maven.internal.publisher.BintrayPublisher
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository

open class PublishToBintrayRepository : PublishToMavenRepository() {
    override fun publish() {
        val remotePublisher = BintrayPublisher(temporaryDirFactory, project.gradle.startParameter)
        val normalizedPublication = publicationInternal.asNormalisedPublication()
        remotePublisher.publish(normalizedPublication, repository)
    }
}
