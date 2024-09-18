package cn.ecosync.ibms.system.command;

import cn.ecosync.ibms.command.Command;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@RequiredArgsConstructor
public class PutSystemDictionaryCommand implements Command {
    private final String dictKey;
    private final Map<String, Object> dictValue;
}
