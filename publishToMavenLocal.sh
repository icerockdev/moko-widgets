./gradlew -PpluginPublish publishPluginPublicationToMavenLocal
./gradlew -PlibraryPublish :widgets:publishToMavenLocal
(cd sample/ios-app && pod install)
./gradlew -PlibraryPublish :widgets-flat:publishToMavenLocal :widgets-bottomsheet:publishToMavenLocal :widgets-sms:publishToMavenLocal :widgets-datetime-picker:publishToMavenLocal :widgets-collection:publishToMavenLocal