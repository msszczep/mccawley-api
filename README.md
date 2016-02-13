# mccawley-api_

This is a backend for the McCawley app, which delivers
back a string of a syntactic tree of an English
language sentence.  Usage:

http://mccawley-api.com/parse/Hello%20world!

http://mccawley-api.com/parse-multi/Hello%20world!%20We%20are%20ready.

## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server

## License

Copyright © 2015 FIXME
