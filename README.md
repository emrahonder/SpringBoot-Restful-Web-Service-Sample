# SpringBoot Restful Web Service Sample

This restful WS has 2 endpoints:
```
http://localhost:9005/transactions
```
This allows just post requests and accepts transactions if timestamp is newer than previous 60 seconds. 
If request is eligible for saving, it returns HTTP Status Code 201, Created. OTherwise it returns HTTP Status Code 204, No Content.

```
http://localhost:9005/statistics
```
This allows just get requests and returns statictics of transactions came at last 60 seconds.

###  Assumptions
Transaction and statistics operations are done on middle of day at the same calendar day.
