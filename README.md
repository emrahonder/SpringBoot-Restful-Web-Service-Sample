# SpringBoot Restful Web Service Sample

This restful WS has 2 endpoints:
* Post Transaction
```
http://localhost:9005/transactions
```
This allows just post requests and accepts transactions if timestamp is newer than previous 60 seconds. 
If request is eligible for saving, it returns HTTP Status Code 201, Created. OTherwise it returns HTTP Status Code 204, No Content.
* Get Statistics
```
http://localhost:9005/statistics
```
This allows just get requests and returns statictics of transactions came at last 60 seconds.
###  Service Configuration
As mentioned above, WS is served over 9005, this port is define on application.properties file under /src/main/resources/
Below ones can be updated according to needs.
```
server.port: 9005
management.server.port: 9006
management.server.address: 127.0.0.1
```
###  Assumptions
Transaction and statistics operations are done on middle of day at the same calendar day.


