package com.example.beautybook.util.impl;

import com.example.beautybook.model.TelegramCode;
import com.example.beautybook.model.User;
import com.example.beautybook.repository.TelegramCodeRepository;
import com.example.beautybook.repository.user.UserRepository;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class TelegramGenerateCodeUtil {
    private final ScheduledExecutorService executor;
    private final TelegramCodeRepository telegramCodeRepository;
    private final UserRepository userRepository;
    private final Random random;

    public TelegramCode getCode(Update update) {
        Long code = null;
        do {
            code = random.nextLong(111111, 999999);
        } while (telegramCodeRepository.existsByCode(code));

        Long chatId = update.getMessage().getChatId();
        String username = update.getMessage().getFrom().getUserName();
        Optional<User> user = userRepository.findByTelegramAccountChatId(chatId);
        TelegramCode telegramCode = null;
        if (user.isPresent()) {
            telegramCode = telegramCodeRepository.save(new TelegramCode(
                    code, chatId, username, user.get().getUuid(), TelegramCode.Operation.LOGIN));
        } else {
            telegramCode = telegramCodeRepository.save(new TelegramCode(
                    code, chatId, username, null, TelegramCode.Operation.VERIFICATION));
        }
        Long finalCode = code;
        executor.schedule(() -> deleteCode(finalCode), 1, TimeUnit.MINUTES);
        return telegramCode;
    }

    public void deleteCode(Long code) {
        telegramCodeRepository.deleteById(code);
    }

    public void shutdownExecutor() {
        executor.shutdown();
    }

}
