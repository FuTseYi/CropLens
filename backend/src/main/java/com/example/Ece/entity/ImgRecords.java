package com.example.Ece.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("imgrecords")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImgRecords {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String weight;
    private String inputImg;
    private String outImg;
    private String confidence;
    private String allTime;
    private String conf;
    private String label;
    private String username;
    private String kind;
    private String startTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getInputImg() {
        return inputImg;
    }

    public void setInputImg(String inputImg) {
        this.inputImg = inputImg;
    }

    public String getOutImg() {
        return outImg;
    }

    public void setOutImg(String outImg) {
        this.outImg = outImg;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

    public String getAllTime() {
        return allTime;
    }

    public void setAllTime(String allTime) {
        this.allTime = allTime;
    }

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }

    public String getLable() {
        return label;
    }

    public void setLable(String lable) {
        this.label = lable;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
}
