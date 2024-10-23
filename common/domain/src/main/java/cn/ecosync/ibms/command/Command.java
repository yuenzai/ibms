package cn.ecosync.ibms.command;

public interface Command {
    default void validate() {
    }
}
