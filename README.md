# JAX-RS Sample application for Quarkus
A very simple JAX-RS sample application that implements a few services.  This version is for
deployment to a smaller environment such as a Docker or microservice type environment. 

Deployment
----

To build, simply run

```mvn clean package```

This will create a standalone jar file that can be run standalone.  It produces
`target/jaxrs-sample-quarkus--1.0.0-SNAPSHOT.jar` that can then be run with
`java -jar target/jaxrs-sample-quarkus--1.0.0-SNAPSHOT.jar`  It also runs the unit tests to validate the server.



## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
mvn quarkus:dev
```

The application listens on port 8080.

## Creating a native executable

You need GraalVM and Docker in your environment to run this.  If GraalVM is not available a suitable Docker will be downloaded
and used.

You can create a native executable using: `mvn package -Pnative`.

Or you can use Docker to build the native executable using: `mvn package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your binary: `./target/jaxrs-sample-quarkus-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult [the Quarkus guide](https://quarkus.io/guides/building-native-image-guide).

Sample Service Info
----
There are a total of three service endpoints:

```GET /v1/heartbeat``` - returns a "text/plain" HTTP body of "OK" if the service is up
  and running. A heartbeat service like this is commonly used in load balancing
  environments so that a load balancer can validate that an application is healthy.

```POST /v1/product``` - puts a "product" in the catalog.  The product is a simple JSON
  formatted object:
  
  ```json
{"description": "The Product Description"}
```

This creates a product in the catalog.  The response will look like:

```json
{
  "productId": 96361,
  "description": "The Product Description",
  "createDate": "2017-04-10T02:51:12.772Z"
}
```

Note that the productId is simply a random integer between 10000 and 1000000.  The
createDate is when the product was added to the catalog.  Note that you can also
send your own product id in the POST:

```json
{
  "productId": 123456,
  "description": "The Product Description"
}
```

in which case the id that is given will be used.

If the product id is already in use a 409 (Conflict) HTTP error will be returned
with the body

```json
{
  "message": "product id 123456 already exists"
}
```

```GET /v1/product/{productId}``` gets an existing product with the given product id.
The response body looks the same as the call to create a new product.  A call with a
product id that doesn't exist will return an HTTP 404 error with the body:

```json
{
  "message": "productId not found - 123456"
}
```

```GET /v1/version``` gets a long description of the current version.  Note that in a production environment
this could give out a bit more (or perhaps alot more) information than you might like to.  This call takes
advantage of the `Accepts` header - if you don't specify anything then you'll get an XML response.  Pass
`application/json` to get back JSON.

```GET /v1/version/summary``` gets a short description of the current version.  This is a `text/plain` response
that gets just a quick summary of the version information.  Again, it may give out more information than
you'd like.


Logging
----
The two product services are annotated with @Logged which means that the input
and output of them will be logged to the loggers.  This is a nice way to get
the input and output of the web service calls without using any proprietary libraries.


Copyright (c) 2020
by Xigole Systems
Licensed under the MIT License - see the file LICENSE for details. 
