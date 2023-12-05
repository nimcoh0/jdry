package com.cassiomolin.example.security.service;

import com.cassiomolin.example.common.configuration.Configurable;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;

/**
 * Settings for signing and verifying JWT tokens.
 *
 * @author cassiomolin
 */
@Dependent
class AuthenticationTokenSettings {

    /**
     * Secret for signing and verifying the token signature.
     */
    @Inject
    @Configurable("authentication.jwt.secret")
    private String secret;

    /**
     * Allowed clock skew for verifying the token signature (in seconds).
     */
    @Inject
    @Configurable("authentication.jwt.clockSkew")
    private Long clockSkew;

    /**
     * Identifies the recipients that the JWT token is intended for.
     */
    @Inject
    @Configurable("authentication.jwt.audience")
    private String audience;

    /**
     * Identifies the JWT token issuer.
     */
    @Inject
    @Configurable("authentication.jwt.issuer")
    private String issuer;

    /**
     * JWT claim for the authorities.
     */
    @Inject
    @Configurable("authentication.jwt.claimNames.authorities")
    private String authoritiesClaimName = "authorities";

    /**
     * JWT claim for the token refreshment count.
     */
    @Inject
    @Configurable("authentication.jwt.claimNames.refreshCount")
    private String refreshCountClaimName = "refreshCount";

    /**
     * JWT claim for the maximum times that a token can be refreshed.
     */
    @Inject
    @Configurable("authentication.jwt.claimNames.refreshLimit")
    private String refreshLimitClaimName = "refreshLimit";

    public String getSecret() {
        String key = "random_secret_key_aaaaa_bbbb_ccccc_dddddd";
        String base64Key = DatatypeConverter.printBase64Binary(key.getBytes());
        //SecretKey key = io.jsonwebtoken.security.Keys.secretKeyFor(SignatureAlgorithm.HS256);
        //byte[] secretBytes = DatatypeConverter.parseBase64Binary(base64Key);
        return base64Key;
    }

    public Long getClockSkew() {
        return clockSkew;
    }

    public String getAudience() {
        return audience;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getAuthoritiesClaimName() {
        return authoritiesClaimName;
    }

    public String getRefreshCountClaimName() {
        return refreshCountClaimName;
    }

    public String getRefreshLimitClaimName() {
        return refreshLimitClaimName;
    }
}
