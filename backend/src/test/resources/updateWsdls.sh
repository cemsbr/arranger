#!/bin/bash

# Run Server.java in tests project then run this script.
# (don't forget to stop the Server.java afterwards)

wget -O performer1.wsdl http://127.0.0.1:8081/performer1/endpoint?wsdl
wget -O performer2.wsdl http://127.0.0.1:8081/performer2/endpoint?wsdl

echo "Don't forget to refresh Eclipse"
