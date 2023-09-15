package com.example.springframe.utils.tree;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 案例大纲视图
 * </p>
 *
 * @author liy
 * @since 2022-03-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Example extends DefaultTreeExtend {

    @TreeProperty(TreeField.SORT)
    @ApiModelProperty(value = "排序")
    public Integer sort;

    @ApiModelProperty(value = "数据")
    public String data;
}
