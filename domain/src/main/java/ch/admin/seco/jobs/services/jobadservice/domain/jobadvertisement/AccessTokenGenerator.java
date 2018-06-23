package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AccessTokenGenerator {

    public static final int RANDOM_NUMBER_LENGTH = 64;
    private static Logger LOG = LoggerFactory.getLogger(AccessTokenGenerator.class);

    public String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[RANDOM_NUMBER_LENGTH];
        random.nextBytes(bytes);
        return toBase64(md5Of(bytes));
    }

    private byte[] md5Of(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            LOG.error("MD5 algorithm not found", e);
        }
        return null;
    }

    private String toBase64(byte[] bytes) {
        return (bytes != null) ? Base64.getUrlEncoder().encodeToString(bytes) : null;
    }

}
