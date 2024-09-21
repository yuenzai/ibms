package cn.ecosync.ibms.system.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.system.model.DictionaryKey;
import cn.ecosync.ibms.system.model.DictionaryValue;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PutDictionaryCommand implements Command {
    private DictionaryKey dictKey;
    private DictionaryValue dictValue;
}
