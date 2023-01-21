package com.modong.backend.auth.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {

  Optional<Member> findByMemberId(String memberId);

  Boolean existsById(String memberId);

  Boolean existsByPhone(String phone);

  Boolean existsByEmail(String email);
}
