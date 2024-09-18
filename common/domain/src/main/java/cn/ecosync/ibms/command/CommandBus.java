package cn.ecosync.ibms.command;

public interface CommandBus {
    void execute(Command command);
}
