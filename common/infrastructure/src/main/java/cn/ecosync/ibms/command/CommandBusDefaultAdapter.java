package cn.ecosync.ibms.command;

import cn.ecosync.ibms.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 覃俊元
 * @since 2024
 */
@Slf4j
@Component
public class CommandBusDefaultAdapter implements CommandBus {
    private final Map<Class<?>, CommandHandler<?>> commandHandlers;

    public CommandBusDefaultAdapter(List<CommandHandler<?>> commandHandlers) {
        this.commandHandlers = commandHandlers.stream()
                .map(in -> new AbstractMap.SimpleImmutableEntry<>(getCommandType(in), in))
                .filter(in -> in.getKey() != null)
                .peek(in -> log.info("register command handler[{}]", CollectionUtils.mapOf("commandType", in.getKey().getCanonicalName(), "commandHandler", in.getValue().getClass().getCanonicalName())))
                .collect(Collectors.toMap(AbstractMap.SimpleImmutableEntry::getKey, Map.Entry::getValue));
    }

    @Override
    public void execute(Command command) {
        Assert.notNull(command, "command is null");
        CommandHandler<?> commandHandler = commandHandlers.get(command.getClass());
        Assert.notNull(commandHandler, "command handler not found: " + command.getClass().getCanonicalName());
        Method handleMethod = ReflectionUtils.findMethod(commandHandler.getClass(), "handle", command.getClass());
        Assert.notNull(handleMethod, "handle method not found: " + commandHandler.getClass().getCanonicalName());
        log.debug("execute command[{}]", CollectionUtils.mapOf("commandType", command.getClass().getCanonicalName(), "commandHandler", commandHandler.getClass().getCanonicalName()));
        command.validate();
        ReflectionUtils.invokeMethod(handleMethod, commandHandler, command);
    }

    private Class<?> getCommandType(CommandHandler<?> commandHandler) {
        return ResolvableType.forClass(CommandHandler.class, commandHandler.getClass())
                .resolveGeneric(0);
    }
}
