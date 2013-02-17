#!/bin/bash

# Run Server.java in tests project then run this script.
# You must refresh this folder in Eclipse!
# (don't forget to stop the Server.java afterwards)

wget -O arrangerService1.wsdl http://127.0.0.1:8081/arranger1/endpoint?wsdl
wget -O arrangerService2.wsdl http://127.0.0.1:8081/arranger2/endpoint?wsdl
