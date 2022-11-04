package com.example.springframe.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Slf4j
@Component
public class InitServer implements CommandLineRunner {

	@Autowired
	private Environment env;

	@Override
	public void run(String... args) throws Exception {

        String ip = InetAddress.getLocalHost().getHostAddress();
        String httpPort = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");

        log.info("\n--------------------------------------------------------- \n" +
                "\t swagger is running! Access address: \n" +
                "\t http port at : \t {} \n" +
                "\t swagger Local: \t http://localhost:{}{}/doc.html \n" +
                "\t swagger web External: \t http://{}:{}{}/doc.html \n" +
                "--------------------------------------------------------- \n",
                httpPort, 
                httpPort, path,
                ip, httpPort, path);
	}

}
