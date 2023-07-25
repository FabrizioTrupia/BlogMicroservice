package it.cgmconsulting.mscomment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsCommentApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsCommentApplication.class, args);
	}

}
