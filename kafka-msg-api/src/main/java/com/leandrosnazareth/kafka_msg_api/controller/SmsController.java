package com.leandrosnazareth.kafka_msg_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.leandrosnazareth.kafka_msg_api.model.Sms;
import com.leandrosnazareth.kafka_msg_api.service.SmsService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/sms")
@AllArgsConstructor
public class SmsController {

    private final SmsService smsService;

    @PostMapping("/enviar-com-espera")
    public ResponseEntity<?> enviarSmsComEsperaDeResposta(@RequestBody Sms mensagem) {
        String resultado = smsService.enviarSmsComEsperaDeResposta(mensagem);
        if (resultado != null) {
            return ResponseEntity.ok(resultado);
        }
        // accepted - mensagem enviada/aceita mas sem resposta no tempo
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/enviar-sem-espera")
    public ResponseEntity<?> enviarSmsSemEsperaDeResposta(@RequestBody Sms mensagem) {
        Sms resultado = smsService.processarSms(mensagem);
        // accepted - mensagem enviada/aceita mas sem resposta no tempo
        return ResponseEntity.ok(resultado);
    }

    // CONSULTAR SMS PELO ID
    @GetMapping("/consultar")
    public ResponseEntity<?> consultarSms(@RequestParam String id) {
        Sms resultado = smsService.findSmsById(Long.parseLong(id));
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/informar-recebimento")
    public ResponseEntity<?> informarRecebimentoSMS() {
        System.out.println("Informando recebimento SMS...");
        smsService.informarRecebimentoSMS();
        return ResponseEntity.ok(smsService.informarRecebimentoSMS());
    }

}