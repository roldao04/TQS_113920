package tqs;

public interface ISimpleHttpClient {
    /**
     * Performs an HTTP GET request to the given URL.
     * @param url The URL to send the GET request to.
     * @return The response as a String.
     */
    String doHttpGet(String url);
}
