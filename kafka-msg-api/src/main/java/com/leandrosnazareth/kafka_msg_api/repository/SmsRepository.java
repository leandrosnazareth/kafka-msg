package com.leandrosnazareth.kafka_msg_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.leandrosnazareth.kafka_msg_api.model.Sms;

@Repository
public interface SmsRepository extends JpaRepository<Sms, Long> {

    // Atualiza todos os registros com a resposta "MINHA RESPOSTA"
    @Modifying
    @Transactional
    @Query(value = "UPDATE sms SET resposta = 'MINHA RESPOSTA'", nativeQuery = true)
    int atualizarRespostaSMS();

}
