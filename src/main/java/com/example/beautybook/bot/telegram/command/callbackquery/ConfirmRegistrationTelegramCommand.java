package com.example.beautybook.bot.telegram.command.callbackquery;

import com.example.beautybook.bot.Command;
import com.example.beautybook.bot.telegram.TelegramBot;
import com.example.beautybook.bot.telegram.provider.CallbackQueryTelegramCommandProvider;
import com.example.beautybook.message.MessageProvider;
import com.example.beautybook.model.Role;
import com.example.beautybook.model.TelegramAccount;
import com.example.beautybook.model.User;
import com.example.beautybook.repository.TelegramAccountRepository;
import com.example.beautybook.repository.user.UserRepository;
import com.example.beautybook.util.PasswordGeneratorUtil;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

@Component
@RequiredArgsConstructor
public class ConfirmRegistrationTelegramCommand
        implements Command<CallbackQueryTelegramCommandProvider> {
    private final TelegramAccountRepository telegramAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordGeneratorUtil passwordGeneratorUtil;
    private final UserRepository userRepository;

    @Override
    public String getKey() {
        return "#confirm:registration";
    }

    @Transactional
    @Override
    public void executeCommand(Object o, Object bot) {
        if (o instanceof Update update && bot instanceof TelegramBot telegramBot) {
            String username = update.getCallbackQuery().getFrom().getUserName();
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String text;
            if (telegramAccountRepository.existsByChatId(chatId)) {
                text = "User with such a telegram account already exists";
                telegramBot.send(SendMessage.builder().chatId(chatId).text(text).build());
            }

            if (username == null
                    || username.isBlank()
                    || userRepository.existsByUsername(username)) {
                username = "Id" + update.getCallbackQuery().getFrom().getId();
            }
            try {
                User newUser = new User();
                newUser.setTelegramAccount(new TelegramAccount(username, chatId));
                newUser.setUsername(username);
                newUser.setUuid(UUID.randomUUID().toString());

                String password = passwordGeneratorUtil.generateRandomPassword();
                newUser.setPassword(passwordEncoder.encode(password));
                newUser = userRepository.save(newUser);
                newUser.setUuid(newUser.getUuid() + "-" + newUser.getId());
                Set<Role> roles = new HashSet<>();
                roles.add(new Role(4L));
                newUser.setRoles(roles);
                newUser.setUsername(newUser.getUserName() + "-Id-" + newUser.getId());
                userRepository.save(newUser);
                text = MessageProvider.getMessage(
                        "user.registration", newUser.getUserName(), chatId);
                telegramBot.send(
                        SendMessage.builder()
                                .chatId(chatId)
                                .text(text)
                                .replyMarkup(createKeyboardMarkup())
                                .parseMode(ParseMode.MARKDOWN)
                                .build()
                );
                telegramBot.send(SendMessage.builder().chatId(chatId).text(password).build());
                telegramBot.send(
                        SendMessage.builder()
                                .chatId(chatId)
                                .text(MessageProvider.getMessage("password"))
                                .build()
                );
            } catch (Exception e) {
                text = "Registration error.";
                telegramBot.send(SendMessage.builder().chatId(chatId).text(text).build());
            }
        }
    }

    private ReplyKeyboardMarkup createKeyboardMarkup() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Get code");
        row1.add("Support");
        keyboard.add(row1);
        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }
}
