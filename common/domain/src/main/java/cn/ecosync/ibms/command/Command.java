package cn.ecosync.ibms.command;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "commandType", visible = true)
public interface Command extends Serializable {
    default void validate() {
    }
}
