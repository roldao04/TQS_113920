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


## Lab 7.2
### a)
- **How long did the API call take?**
Average (avg): The average time for the API calls to complete was 145.43 ms.

Minimum (min): The minimum time for the API calls was 4.64 ms.

Maximum (max): The maximum time for the API calls was 731.1 ms.
- **How many requests were made?**
Total Requests: The total number of requests made during the test was 2090.

The test executed at a rate of 104.43 requests per second (http_reqs: 2090 104.433851/s).
- **How many requests failed?**
Requests Failed: There were 0 failed requests, as indicated by the http_req_failed metric:

0.00% of requests failed (0 out of 2090 requests).

### b)
Summary and Actions Based on the Results:

API Call Duration:

The average response time of 398.92 ms is acceptable, but the maximum of 1.69 seconds exceeds the threshold (max<500 ms), which is a concern. You may want to investigate the cause of the slowest request, as it could indicate performance issues when the system is under load.

The 90th percentile (p(90)) and 95th percentile (p(95)) show that the majority of requests are processed reasonably quickly, with only a small number of requests taking longer.

Requests Failed:

There was only 1 failed request, which is very low, but it’s still worth investigating why that single request failed (perhaps due to timeouts or other issues like API rate limits).

Threshold Exceeded:

The http_req_duration threshold was violated because some requests exceeded 500 ms. This suggests that some requests took longer than expected and may need optimization. It could be due to increased load, server performance issues, or inefficient endpoints.

### c)
1. Thresholds:
    - http_req_duration (p(95)<1100):
    - The 95th percentile (p(95)) for HTTP request duration was 670.08 ms, which is well below the 1.1s threshold.
    - Threshold status: ✓ Passed
    - http_req_failed (rate<0.01):
    - 0.05% of the requests failed, which is slightly above the target of 1% but still within an acceptable range for this test.
    - Threshold status: ✓ Passed

2. Total Results:
    - Total Checks: There were 6862 checks performed in total.
    - 6862 checks total — 99.94% succeeded, and 0.05% failed (4 out of 6862).
    - Failed Checks:
    - is status 200: 2 requests failed (HTTP status code not 200).
    - contains pizza name: 2 requests failed (response did not contain the pizza name).
    - Status: These failures represent 0.05% of the total checks, which is a small number but still indicates areas for improvement.

3. HTTP Request Duration:
    - Average (avg): The average duration for HTTP requests was 354.88 ms.
    - Minimum (min): The minimum duration was 23.07 ms.
    - Median (med): The median request time was 339.87 ms.
    - Maximum (max): The maximum request time was 1.49 seconds (1490 ms).
    - 90th Percentile (p(90)): 90% of requests completed in 589.61 ms.
    - 95th Percentile (p(95)): 95% of requests completed in 670.08 ms.

4. Requests Made:
    - Total Requests: 3431 HTTP requests were made.
    - Request Rate: 38.05 requests per second (38.049234/s).

5. Requests Failed:
    - Requests Failed:
    - 2 requests failed out of 3431.
    - This equates to a failure rate of 0.05% of the requests (http_req_failed: 0.05%).
    - Since your threshold for failures was set to be less than 1% (threshold rate<0.01), this is still acceptable, but you may want to investigate the causes of these two failures.

6. Execution and Virtual Users (VUs):
    - Iterations: There were 3431 iterations completed.
    - Virtual Users (VUs): The test ran with 1 to 20 virtual users during the test.
    - Max VUs: The test had up to 20 virtual users active at the peak.

7. Network Traffic:
    - Data Received: 2.5 MB (28 kB/s).
    - Data Sent: 1.3 MB (14 kB/s).

8. Execution Time:
    - The test ran for 1 minute and 30 seconds (1m30s).
    - Completed iterations: 3431 iterations with no interrupted iterations.