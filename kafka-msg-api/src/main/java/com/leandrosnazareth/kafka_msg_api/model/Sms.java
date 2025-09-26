package com.leandrosnazareth.kafka_msg_api.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String mensagem;
    private String resposta;
    // data gerada automaticamente pelo sistema
    private LocalDateTime dataEnvio;
    private LocalDateTime dataRecebimento;
    private String destino;
    private String origem;
    private StatusSms statusSms;
    @Version
    private Long version;

    @PrePersist
    protected void prePersist() {
        if (this.dataEnvio == null)
            dataEnvio = LocalDateTime.now();
        if (this.dataRecebimento == null)
            dataRecebimento = LocalDateTime.now();
    }

    @PreUpdate
    protected void preUpdate() {
        this.dataRecebimento = LocalDateTime.now();
    }

}