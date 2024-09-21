package cn.ecosync.ibms.system.command.handler;

import cn.ecosync.ibms.command.CommandHandler;
import cn.ecosync.ibms.system.command.PutDictionaryCommand;
import cn.ecosync.ibms.system.model.Dictionary;
import cn.ecosync.ibms.system.model.DictionaryKey;
import cn.ecosync.ibms.system.model.DictionaryValue;
import cn.ecosync.ibms.system.repository.DictionaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PutDictionaryCommandHandler implements CommandHandler<PutDictionaryCommand> {
    private final DictionaryRepository repository;

    @Override
    @Transactional
    public void handle(PutDictionaryCommand command) {
        DictionaryKey key = command.getDictKey();
        DictionaryValue value = command.getDictValue();
        Dictionary dictionary = repository.get(key).orElse(null);
        if (dictionary == null) {
            dictionary = new Dictionary(key, value);
            repository.put(dictionary);
        } else {
            dictionary.setValue(value);
        }
    }
}
