package com.astro.AstroRitaChaturvedi.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SPAController {

    @RequestMapping(value = {"/{path:^(?!api|actuator).*$}", "/{path:^(?!api|actuator).*}/**"})
    public String forward() {
        return "forward:/index.html";
    }
}