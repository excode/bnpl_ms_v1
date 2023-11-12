
package com.java.bnpl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.java.bnpl")
public class BnplTransactionApplication {

	public static void main(String[] args) {
		SpringApplication.run(BnplTransactionApplication.class, args);
	}

}

