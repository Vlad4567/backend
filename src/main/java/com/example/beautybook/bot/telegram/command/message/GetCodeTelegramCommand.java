package com.example.beautybook.bot.telegram.command.message;

import com.example.beautybook.bot.Command;
import com.example.beautybook.bot.telegram.TelegramBot;
import com.example.beautybook.bot.telegram.provider.MessageTelegramCommandProvider;
import com.example.beautybook.message.MessageProvider;
import com.example.beautybook.model.TelegramCode;
import com.example.beautybook.util.impl.TelegramGenerateCodeUtil;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class GetCodeTelegramCommand implements Command<MessageTelegramCommandProvider> {
    private final TelegramGenerateCodeUtil generateCodeUtil;
    private final ScheduledExecutorService executor;

    @Override
    public String getKey() {
        return "Get code";
    }

    @Override
    public void executeCommand(Object o, Object bot) {
        if (o instanceof Update update && bot instanceof TelegramBot telegramBot) {
            String text = createText(update);
            Long chatId = update.getMessage().getChatId();
            int idMessage = update.getMessage().getMessageId();
            int idNewMessage = telegramBot.send(
                    SendMessage.builder()
                            .chatId(chatId)
                            .text(text)
                            .parseMode(ParseMode.MARKDOWN)
                            .build());
            for (int i = 55; i > 0; i -= 5) {
                String finalText = text.substring(0, text.length() - 10) + i + " seconds";
                executor.schedule(
                        () -> telegramBot.updateTextMessage(chatId, idNewMessage, finalText),
                        60 - i,
                        TimeUnit.SECONDS);
            }
            executor.schedule(() ->
                    telegramBot.deleteMessage(chatId, idMessage), 60, TimeUnit.SECONDS);
            executor.schedule(() ->
                    telegramBot.deleteMessage(chatId, idNewMessage), 60, TimeUnit.SECONDS);
        }
    }

    private String createText(Update update) {
        TelegramCode code = generateCodeUtil.getCode(update);
        if (code.getOperation().equals(TelegramCode.Operation.VERIFICATION)) {
            return MessageProvider.getMessage("verification.code", code.getCode());
        } else {
            return MessageProvider.getMessage("login.code", code.getCode());
        }
    }
}
