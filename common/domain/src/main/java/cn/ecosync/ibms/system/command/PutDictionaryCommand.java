package cn.ecosync.ibms.system.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.system.model.DictionaryKey;
import cn.ecosync.ibms.system.model.DictionaryValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class PutDictionaryCommand implements Command {
    private final DictionaryKey dictKey;
    private final DictionaryValue dictValue;
}
