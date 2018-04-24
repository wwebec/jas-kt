package ch.admin.seco.jobs.services.jobadservice.infrastructure.web.util;


import org.springframework.http.HttpHeaders;

/**
 * Utility class for HTTP headers creation.
 */
public final class HeaderUtil {


    private static final String APPLICATION_NAME = "job-ad-service";

    private HeaderUtil() {
    }

    public static HttpHeaders createAlert(String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(String.format("X-%s-alert", APPLICATION_NAME), message);
        headers.add(String.format("X-%s-params", APPLICATION_NAME), param);
        return headers;
    }
}
