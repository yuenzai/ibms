package cn.ecosync.ibms.command;

public interface CommandHandler<T extends Command> {
    void handle(T command);
}
