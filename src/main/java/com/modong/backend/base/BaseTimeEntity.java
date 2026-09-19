package com.modong.backend.base;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseTimeEntity {

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createDate = LocalDateTime.now();

  @LastModifiedDate
  private LocalDateTime lastModifiedDate = LocalDateTime.now();

  private Boolean isDeleted = false;

  public void delete(){
    this.isDeleted = true;
  }
}