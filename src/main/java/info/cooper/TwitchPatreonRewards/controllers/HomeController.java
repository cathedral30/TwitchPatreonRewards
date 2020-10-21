package info.cooper.TwitchPatreonRewards.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @RequestMapping(method=RequestMethod.GET)
    public String getHome(@RequestParam(value="code", required = false) String code, @RequestParam(value="state", required = false) String state) {
        if (code != null) {
            System.out.println(code);
            System.out.println(state);
        }
        return "index";
    }
}
