package hk.edu.hkbu.comp;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyController {
    @GetMapping("**")
    @ResponseBody
    String load(HttpServletRequest request) {
        return String.format("You are browsing %s",
            request.getRequestURI(), request.getQueryString());
    }
}