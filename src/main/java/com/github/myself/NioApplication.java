package com.github.myself;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

public class NioApplication {

	public static void main(String[] args) throws IOException {
		NioServer nioServer = new NioServer();
		nioServer.server(8090);
	}

}

