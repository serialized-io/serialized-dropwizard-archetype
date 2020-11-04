# Event Sourcing / CQRS Demo App using Dropwizard and Serialized

---

**Build**

``` 
mvn clean install
```

**Start server**

``` 
export SERIALIZED_ACCESS_KEY=<your-access-key>
export SERIALIZED_SECRET_ACCESS_KEY=<your-secret-key>

java -jar target/${artifactId}-${version}.jar server config/config.yml
``` 

