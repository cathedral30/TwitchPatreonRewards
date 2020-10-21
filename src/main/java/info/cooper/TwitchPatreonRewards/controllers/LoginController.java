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

    @Value("${patreon.redirect}")
    private String uri;

    @RequestMapping(method= RequestMethod.GET)
    public String getLogin(Model model) {
        PatreonAuth patreonAuth = new PatreonAuth(clientId, uri, new String[]{}, "");
        model.addAttribute("patreonAuth", patreonAuth);
        return "Login";
    }
}
