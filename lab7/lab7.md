# Lab 7
## Lab 7.1
### d) 2)
- **How long did the API call take?**
The average duration for the HTTP request (http_req_duration) was 127.27 ms. The response time was consistent for each request:

avg: 127.27 ms

min: 127.27 ms

max: 127.27 ms

p(90): 127.27 ms (90% of the requests were completed in this time or less)

p(95): 127.27 ms (95% of the requests were completed in this time or less)

- **How many requests were made?**
A total of 1 HTTP request was made during the test, as shown by the http_reqs metric:

http_reqs: 1 request

The test executed 1 iteration for the single virtual user (VUs), and each iteration triggered 1 HTTP request to the /api/pizza endpoint.

- **How many requests failed?**
There were 0 failed requests, as indicated by the http_req_failed metric:

http_req_failed: 0.00% (0 out of 1 requests failed)

This means that all HTTP requests successfully returned a status code 200, which is the expected behavior for this test scenario.

- **Summary of results**
    - API call duration: 127.27 ms
    - Total requests made: 1
    - Requests failed: 0
    - Request rate: 7.82 requests/second
    - Data received: 641 B
    - Data sent: 365 B
    - Iteration duration: 127.84 ms