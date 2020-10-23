package info.cooper.TwitchPatreonRewards.controllers;

import info.cooper.TwitchPatreonRewards.patreon.PatreonRequests;
import org.json.JSONObject;
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
    public String getHome(
            @RequestParam(value="code", required = false) String code,
            @RequestParam(value="state", required = false) String state
    ) {
        if (code != null) {
            PatreonRequests patreonRequests = new PatreonRequests();
            try {
                System.out.println(code);
                JSONObject json = patreonRequests.validateOAuthCode(code, clientId, secret, uri);
                System.out.println(json.toString());
                String token = json.getString("access_token");
                String refresh_token = json.getString("refresh_token");
                Long expires = json.getLong("expires_in");
                System.out.println(token + " " + refresh_token + " " + expires);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "index";
    }
}
