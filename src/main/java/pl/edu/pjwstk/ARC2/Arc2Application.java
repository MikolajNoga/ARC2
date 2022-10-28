package pl.edu.pjwstk.ARC2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Arc2Application {
	public static void main(String[] args) {
		SpringApplication.run(Arc2Application.class, args);
	}

	@GetMapping("/")
	public String helloPage(){
		return "Hello";
	}
}
