package org.apereo.cas.adaptors.rest;

import org.apereo.cas.authentication.UsernamePasswordCredential;
import org.apereo.cas.authentication.principal.SimplePrincipal;
import org.apereo.cas.util.EncodingUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * This is {@link RestAuthenticationApi}.
 *
 * @author Misagh Moayyed
 * @since 5.0.0
 */
public class RestAuthenticationApi {

    private RestTemplate restTemplate;

    private String authenticationUri;

    public void setRestTemplate(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public String getAuthenticationUri() {
        return authenticationUri;
    }

    public void setAuthenticationUri(final String authenticationUri) {
        this.authenticationUri = authenticationUri;
    }

    /**
     * Authenticate and receive eneity from the rest template.
     *
     * @param c the credential
     * @return the response entity
     */
    public ResponseEntity<SimplePrincipal> authenticate(final UsernamePasswordCredential c) {
        final HttpEntity<SimplePrincipal> entity = new HttpEntity<>(createHeaders(c));
        return restTemplate.exchange(authenticationUri, HttpMethod.POST, entity, SimplePrincipal.class);
    }

    /**
     * Create authorization http headers.
     *
     * @param c the credentials
     * @return the http headers
     */
    public static HttpHeaders createHeaders(final UsernamePasswordCredential c) {
        final HttpHeaders acceptHeaders = new HttpHeaders() {
            private static final long serialVersionUID = -3529759978950667758L;

            {
                set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON.toString());
            }
        };
        final String authorization = c.getUsername() + ':' + c.getPassword();
        final String basic = EncodingUtils.encodeBase64(authorization.getBytes(Charset.forName("US-ASCII")));
        acceptHeaders.set("Authorization", "Basic " + basic);
        return acceptHeaders;
    }


}
