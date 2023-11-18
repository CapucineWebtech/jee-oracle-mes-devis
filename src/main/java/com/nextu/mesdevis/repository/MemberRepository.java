package com.nextu.mesdevis.repository;

import com.nextu.mesdevis.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByRole(String role);
}
