package hk.edu.hkbu.comp;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;




@Controller
public class MyController {
	static HashMap<String, Set<UrlObject>> keywordUrlMap = new HashMap<String, Set<UrlObject>>();
	@GetMapping("load")
	@ResponseBody
	String load(HttpServletRequest request) throws InterruptedException {
	    String str = ((request.getQueryString()).replaceFirst("^query=*", "")).replace("+"," "); 
	    
	    str = str.replaceAll("%28", "(");
		str = str.replaceAll("%29", ")");	
	   
	     return "Hello <br> <h1>You are searching "+ str +"</h1>" + response(handleUserRequest(res(str)) );
	}

	
	void readData() {
		 //de-serial the hash map
		 try
	      {
	         FileInputStream fis = new FileInputStream("hashmap.ser");
	         ObjectInputStream ois = new ObjectInputStream(fis);
	         keywordUrlMap = (HashMap) ois.readObject();
	         ois.close();
	         fis.close();

	      }catch(IOException ioe)
	      {
	         ioe.printStackTrace();
	         return;
	      }catch(ClassNotFoundException c)
	      {
	         System.out.println("Class not found");
	         c.printStackTrace();
	         return;
	      }
	}
	
	List<UrlObject> combineANDList(List<UrlObject> temp1, List<UrlObject> temp2 ){
		
		for(int j=0; j<temp2.size();j++) {
			
			if(temp1.contains(temp2.get(j))) {
				// duplicate url, combine them by adding the number Appear
				temp1.get(temp1.indexOf(temp2.get(j))).numberAppear+=temp2.get(j).numberAppear;
			}
		}
		for (int j=0; j<temp1.size();j++) {
			if(!temp2.contains(temp1.get(j))) {
				
				// duplicate url, combine them by adding the number Appear
				temp1.remove(temp1.get(j));
				j--;
			}
		}
		return temp1;
	}
	List<UrlObject> combineORList(List<UrlObject> temp1, List<UrlObject> temp2 ){
		for(int j=0; j<temp2.size();j++) {
			if(temp1.contains(temp2.get(j))) {
				// duplicate url, combine them by adding the number Appear
				temp1.get(temp1.indexOf(temp2.get(j))).numberAppear+=temp2.get(j).numberAppear;
			}else {
				temp1.add(temp2.get(j));
			}
		}
		return temp1;
	}
	
	List<UrlObject> handleUserRequest(String[] input) {
		readData();
		Set<UrlObject> temp = keywordUrlMap.get(input[0]);
		List<UrlObject> result = new ArrayList<UrlObject>();
		if(temp!=null) {
			result = new ArrayList<UrlObject>(temp);
			
		}
		int i =1;
		while(input.length>i) {
			if(input[i].equals("AND")|| (!input[i].equals("OR") && !input[i].equals("NOT"))){
				if(input[i].equals("AND")) {
					if(input[i+1].contains("(")) {
						List<UrlObject> result2 = handleUserRequest(release(input[i+1]));
						result = combineANDList(result,result2);
						i = i+2;
					}else {
						Set<UrlObject> temp2 = keywordUrlMap.get(input[i+1]);
						List<UrlObject> result2 = new ArrayList<UrlObject>(temp2);
						//combine the arraylist
							result = combineANDList(result,result2);
							i = i+2;
					}
				}else { //if other words
					if(input.length>i) {
					if(input[i].contains("(")) {
						List<UrlObject> result2 = handleUserRequest(release(input[i]));
						result = combineANDList(result,result2);
						i = i+1;
					}else {
						Set<UrlObject> temp2 = keywordUrlMap.get(input[i]);
						List<UrlObject> result2 = new ArrayList<UrlObject>(temp2);
						//combine the arraylist
							result = combineANDList(result,result2);
							i = i+1;
						
					}
					}
				}
			}else if(input[i].equals("OR")){
				if( input[i+1].contains("(")) {
					List<UrlObject> result2 = handleUserRequest(release(input[i+1]));
					result = combineORList(result,result2);
					i = i+2;
				}else {
					Set<UrlObject> temp2 = keywordUrlMap.get(input[i+1]);
					List<UrlObject> result2 = new ArrayList<UrlObject>(temp2);
					//combine the arraylist
						result = combineORList(result,result2);
						i = i+2;
				}
				
			}else if (input[i].equals("NOT")) {
				if(input[i+1].contains("(")) {
					List<UrlObject> result2 = handleUserRequest(release(input[i+1]));
					for(int j=0; j<result2.size();j++) {
						if(result.contains(result2.get(j))) {
							// remove
							result.remove(result.indexOf(result2.get(j)));
						}
					}
					i = i+2;
				}else {
					Set<UrlObject> temp2 = keywordUrlMap.get(input[i+1]);
					List<UrlObject> result2 = new ArrayList<UrlObject>(temp2);
					//combine the arraylist
					for(int j=0; j<result2.size();j++) {
						if(result.contains(result2.get(j))) {
							// remove
							result.remove(result.indexOf(result2.get(j)));
						}
					}
						i = i+2;
				}
				
			}
		}

		return result;
	}
	String response(List<UrlObject> input) {
		Collections.sort(input, new Comparator<UrlObject>() {
	       @Override
			public int compare(UrlObject o1, UrlObject o2) {
				// TODO Auto-generated method stub
	    	   return Double.compare(o2.numberAppear, o1.numberAppear);
			}
	    });
		
		String finalResult ="";
		if(input.size()>1) {
		for (UrlObject t : input) {
			finalResult += t.toString()+"<br>";
		}
		}else {
			finalResult=" No matched URLs";
		}
		return finalResult;
	}
	String[] res(String str) {
		String fomatStr = str;
		
		//print out what we have input;
		fomatStr = fomatStr.replaceAll("%28", "(");
		fomatStr = fomatStr.replaceAll("%29", ")");		
		System.out.println(fomatStr);	
		
		ArrayList<String> groupitem = new ArrayList<String>();
		
		Matcher m = Pattern.compile("(\\([^)]+\\)|\\w+)").matcher(fomatStr);		   		 	
        while(m.find()) {	    	 	    		    	   	 
	    	 // System.out.println("Found bracket from index "+ m.start() +" to "+ (m.end()-1));
	    	 String temp= String.format(m.group(0));	    	 
	    	 groupitem.add(temp);
	    	    	 	    	
	}       	
        String.format(fomatStr);   
        System.out.println("groupitem: ");
        System.out.println(groupitem);
        //change it to array
        String[] input = new String[groupitem.size()];
        for (int i = 0; i<groupitem.size();i++) {
        	input[i]=groupitem.get(i);
        }
		return input; 
	}
	
	String[] release(String str) {
		//release the backets
		str= str.substring(1,str.length()-1);
		String[] input = str.split(" ");
		return input;
	}
	
    @GetMapping("greeting")
    @ResponseBody
    String sayHello(
    	@RequestParam(name = "name", required = false, defaultValue = "there") 
    		String name) {

    	return "<h1>Hello " + name + "!</h1>";
    }
    
    
    

}
