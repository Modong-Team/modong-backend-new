package com.modong.backend.Enum;

import lombok.Getter;

public enum Regex {

  ID("^[a-zA-Z]{1}[a-zA-Z0-9_]{3,20}$"),
  PHONE("[0-9]{10,11}"),
  EMAIL("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$"),
  PASSWORD("^.*(?=^.{6,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$"),
  ;

  private String regex;

  Regex(String regex) {
    this.regex = regex;
  }

   public String getRegex(){
    return this.regex;
  }

}
