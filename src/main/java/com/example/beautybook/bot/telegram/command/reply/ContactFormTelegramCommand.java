package com.example.beautybook.bot.telegram.command.reply;

import com.example.beautybook.bot.Command;
import com.example.beautybook.bot.telegram.TelegramBot;
import com.example.beautybook.bot.telegram.provider.ReplyMessageTelegramCommandProvider;
import com.example.beautybook.exceptions.EntityNotFoundException;
import com.example.beautybook.message.MessageProvider;
import com.example.beautybook.model.ContactForm;
import com.example.beautybook.repository.ContactFormRepository;
import com.example.beautybook.util.EmailSenderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class ContactFormTelegramCommand implements Command<ReplyMessageTelegramCommandProvider> {
    private static final String DELIMITER = ";";
    private static final int INDEX_LINE = 0;
    private final ContactFormRepository contactFormRepository;
    private final EmailSenderUtil emailSenderUtil;

    @Override
    public String getKey() {
        return "#ContactFormaId";
    }

    @Override
    public void executeCommand(Object o, Object bot) {
        if (o instanceof Update update && bot instanceof TelegramBot telegramBot) {
            String line =
                    update.getMessage().getReplyToMessage().getText().split(DELIMITER)[INDEX_LINE];
            Long contactFormId =
                    Long.parseLong(line.replace("#ContactFormaId-", ""));

            System.out.println(contactFormId);

            ContactForm contactForm = contactFormRepository.findById(contactFormId).orElseThrow(
                    () -> new EntityNotFoundException(
                            "Not found Contact Form by id " + contactFormId
                    )
            );
            contactForm.setResponse(update.getMessage().getText());
            contactFormRepository.save(contactForm);

            String text = MessageProvider.getMessage(
                    "contact.form",
                    contactForm.getMessage(),
                    contactForm.getResponse()
            );
            emailSenderUtil.sendEmail(contactForm.getEmail(), "Response", text);
        }
    }
}
