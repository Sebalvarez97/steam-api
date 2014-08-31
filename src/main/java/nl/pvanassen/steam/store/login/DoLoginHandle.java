package nl.pvanassen.steam.store.login;

import java.io.IOException;
import java.io.InputStream;

import nl.pvanassen.steam.http.DefaultHandle;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

class DoLoginHandle extends DefaultHandle {

    private final ObjectMapper objectMapper;

    private boolean success;

    private String message;

    private boolean emailAuthNeeded;

    private String emailDomain;

    private String emailSteamId;
    
    private boolean capchaNeeded;

    private String capchaGid;

    DoLoginHandle(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(InputStream stream) throws IOException {
        JsonNode node = objectMapper.readTree(stream);
        success = node.get("success").asBoolean();
        if (node.get("message") != null) {
        	message = node.get("message").asText();
        }
        if (node.get("capcha_needed") != null) {
            capchaNeeded = node.get("capcha_needed").asBoolean();
            capchaGid = node.get("capcha_gid").asText();
        }
        if (node.get("emailauth_needed") != null) {
            emailAuthNeeded = node.get("emailauth_needed").asBoolean();
            emailDomain = node.get("emaildomain").asText();
            emailSteamId = node.get("emailsteamid").asText();
        }
    }

    boolean isSuccess() {
        return success;
    }

    String getMessage() {
        return message;
    }

    boolean isEmailAuthNeeded() {
        return emailAuthNeeded;
    }

    String getEmailDomain() {
        return emailDomain;
    }

    String getEmailSteamId() {
        return emailSteamId;
    }

    boolean isCapchaNeeded() {
        return capchaNeeded;
    }
    
    String getCapchaGid() {
        return capchaGid;
    }
}
