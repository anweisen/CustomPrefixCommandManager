package net.codingarea.engine.utils;

import net.dv8tion.jda.annotations.DeprecatedSince;
import net.dv8tion.jda.annotations.ReplaceWith;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.ErrorResponse;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.internal.requests.RestActionImpl;
import net.dv8tion.jda.internal.requests.Route;
import net.dv8tion.jda.internal.requests.Route.CompiledRoute;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * @author anweisen | https://github.com/anweisen
 * @since 2.7
 */
public class GateWayHelper {

	@Nonnull
	@CheckReturnValue
	public static String defaultAvatar() {
		return "https://discordapp.com/assets/322c936a8c8be1b803cd94861bdfa868.png";
	}

	/**
	 * The following {@link ErrorResponse ErrorResponses} are possible:
	 * <ul>
	 *     <li>
	 *         {@link ErrorResponse#MISSING_ACCESS}
	 *         <br>The {@link Message Message}, which should be published, was not sent in an announcement channel or
	 *         <br>the {@link Message Message} was sent by the bot, but it is missing the permission {@link Permission#MESSAGE_WRITE} or
	 *         <br>the {@link Message Message} was not sent by the bot and it is missing the permission {@link Permission#MESSAGE_MANAGE}
	 *     </li>
	 * </ul>
	 *
	 * @param message The message which should be published
	 * @return {@link RestAction<Void> RestAction<Void>}
	 *
	 * @throws NullPointerException
	 *         If the {@link Message Message} is {@code null}
	 *
	 * @deprecated Replaced with {@link Message#crosspost()}
	 * @see Message#crosspost()
	 */
	@Nonnull
	@Deprecated
	@CheckReturnValue
	@DeprecatedSince("JDA 4.2.0_214")
	@ReplaceWith("Message#crosspost()")
	public static RestAction<Void> publishMessage(@Nonnull Message message) {
		CompiledRoute route = Route.post("channels/{channel.id}/messages/{message.id}/crosspost").compile(message.getChannel().getId(), message.getId());
		return new RestActionImpl<>(message.getJDA(), route);
	}

}
