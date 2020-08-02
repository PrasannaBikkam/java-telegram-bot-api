package com.poc.telegram;

import java.util.List;

import org.springframework.stereotype.Component;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;
import com.pengrad.telegrambot.response.SendResponse;

@Component
public class PrasannaBot {
	// Create your bot passing the token received from @BotFather
	TelegramBot bot = new TelegramBot("1200419395:AAHIWnuUDDu86GwlbEXbXn19oQqcwe5usRI");
//	String response = callGetUpdates(bot);
	String response = callupdateListener(bot);

	public String callupdateListener(TelegramBot bot) {
		bot.setUpdatesListener(new UpdatesListener() {
			@Override
			public int process(List<Update> updates) {
				for (Update update : updates) {
					String chatId = update.message().chat().id().toString();
					Message message = update.message();
					if (message.text() != null && message.text().equals("/register")) {
						KeyboardButton[] keyboards = { new KeyboardButton("register now").requestContact(true) };
						bot.execute(new SendMessage(chatId, "Click the below button to register!!")
								.replyMarkup(new ReplyKeyboardMarkup(keyboards).oneTimeKeyboard(true)
										.resizeKeyboard(true).selective(true)));
					} else if (message.contact() != null) {
						SendResponse response = bot.execute(new SendMessage(chatId,
								"Successfully registed with mobile number" + message.contact().phoneNumber()));
					}
				}
				return UpdatesListener.CONFIRMED_UPDATES_ALL;
			}
		});
		return null;
	}

	public String callGetUpdates(TelegramBot bot) {
		int updateId = 0;
		while (true) {
			System.out.println("Checking for updates...");
			GetUpdates getUpdates = new GetUpdates().limit(100).offset(updateId).timeout(0);
			GetUpdatesResponse updatesResponse = bot.execute(getUpdates);
			List<Update> updates = updatesResponse.updates();
			if (updates.size() > 0) {
				Update update = updates.get(updates.size() - 1);
				Message message = update.message();
				// Send messages
				long chatId = message.chat().id();
				if (message.contact() != null) {
					SendResponse response = bot.execute(new SendMessage(chatId,
							"Successfully registed with mobile number" + message.contact().phoneNumber()));
				} else if (message.text() != null && message.text().equals("/register")) {
					KeyboardButton[] keyboards = { new KeyboardButton("register now").requestContact(true) };
					SendResponse response = bot.execute(new SendMessage(chatId, "Click the below button to register!!")
							.replyMarkup(new ReplyKeyboardMarkup(keyboards).oneTimeKeyboard(true).resizeKeyboard(true)
									.selective(true)));
				}
				updateId = update.updateId() + 1;
			}

		}
	}

}
