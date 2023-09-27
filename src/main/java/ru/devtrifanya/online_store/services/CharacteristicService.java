package ru.devtrifanya.online_store.services;

import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.devtrifanya.online_store.repositories.CharacteristicRepository;
import ru.devtrifanya.online_store.repositories.ItemCharacteristicRepository;

@Service
@Transactional
@Data
public class CharacteristicService {
    private final CharacteristicRepository characteristicRepository;
    private final ItemCharacteristicRepository itemCharacteristicRepository;
}
