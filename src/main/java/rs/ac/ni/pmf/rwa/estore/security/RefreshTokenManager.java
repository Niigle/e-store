package rs.ac.ni.pmf.rwa.estore.security;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class RefreshTokenManager {

    private static String generateNewToken()
    {
        final byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    @Transactional
    public String createRefreshToken(final String username)
    {
        // TODO: Implement token storage, etc.

        return generateNewToken();
    }

    public void deleteRefreshToken(final String username)
    {
        // TODO: Implement token deletion logic
    }

}
