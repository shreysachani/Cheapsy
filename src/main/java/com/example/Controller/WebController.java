package com.example.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WebController {
	
	
	@GetMapping(path="/")
    public String Arithinputpage(@RequestParam(required=false) String action)
    {
		
		return "welcome";
	}
	@PostMapping(path="/")
	public ModelAndView showArithmeticOutpage()
	{
		ModelAndView mv=new ModelAndView();
		
		return mv;
		
	}

}
