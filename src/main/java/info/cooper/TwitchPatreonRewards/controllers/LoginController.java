package info.cooper.TwitchPatreonRewards.controllers;

import info.cooper.TwitchPatreonRewards.patreon.PatreonAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value="/login")
public class LoginController {

    @Value("${patreon.client.id}")
    private String clientId;

    @Value("${patreon.secret}")
    private String secret;

    private PatreonAuth patreonAuth;

    @RequestMapping(method= RequestMethod.GET)
    public String getLogin(Model model) {
        patreonAuth = new PatreonAuth(clientId, secret, new String[]{}, "");
        model.addAttribute("patreonAuth", patreonAuth);
        return "Login";
    }
}
