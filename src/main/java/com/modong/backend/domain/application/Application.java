package com.modong.backend.domain.application;

import com.modong.backend.domain.application.Dto.ApplicationRequest;
import com.modong.backend.domain.applicationEssential.ApplicationEssential;
import com.modong.backend.base.BaseTimeEntity;
import com.modong.backend.domain.club.Club;
import com.modong.backend.domain.form.Form;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
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

  private boolean isClosed;

  @ManyToOne(fetch = FetchType.LAZY)
  private Club club;

  @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ApplicationEssential> essentials = new ArrayList<>();

  @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Form> forms = new ArrayList<>();



  public Application(ApplicationRequest applicationRequest, Club club) {
    this.title = applicationRequest.getTitle();
    this.club = club;
    this.urlId = applicationRequest.getUrlId();
  }

  public void addEssential(ApplicationEssential applicationEssential){
    this.essentials.add(applicationEssential);
  }

  public void addForm(Form form){
    this.forms.add(form);
  }

  public void update(ApplicationRequest applicationRequest) {
    this.title = applicationRequest.getTitle();
  }

  public void initEssentials() {
    this.essentials.remove(this.essentials);
  }
}