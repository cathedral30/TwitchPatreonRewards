package info.cooper.TwitchPatreonRewards.controllers;

import info.cooper.TwitchPatreonRewards.patreon.PatreonKey;
import info.cooper.TwitchPatreonRewards.patreon.PatreonRequests;
import info.cooper.TwitchPatreonRewards.patreon.PatreonUser;
import info.cooper.TwitchPatreonRewards.repo.PatreonKeysRepo;
import info.cooper.TwitchPatreonRewards.security.EncryptService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class HomeController {

    @Value("${patreon.client.id}")
    private String clientId;

    @Value("${patreon.client.secret}")
    private String secret;

    @Value("${patreon.redirect}")
    private String uri;

    @Autowired
    private PatreonKeysRepo patreonKeysRepo;

    @Autowired
    private EncryptService encryptService;

    @RequestMapping(method=RequestMethod.GET)
    public RedirectView getHome(HttpServletResponse response,
                                @CookieValue(value = "patreon_token", defaultValue = "unknown") String patreon_token,
                                @CookieValue(value = "patreon_id", defaultValue = "0") String patreon_id,
                                @RequestParam(value="code", required = false) String code,
                                @RequestParam(value="state", required = false) String state
    ) {
        if (patreon_token.equals("unknown")) {
            if (code != null) {
                PatreonRequests patreonRequests = new PatreonRequests();
                try {
                    JSONObject json = patreonRequests.validateOAuthCode(code, clientId, secret, uri);
                    String token = json.getString("access_token");
                    String refresh_token = json.getString("refresh_token");
                    Integer expires = json.getInt("expires_in");
                    PatreonUser user = patreonRequests.getPatreonUser(token);
                    PatreonKey databaseUser = patreonKeysRepo.findByPatreonId(user.getId());
                    String encryptToken = null;
                    try {
                        encryptToken = encryptService.encryptText(token);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (databaseUser == null) {
                        patreonKeysRepo.createKey(token, expires, refresh_token, user.getId());
                    } else {
                        patreonKeysRepo.updateKey(token, expires, refresh_token, user.getId());
                    }
                    Cookie token_cookie = new Cookie("patreon_token", encryptToken);
                    Cookie id_cookie = new Cookie("patreon_id", String.valueOf(user.getId()));
                    token_cookie.setMaxAge(expires);
                    id_cookie.setMaxAge(expires);
                    token_cookie.setPath("/");
                    id_cookie.setPath("/");
                    response.addCookie(id_cookie);
                    response.addCookie(token_cookie);
                    return new RedirectView("/profile");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            String unencrypted_patreon_token = null;
            try {
                System.out.println(patreon_token);
                unencrypted_patreon_token = encryptService.decryptText(patreon_token);
            } catch (Exception e) {
                e.printStackTrace();
                //unencrypted_patreon_token = patreon_token;
            }
            Long id = Long.getLong(patreon_id);
            PatreonKey databaseUser = patreonKeysRepo.findById(id);
            PatreonKey databaseUser1 = patreonKeysRepo.findByAccessToken(unencrypted_patreon_token);
            if (databaseUser != null && databaseUser1 != null) {
                if (databaseUser.getToken().equals(databaseUser1.getToken())) {
                    return new RedirectView("/profile");
                }
            }
            Cookie token_cookie = new Cookie("patreon_token", null);
            Cookie id_cookie = new Cookie("patreon_id", null);
            token_cookie.setMaxAge(0);
            id_cookie.setMaxAge(0);
            token_cookie.setPath("/");
            id_cookie.setPath("/");
            response.addCookie(id_cookie);
            response.addCookie(token_cookie);
        }
        return new RedirectView("/login");
    }
}
