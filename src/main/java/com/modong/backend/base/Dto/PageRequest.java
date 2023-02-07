package com.modong.backend.base.Dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import java.util.List;
import org.springframework.data.domain.Sort.Order;

@Data
@Schema(name = "페이지 조회 요청")
public class PageRequest {

  @Schema(description = "페이지 번호", example = "1")
  private int page = 1;
  @Schema(description = "한 페이지에서 조회할 객체 수", example = "6")
  @Range(min = 1, max = 50)
  private int size = 6;
  @Schema(description = "정렬할때 사용할 수 있는 것들 (name,asc,id,desc,rate) 정렬방향 적지 않으면 asc", example = "name,desc")
  private String sort = "name";


  public void setPage(int page) {
    this.page = page <= 0 ? 1 : page;
  }

  public void setSize(int size) {
    int DEFAULT_SIZE = 10;
    int MAX_SIZE = 50;
    this.size = size > MAX_SIZE ? DEFAULT_SIZE : size;
  }
  public org.springframework.data.domain.PageRequest of() {

    return org.springframework.data.domain.PageRequest.of(page - 1, size, makeSort(sort));
  }
  public Sort makeSort(String sort){
    String[] sorts = sort.split(",");
    List<Order> orders = new ArrayList<>();
    int lastIndex = 0;
    String lastProperty = "";
    for(int i=0; i<sorts.length;i++){
      if(sorts[i].equals("asc")){
        orders.set(lastIndex,new Order(Direction.ASC,lastProperty));
      } else if (sorts[i].equals("desc")) {
        orders.set(lastIndex,new Order(Direction.DESC,lastProperty));
      }else{
        Order order = new Order(Direction.ASC,sorts[i]);
        orders.add(order);
        lastProperty = sorts[i];
      }
      lastIndex = orders.size()-1;
    }
   return Sort.by(orders);
  }
}