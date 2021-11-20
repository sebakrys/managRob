package pl.skrys.controller;

import org.springframework.stereotype.Controller;
import pl.skrys.app.SpVerifyToken;
import pl.skrys.service.SpVerifyTokenService;

@Controller
public class SpVerifyTokenControl {
    SpVerifyTokenService verifyTokenService;

    public SpVerifyTokenControl(SpVerifyTokenService verifyTokenService) {
        this.verifyTokenService = verifyTokenService;
    }

    //TODO dodac aktywacje przekierowania stron
}
