package info.cooper.TwitchPatreonRewards.controllers;

import info.cooper.TwitchPatreonRewards.patreon.PatreonKey;
import info.cooper.TwitchPatreonRewards.patreon.PatreonRequests;
import info.cooper.TwitchPatreonRewards.repo.PatreonKeysRepo;
import info.cooper.TwitchPatreonRewards.security.EncryptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(value="/profile")
public class ProfileController {

    @Autowired
    private PatreonKeysRepo patreonKeysRepo;

    @Autowired
    private EncryptService encryptService;

    @RequestMapping(method= RequestMethod.GET)
    public String getProfile(HttpServletResponse response,
                             @CookieValue(value = "patreon_token", defaultValue = "unknown") String patreon_token,
                             @CookieValue(value = "patreon_id", defaultValue = "0") String patreon_id,
                             Model model) {
        if (patreon_token.equals("unknown")) {
                response.setStatus(401);
                return "401";
        } else {
            String unencrypted_patreon_token = null;
            try {
                unencrypted_patreon_token = encryptService.decryptText(patreon_token);
            } catch (Exception e) {
                e.printStackTrace();
                //unencrypted_patreon_token = patreon_token;
            }
            Long id = Long.parseLong(patreon_id);
            PatreonKey databaseUser = patreonKeysRepo.findByPatreonId(id);
            PatreonKey databaseUser1 = patreonKeysRepo.findByAccessToken(unencrypted_patreon_token);
            if (databaseUser != null && databaseUser1 != null) {
                if (databaseUser.getToken().equals(databaseUser1.getToken())) {
                    PatreonRequests patreonRequests = new PatreonRequests();
                    try {
                        model.addAttribute("user", patreonRequests.getPatreonUser(unencrypted_patreon_token));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return "Profile";
                }
            }
        }
        response.setStatus(418);
        return "teapot";
    }
}
