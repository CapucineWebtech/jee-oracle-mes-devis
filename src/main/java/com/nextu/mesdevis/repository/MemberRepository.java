package com.nextu.mesdevis.repository;

import com.nextu.mesdevis.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'entité Member.
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * Recherche un membre par son rôle.
     *
     * @param role Le rôle du membre à rechercher.
     * @return Le membre correspondant au rôle, s'il existe.
     */
    Member findByRole(String role);
}
