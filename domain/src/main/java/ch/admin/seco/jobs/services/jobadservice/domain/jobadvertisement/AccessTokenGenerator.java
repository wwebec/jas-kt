package ch.admin.seco.jobs.services.jobadservice.domain.jobadvertisement;

import ch.admin.seco.jobs.services.jobadservice.core.time.TimeMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class AccessTokenGenerator {

    private static Logger LOG = LoggerFactory.getLogger(AccessTokenGenerator.class);

    public String generateToken() {
        String stamp = TimeMachine.now().toString();
        return toBase64(md5Of(stamp));
    }

    private byte[] md5Of(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(value.getBytes());
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
