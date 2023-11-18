package com.nextu.mesdevis.repository;

import com.nextu.mesdevis.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

}
