package hu.clinvio.ui.security.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TokenBlacklistService {

    private static final Logger log = LoggerFactory.getLogger(TokenBlacklistService.class);

    private final Map<String, Long> blacklist = new ConcurrentHashMap<>();

    public void blacklist(String token, long expirationTime) {
        blacklist.put(token, expirationTime);
        log.debug("Token blacklisted");
    }

    public boolean isBlacklisted(String token) {
        Long expiry = blacklist.get(token);
        if (expiry == null) return false;
        if (System.currentTimeMillis() > expiry) {
            blacklist.remove(token);
            return false;
        }
        return true;
    }

    @Scheduled(fixedRate = 60000)
    public void purgeExpired() {
        int before = blacklist.size();
        blacklist.values().removeIf(expiry -> System.currentTimeMillis() > expiry);
        int purged = before - blacklist.size();
        if (purged > 0) {
            log.debug("Purged {} expired tokens from blacklist", purged);
        }
    }
}
