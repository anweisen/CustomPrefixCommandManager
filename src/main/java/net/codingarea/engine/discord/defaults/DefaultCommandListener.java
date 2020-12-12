package net.codingarea.engine.discord.defaults;

import net.codingarea.engine.discord.commandmanager.CommandResult;
import net.codingarea.engine.discord.commandmanager.ICommandHandler;
import net.codingarea.engine.discord.commandmanager.helper.CommandHelper;
import net.codingarea.engine.discord.listener.DiscordEvent;
import net.codingarea.engine.discord.listener.Listener;
import net.codingarea.engine.utils.CoolDownManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

import static net.codingarea.engine.utils.NumberFormatter.DEFAULT;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 1.2
 */
public class DefaultCommandListener implements Listener {

	protected final ICommandHandler handler;

	public DefaultCommandListener(@Nonnull ICommandHandler handler) {
		this.handler = handler;
	}

	@Nonnull
	@CheckReturnValue
	public ICommandHandler getHandler() {
		return handler;
	}

	@DiscordEvent
	public void onMessage(@Nonnull MessageReceivedEvent event) {
		handler.handleEvent(event).thenAccept(result -> react(result, event));
	}

	@DiscordEvent
	public void onEdit(@Nonnull MessageUpdateEvent event) {
		handler.handleEvent(event).thenAccept(result -> react(result, event));
	}

	protected void react(@Nonnull CommandResult result, @Nonnull GenericMessageEvent event) {

		if (result.isSystemResult()) return;
		String answer = result.getAnswer();
		if (answer == null) return;

		switch (result) {
			case MEMBER_ON_COOLDOWN:
				CoolDownManager cooldown = handler.getCoolDownManager();
				if (cooldown != null) {
					Member member = CommandHelper.getMember(event);
					if (member != null) {
						answer = answer.replace("%cooldown%", DEFAULT.format(cooldown.getCoolDownLeft(member) / 1000F) + "s");
					}
				}
		}

		Message message = CommandHelper.getMessage(event);
		if (message == null) return;
		message.reply(answer).mentionRepliedUser(false).queue();

	}

}
