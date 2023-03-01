package com.modong.backend.domain.application;

import com.modong.backend.Enum.StatusCode;
import com.modong.backend.domain.application.Dto.ApplicationCreateRequest;
import com.modong.backend.domain.application.Dto.ApplicationUpdateRequest;
import com.modong.backend.domain.applicationEssential.ApplicationEssential;
import com.modong.backend.base.BaseTimeEntity;
import com.modong.backend.domain.club.Club;
import com.modong.backend.domain.form.Form;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String urlId;

  @ManyToOne(fetch = FetchType.LAZY)
  private Club club;

  @Enumerated(EnumType.STRING)
  private StatusCode statusCode = StatusCode.BEFORE_OPENING;

  @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ApplicationEssential> essentials = new ArrayList<>();

  @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Form> forms = new ArrayList<>();



  public Application(ApplicationCreateRequest ApplicationCreateRequest, Club club) {
    this.title = ApplicationCreateRequest.getTitle();
    this.club = club;
    this.urlId = ApplicationCreateRequest.getUrlId();
  }

  public void addEssential(ApplicationEssential applicationEssential){
    this.essentials.add(applicationEssential);
  }

  public void addForm(Form form){
    this.forms.add(form);
  }

  public void update(ApplicationUpdateRequest applicationUpdateRequest) {
    this.title = applicationUpdateRequest.getTitle();
  }

  public void initEssentials() {
    this.essentials.remove(this.essentials);
  }

  public void open() {
    statusCode = StatusCode.OPEN;
  }

  public void close() {
    statusCode = StatusCode.CLOSE;
  }

  public boolean eqStatus(StatusCode statusCode){
    return this.statusCode == statusCode;
  }

  public boolean checkApplicationClosed(){
    return this.statusCode.equals(StatusCode.CLOSE);
  }

  public void delete() {
    this.isDeleted = true;
  }
}