package com.leandrosnazareth.kafka_require;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RequireSms {

    // método para fazer chamada REST para o serviço de SMS curl --location
    // 'http://localhost:8080/sms/enviar-com-espera' \ --header 'Content-Type:
    // application/json' \ --data '{ "mensagem": "Texto da mensagem", "destino":
    // "5511999999999", "origem": "5511888888888"}'
    public String enviarSmsComEspera() {
        String result = null;
        String urlStr = "http://localhost:8080/sms/enviar-com-espera";
        String jsonPayload = "{\"mensagem\":\"Texto da mensagem\",\"destino\":\"5511999999999\",\"origem\":\"5511888888888\"}";

        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            byte[] out = jsonPayload.getBytes(StandardCharsets.UTF_8);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(out);
            }

            int status = conn.getResponseCode();
            InputStream is = status >= 200 && status < 400 ? conn.getInputStream() : conn.getErrorStream();

            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }

            result = "HTTP " + status + ": " + response.toString();
            System.out.println("Resposta SMS service - " + result);
        } catch (Exception e) {
            result = "Erro ao enviar SMS: " + e.getMessage();
            System.err.println(result);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return result;
    }

}
