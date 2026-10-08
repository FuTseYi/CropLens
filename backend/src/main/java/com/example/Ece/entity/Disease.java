package com.example.Ece.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("disease")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Disease {
    @Getter
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;        // 中文名称
    private String cropType;    // 作物类型
    private String symptoms;    // 病害症状
    private String causes;      // 发病原因
    private String prevention;  // 防治方法
    private String images;

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCropType() {
        return cropType;
    }

    public void setCropType(String cropType) {
        this.cropType = cropType;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getCauses() {
        return causes;
    }

    public void setCauses(String causes) {
        this.causes = causes;
    }

    public String getPrevention() {
        return prevention;
    }

    public void setPrevention(String prevention) {
        this.prevention = prevention;
    }

    public String getImage() {
        return images;
    }

    public void setImage(String image) {
        this.images = images;
    }
}