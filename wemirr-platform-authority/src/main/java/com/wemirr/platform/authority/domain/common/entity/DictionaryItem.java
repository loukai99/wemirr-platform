package com.wemirr.platform.authority.domain.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wemirr.framework.commons.entity.SuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * 实体类
 * 字典项
 * </p>
 *
 * @author Levin
 * @since 2020-01-03
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("common_dictionary_item")
@Schema(name = "DictionaryItem", description = "字典项")
public class DictionaryItem extends SuperEntity<Long> {

    

    /**
     * 类型ID
     */
    @Schema(description = "类型ID")
    @TableField("dictionary_id")
    private Long dictionaryId;

    /**
     * 编码
     */
    @Schema(description = "编码")
    private String dictionaryCode;

    /**
     * 编码
     */
    @Schema(description = "编码")
    @TableField(value = "`value`")
    private String value;

    /**
     * 名称
     */
    @Schema(description = "名称")
    @TableField(value = "label")
    private String label;

    /**
     * 状态
     */
    @Schema(description = "状态")
    @TableField("`status`")
    private Boolean status;

    /**
     * 描述
     */
    @Schema(description = "描述")
    @TableField(value = "description")
    private String description;

    /**
     * 排序
     */
    @Schema(description = "排序")
    @TableField("`sequence`")
    private Integer sequence;


}
