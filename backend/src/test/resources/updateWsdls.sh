#!/bin/bash

# Run PerformerPublisher.java in tests.functional package then run this script.
# (don't forget to stop the PerformerPublisher.java afterwards)

wget -O performer0.wsdl http://127.0.0.1:8081/performer0/endpoint?wsdl
wget -O performer1.wsdl http://127.0.0.1:8081/performer1/endpoint?wsdl

echo "Don't forget to refresh Eclipse"
