package com.leandrosnazareth.kafka_msg_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.leandrosnazareth.kafka_msg_api.model.Sms;
import com.leandrosnazareth.kafka_msg_api.model.StatusSms;
import com.leandrosnazareth.kafka_msg_api.repository.SmsRepository;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SmsService {

    private final SmsRepository smsRepository;
    @Autowired
    private EntityManager entityManager;

    public Sms processarSms(Sms sms) {
        return smsRepository.save(sms);
    }

    public String enviarSmsComEsperaDeResposta(Sms mensagem) {
        Sms smsEnviado = smsRepository.save(mensagem);

        // verificar se o status do sms está como recebido repetir 60x com espera de 5s
        for (int i = 0; i < 60; i++) {
            System.out.println("Verificando se o SMS foi respondido... Tentativa " + (i + 1));
            Sms recebido = this.findSmsById(smsEnviado.getId());
            System.out.println("Resposta atual: " + recebido.getResposta() + " Para ID: " + smsEnviado.toString());
            if (recebido.getResposta() != null) {
                recebido.setStatusSms(StatusSms.RESPONDIDO);
                smsRepository.save(recebido);
                return recebido.getResposta();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                // interrompido -> tratar como não respondido
                break;
            }
        }

        // não recebeu resposta dentro do tempo/ tentativas
        return "-1";
    }

    public Sms enviarSmsSemEsperaDeResposta(Sms mensagem) {
        mensagem.setStatusSms(StatusSms.ENVIADO);
        return smsRepository.save(mensagem);
    }

    public Sms findSmsById(Long idMensagem) {
        entityManager.clear(); // força busca do banco, não do cache
        return smsRepository.findById(idMensagem).get();
    }

    public int informarRecebimentoSMS() {
        int alterados = smsRepository.atualizarRespostaSMS();
        entityManager.clear(); // limpa o cache do JPA
        return alterados;
    }
}
