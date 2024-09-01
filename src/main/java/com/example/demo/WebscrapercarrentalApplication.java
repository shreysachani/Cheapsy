package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.Controller.WebController;



@SpringBootApplication
public class WebscrapercarrentalApplication implements CommandLineRunner
{

	public static void main(String[] args) {
		
		SpringApplication.run(WebscrapercarrentalApplication.class, args);
		
	}
	@Override
	public void run(String... args) throws Exception 
	{
		
		Entry r=new Entry();
		r.main(args);
	}
}
