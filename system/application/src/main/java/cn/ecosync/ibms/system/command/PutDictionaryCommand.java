package cn.ecosync.ibms.system.command;

import cn.ecosync.ibms.command.Command;
import cn.ecosync.ibms.system.model.DictionaryKey;
import cn.ecosync.ibms.system.model.DictionaryValue;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@ToString
public class PutDictionaryCommand implements Command {
    @NotNull
    private String dictKey;
    @Valid
    @NotNull
    private DictionaryValue dictValue;

    public DictionaryKey toDictionaryKey() {
        return new DictionaryKey(dictKey);
    }
}
