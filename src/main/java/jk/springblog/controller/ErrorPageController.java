package jk.springblog.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorPageController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @GetMapping("/error")
    public String errorPage (Model model){
        model.addAttribute("info","This address does not exist");
                return "errorpage";
    }

    @GetMapping("/errorLogin")
    public String errorLoginPage (Model model){
        model.addAttribute("info","Login invalid");
        return "errorpage";
    }
}
