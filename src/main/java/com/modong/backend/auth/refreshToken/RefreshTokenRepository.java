package com.modong.backend.auth.refreshToken;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

  Optional<RefreshToken> findByMemberId(Long memberId);
}