package hk.edu.hkbu.comp;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyController {
	//static user query
//    @GetMapping("**")
//    @ResponseBody
//    String load(HttpServletRequest request) {
//    	
//    	String reply = "Serach  engine's response is hello.";
//        return String.format("Keywords of User's query %s, %s",
//            request.getRequestURI(), reply);
//        
//    }
	
//	// dynmeic 
//	@GetMapping("greeting")
//	@ResponseBody
//	String sayHello(
//		@RequestParam(name = "name", required = false, defaultValue = "there") 
//			String name) {
//
//		return "<h1>Hello " + name + "!</h1>";
//	}
	@GetMapping("load")
	@ResponseBody
	String load(HttpServletRequest request) throws InterruptedException {
	    String str = ((request.getQueryString()).replaceFirst("^query=*", "")).replace("+"," "); 
	    	
	    return String.format("You are browsing %s",
	            request.getRequestURI(), request.getQueryString());
	}

    
}
