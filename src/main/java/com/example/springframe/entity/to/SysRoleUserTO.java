package com.example.springframe.entity.to;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.example.springframe.entity.SearchPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 用户角色关系(SysRoleUser)实体TO类
 *
 * @author makejava
 * @since 2023-03-27 10:21:47
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "SysRoleUserTO对象", description = "用户角色关系TO")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysRoleUserTO extends SearchPage implements Serializable {
    private static final long serialVersionUID = -69345302653415978L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    @NotNull(message = "用户id不能为空")
    private Integer userId;
    /**
     * 角色id
     */
    @ApiModelProperty(value = "角色id", required = true)
    @NotNull(message = "角色id不能为空")
    private Integer roleId;
}
