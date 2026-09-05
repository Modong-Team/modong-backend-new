package com.modong.backend.domain.form;

import com.modong.backend.domain.application.Application;
import com.modong.backend.base.BaseTimeEntity;
import com.modong.backend.domain.form.dto.FormRequest;
import com.modong.backend.domain.question.Question;
import java.util.ArrayList;
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
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Form extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  @ManyToOne(fetch = FetchType.LAZY)
  private Application application;

  @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Question> questions = new ArrayList<>();

  public Form(FormRequest formRequest, Application application) {
    this.title = formRequest.getTitle();
    this.application = application;
  }

  public void updateForm(FormRequest formRequest){
    this.title = formRequest.getTitle();
  }

  public void addQuestion(Question question) {
    this.questions.add(question);
  }

}