# catfindr

Have ascii art with cat faces hidden inside? Well this is the repo for you! Currently it provides a simple REST endpoint for uploading ascii art and having the (known) cat faces starting coordinates returned, along with a confidence score.

## What's it do?

Provides a very straightforward REST API endpoint that accepts an ascii art `.txt` file with embedded cat faces. Based on the provided `min_confidence` query param, a result set is returned identifying the starting coordinates of each detected face.

## API Endpoint description

`POST /find`

Query parameter: `min_confidence`, required, float. Valid values range from 0.00 - 1.00, with the high end of the range forcing 100% confidence.

Form parameter: `frame`, plain/text, containing the ascii art frame to extract cat faces from. 

**Example query for demo URL**
```
❯ curl -X POST -F "frame=@src/test/resources/cat_input_frame.txt" "https://catfindr.kono.sh/find?min_confidence=0.75"

[{"row":0,"col":80,"confidence":0.8577778},{"row":1,"col":45,"confidence":0.84},{"row":40,"col":42,"confidence":0.83111113},{"row":49,"col":84,"confidence":0.83555555},{"row":80,"col":47,"confidence":0.84444445},{"row":84,"col":84,"confidence":0.8488889}]
```


## Getting things running

With pre-reqs covered, you can get this running with just a few commands.

### Pre-reqs

- [JDK 8 or higher](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0ahUKEwiHt8ejmt7YAhVIEawKHa8ZAM8QFggpMAA&url=http%3A%2F%2Fwww.oracle.com%2Ftechnetwork%2Fjava%2Fjavase%2Fdownloads%2Fjdk8-downloads-2133151.html&usg=AOvVaw27mFVHV9M4wo4ENQuM77C5)
- [Gradle](https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0ahUKEwiunOa4m97YAhVHSq0KHWZ3BlkQFggpMAA&url=https%3A%2F%2Fgradle.org%2Finstall%2F&usg=AOvVaw2fWc5F4rapV0XQ0y6AVSq3)
- Comfort on the command line (if you want to follow these instructions step-by-step)

### Building

Being a gradle based project, building with a working environment is as simple as dropping into the root of the project and running:

```
gradle build
```

### Running tests

Brace yourself, this is a hard one ... `gradle test`. Assuming your environment is configured correctly, you can expect output along the lines of:

```
❯ gradle test
Starting a Gradle Daemon (subsequent builds will be faster)

> Task :test
... generic log statements truncated ....

BUILD SUCCESSFUL in 9s
5 actionable tasks: 2 executed, 3 up-to-date
```

### Running the server

Tests are nice, but interacting with it for real is much more fun. Launching is about as difficult as running the tests, `gradle bootRun`. You're up and running when the output looks like this:

```
sh.kono.catfindr.Application             : Started Application in 2.022 seconds (JVM running for 2.253)
<==========---> 80% EXECUTING [1m 13s]
> :bootRun
```

To verify everything is working as expected, run this curl from the root directory:

```
curl -X POST -F "frame=@src/test/resources/cat_input_frame.txt" "localhost:3000/find?min_confidence=0.75"
```

That will take one of the embedded test txt files and pass it to the local endpoint. The returned results will be:

```
[{"row":0,"col":80,"confidence":0.8577778},{"row":1,"col":45,"confidence":0.84},{"row":40,"col":42,"confidence":0.83111113},{"row":49,"col":84,"confidence":0.83555555},{"row":80,"col":47,"confidence":0.84444445},{"row":84,"col":84,"confidence":0.8488889}]
```

For the more visual of this README's viewers, here's a gif demonstrating the whole process:

![](https://dsh.re/95c56)

## Features and Roadmap

- [x] Basic brute force search algo
- [x] Spring Boot based REST API endpoints
- [x] [apex/up](https://github.com/apex/up) demo deployment on lambda
- [x] Basic handling of uneven column counts within the text matrices
- [ ] Test page on index to provide an alternative to curl
- [ ] More robust input validation
- [ ] Streaming API
- [ ] Sessions with a TTL to support ad-hoc "training images" instead of embedded only