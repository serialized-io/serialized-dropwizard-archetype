# Serialized Dropwizard Archetype 

To install locally:

```
mvn clean install
```

To test locally, create a new empty directory and type:

```
mvn archetype:generate -DarchetypeGroupId=io.serialized.archetype -DarchetypeArtifactId=serialized-dropwizard-archetype
```

Fill in the `groupId`, `artifactId`, `package` and `version` of your choice.
Optionally change the suggested `aggregateRoot` and `applicationName` by answering 'N'.
