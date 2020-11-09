#set( $aggregateRootLc = $aggregateRoot.toLowerCase() )
#set( $aggregateRootLcPlural = $aggregateRootLc + "s" )
# Event Sourcing / CQRS Demo App using Dropwizard and Serialized

---

Congratulations! You have successfully generated **{$applicationName}**!

Your service are focused around the [aggregate root](https://serialized.io/ddd/aggregates) **{$aggregateRoot}** 
and it comes with an HTTP API divided into a _command_ and a _query_ part, stipulated by the [CQRS principles](https://udidahan.com/2009/12/09/clarified-cqrs/)
which in short tell us to separate _reads_ from _writes_.

**Build your service using Maven**

``` 
mvn clean install
```

**Start server**

Make sure to set/export the API keys to your Serialized project:

For Mac and Linux users:
```
export SERIALIZED_ACCESS_KEY=<your-access-key>
export SERIALIZED_SECRET_ACCESS_KEY=<your-secret-key>
```
or if you are on Windows:
```
set SERIALIZED_ACCESS_KEY=<your-access-key>
set SERIALIZED_SECRET_ACCESS_KEY=<your-secret-key>
```
Start the service:
``` 
java -jar target/${artifactId}-${version}.jar server config/config.yml
``` 

**Calling the command API of your service**

When we want to write/make state changes to our aggregate root we call the command API.

Open a shell and perform the following curl to send a _start_ command: 

```
curl -i http://localhost:8080/commands/start-${aggregateRootLc} \
  --header "Content-Type: application/json" \
  --data '
  {  
     "${aggregateRootLc}Id": "3dbc7063-76d6-4df2-8ab4-72d1f897563b"
  }
  '
```

The expected response should be something like this:

```
HTTP/1.1 201 Created
Location: http://localhost:8080/queries/${aggregateRootLcPlural}/3dbc7063-76d6-4df2-8ab4-72d1f897563b
Content-Length: 0
```
Note that the _Location_ header points to where we can access the projected read-model via the query API.

The _start_ command results in a stored event _called ${aggregateRoot}Started_ was stored in [Serialized](https://serialized.io).
Browse to the [Console](https://app.serialized.io) and have a look at it! 

**Calling the query API of your service**

When we want to get our data, we use the query API. 

Try to get the projected ${aggregateRootLc} state using this simple GET request:

```
curl -i http://localhost:8080/queries/${aggregateRootLcPlural}/3dbc7063-76d6-4df2-8ab4-72d1f897563b
```

The expected response should be something like this:

```
HTTP/1.1 200 OK
Content-Type: application/json
Vary: Accept-Encoding
Content-Length: 28

{
  "status" : "STARTED"
}
```

**Make a second state change by sending another command to the API**

Lets send a _finish_ command to put our aggregate root into its end-state:

```
curl -i http://localhost:8080/commands/finish-${aggregateRootLc} \
  --header "Content-Type: application/json" \
  --data '
  {  
     "${aggregateRootLc}Id": "3dbc7063-76d6-4df2-8ab4-72d1f897563b"
  }
  '
```

**Verify the change using the query API**

Do the same GET request as before:

```
curl -i http://localhost:8080/queries/${aggregateRootLcPlural}/3dbc7063-76d6-4df2-8ab4-72d1f897563b
```

The _status_ field should now say **FINISHED**.

**Extending the service**

Your are now ready to add your own functionality to your service!
Try to add some fields to existing commands/events, introduce new commands or play around with the projections.
Check out our [sample code](https://github.com/serialized-io/samples-java) for inspiration! 
More info regarding the [Java Client](https://github.com/serialized-io/client-java) can be 
found [on our site](https://serialized.io/code/java-guide).
Detailed info regarding the API:s can be found in the [official documentation](https://docs.serialized.io/).
