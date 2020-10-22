package info.cooper.TwitchPatreonRewards.controllers;

import info.cooper.TwitchPatreonRewards.patreon.PatreonRequests;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class HomeController {

    @Value("${patreon.client.id}")
    private String clientId;

    @Value("${patreon.client.secret}")
    private String secret;

    @Value("${patreon.redirect}")
    private String uri;

    @RequestMapping(method=RequestMethod.GET)
    public String getHome(@RequestParam(value="code", required = false) String code, @RequestParam(value="state", required = false) String state) {
        if (code != null) {
            PatreonRequests patreonRequests = new PatreonRequests();
            try {
                patreonRequests.validateOAuthCode(code, clientId, secret, uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(code);
        }
        return "index";
    }
}
