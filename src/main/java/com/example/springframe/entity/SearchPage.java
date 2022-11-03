package com.example.springframe.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SearchPage {

    @ApiModelProperty(value = "页数")
    private int page = 0;

    @ApiModelProperty(value = "每页条数")
    private int size = 10;
}
