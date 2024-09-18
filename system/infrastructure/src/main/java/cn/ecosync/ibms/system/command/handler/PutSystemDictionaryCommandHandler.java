package cn.ecosync.ibms.system.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.system.command.PutSystemDictionaryCommand;
import cn.ecosync.ibms.system.model.SystemDictionary;
import cn.ecosync.ibms.system.model.SystemDictionaryKey;
import cn.ecosync.ibms.system.model.SystemDictionaryValue;
import cn.ecosync.ibms.system.repository.SystemDictionaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PutSystemDictionaryCommandHandler implements CommandHandler<PutSystemDictionaryCommand> {
    private final SystemDictionaryRepository repository;

    @Override
    @Transactional
    public void handle(PutSystemDictionaryCommand command) {
        SystemDictionaryKey key = new SystemDictionaryKey(command.getDictKey());
        SystemDictionaryValue value = new SystemDictionaryValue(command.getDictValue());
        SystemDictionary systemDictionary = repository.get(key).orElse(null);
        if (systemDictionary == null) {
            systemDictionary = new SystemDictionary(key, value);
            repository.put(systemDictionary);
        } else {
            systemDictionary.setValue(value);
        }
    }
}
