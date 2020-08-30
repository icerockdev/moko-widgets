/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import org.gradle.api.publish.plugins.PublishingPlugin

class BintrayPublishingPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        // schedule after evaluate action...but no guarantee that our action will be last.
        // so we just shedule another afterEvaluate in our action to set action at end of evaluation :)
        target.afterEvaluate {
            target.afterEvaluate { setupNoValidationPublish(target) }
        }
    }

    private fun setupNoValidationPublish(project: Project) {
        val publishNoValidationTasks = project.tasks.filterIsInstance<PublishToMavenRepository>()
            .map { publishTask ->
                val newName = publishTask.name.replace("publish", "publishNoValidation")
                val newTask: PublishToBintrayRepository =
                    project.tasks.create(newName, PublishToBintrayRepository::class.java)

                newTask.group = PublishingPlugin.PUBLISH_TASK_GROUP + " no validation"
                newTask.publication = publishTask.publication
                newTask.repository = publishTask.repository
                newTask.setDependsOn(publishTask.dependsOn)

                newTask
            }

        publishNoValidationTasks
            .groupBy { it.repository }
            .forEach { (repo, publishTasks) ->
                val repoName = repo.name.capitalize()
                project.tasks.create("publishNoValidationTo$repoName") {
                    it.group = PublishingPlugin.PUBLISH_TASK_GROUP + " no validation"
                    it.setDependsOn(publishTasks)
                }
            }
    }
}
