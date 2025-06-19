package com.company.app.menu.dto;

/*
    Controller ↔ Service ↔ Repository
              DTO      Entity
 */

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MenuDto {

    private Integer menuCode;
    private String menuName;
    private Integer menuPrice;
    private Integer categoryCode;
    private String orderableStatus;

}
