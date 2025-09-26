package com.leandrosnazareth.kafka_require;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaRequireApplication {

	public static void main(String[] args) {
		SpringApplication.run(KafkaRequireApplication.class, args);

		// Thread que chama 100x o método enviarSmsComEspera()
		Thread smsThread = new Thread(() -> {
			RequireSms requireSms = new RequireSms();
			for (int i = 0; i < 100; i++) {
				try {
					requireSms.enviarSmsComEspera();
					// pequeno intervalo entre chamadas (ajustável)
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				} catch (Exception ex) {
					// log mínimo de erro
					System.err.println("Erro na chamada enviarSmsComEspera(): " + ex.getMessage());
					ex.printStackTrace();
				}
			}
		}, "sms-sender-thread");

		smsThread.setDaemon(true);
		smsThread.start();
	}

}
