#!/bin/bash

# run the container in background corresponding to the mockmock image
# ports are mapped the same
docker run -d -p 2525:2525 -p 8282:8282 mockmockserver
