package hk.edu.hkbu.comp;


import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;




@Controller
public class MyController {
	@GetMapping("load")
	@ResponseBody
	String load(HttpServletRequest request) throws InterruptedException {
	    String str = ((request.getQueryString()).replaceFirst("^query=*", "")).replace("+"," "); 
	 		    
	   // System.out.println(request.getQueryString());
	     return res(str);
	}

	
	
	String res(String str) {
		String fomatStr = str;
		
		//print out what we have input;
		fomatStr = fomatStr.replace("%28", "(");
		fomatStr = fomatStr.replace("%29", ")");		
		System.out.println(fomatStr);	
		//retreive 
		ArrayList<String> groupitem = new ArrayList<String>();
		Matcher m = Pattern.compile("\\(([^)]+)\\)").matcher(fomatStr);
		   		
        //String[] strs = pattern.split(fomatStr);  
	
        while(m.find()) {	    	 	    	
	    	String temp= String.format(m.group(0));	    	 
	    	 groupitem.add(temp);	    	 	    	 
	}
        System.out.print(groupitem);  		
        String.format(fomatStr);   				
		return fomatStr; 
		
	}
	
    @GetMapping("greeting")
    @ResponseBody
    String sayHello(
    	@RequestParam(name = "name", required = false, defaultValue = "there") 
    		String name) {

    	return "<h1>Hello " + name + "!</h1>";
    }
    
    
    

}
