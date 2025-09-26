package com.leandrosnazareth.kafka_require;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class KafkaRequireApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaRequireApplication.class, args);

		// Thread que chama 100x o método enviarSmsComEspera()
		Thread smsThread = new Thread(() -> {
			RequireSms requireSms = new RequireSms();
			ExecutorService executor = Executors.newFixedThreadPool(10); // 10 threads paralelas
			for (int i = 0; i < 100; i++) {
				final int count = i + 1;
				executor.submit(() -> {
					try {
						System.out.println("Chamada " + count + " de enviarSmsComEspera()");
						requireSms.enviarSmsComEspera();
					} catch (Exception ex) {
						System.err.println("Erro na chamada enviarSmsComEspera(): " + ex.getMessage());
						ex.printStackTrace();
					}
				});
				try {
					Thread.sleep(100); // opcional: intervalo entre disparos
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			executor.shutdown();
			System.out.println("Loop finalizado!");
		}, "sms-sender-thread");

		smsThread.start();
		try {
			smsThread.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
