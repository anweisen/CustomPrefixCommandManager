package net.anweisen.listener;

import net.dv8tion.jda.api.events.*;
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent;
import net.dv8tion.jda.api.events.channel.category.GenericCategoryEvent;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdatePermissionsEvent;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdatePositionEvent;
import net.dv8tion.jda.api.events.channel.category.update.GenericCategoryUpdateEvent;
import net.dv8tion.jda.api.events.channel.priv.PrivateChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.priv.PrivateChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.store.GenericStoreChannelEvent;
import net.dv8tion.jda.api.events.channel.store.StoreChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.store.StoreChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.store.update.GenericStoreChannelUpdateEvent;
import net.dv8tion.jda.api.events.channel.store.update.StoreChannelUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.store.update.StoreChannelUpdatePermissionsEvent;
import net.dv8tion.jda.api.events.channel.store.update.StoreChannelUpdatePositionEvent;
import net.dv8tion.jda.api.events.channel.text.GenericTextChannelEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.text.update.*;
import net.dv8tion.jda.api.events.channel.voice.GenericVoiceChannelEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.voice.update.*;
import net.dv8tion.jda.api.events.emote.EmoteAddedEvent;
import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent;
import net.dv8tion.jda.api.events.emote.GenericEmoteEvent;
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateNameEvent;
import net.dv8tion.jda.api.events.emote.update.EmoteUpdateRolesEvent;
import net.dv8tion.jda.api.events.emote.update.GenericEmoteUpdateEvent;
import net.dv8tion.jda.api.events.guild.*;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.events.guild.member.update.GenericGuildMemberUpdateEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.guild.update.*;
import net.dv8tion.jda.api.events.guild.voice.*;
import net.dv8tion.jda.api.events.http.HttpRequestEvent;
import net.dv8tion.jda.api.events.message.*;
import net.dv8tion.jda.api.events.message.guild.*;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.message.priv.*;
import net.dv8tion.jda.api.events.message.priv.react.GenericPrivateMessageReactionEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.role.GenericRoleEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.*;
import net.dv8tion.jda.api.events.self.*;
import net.dv8tion.jda.api.events.user.GenericUserEvent;
import net.dv8tion.jda.api.events.user.UserActivityEndEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.events.user.UserTypingEvent;
import net.dv8tion.jda.api.events.user.update.*;
import net.dv8tion.jda.api.hooks.EventListener;

import javax.annotation.Nonnull;

/**
 * @author anweisen
 * CommandManager developed on 06-20-2020
 * https://github.com/anweisen
 */

public interface Listener extends EventListener {

	default void onGenericEvent(@Nonnull GenericEvent event) {}
	default void onGenericUpdate(@Nonnull UpdateEvent<?, ?> event) {}
	default void onRawGateway(@Nonnull RawGatewayEvent event) {}
	default void onGatewayPing(@Nonnull GatewayPingEvent event) {}

	//JDA Events
	default void onReady(@Nonnull ReadyEvent event) {}
	default void onResume(@Nonnull ResumedEvent event) {}
	default void onReconnect(@Nonnull ReconnectedEvent event) {}
	default void onDisconnect(@Nonnull DisconnectEvent event) {}
	default void onShutdown(@Nonnull ShutdownEvent event) {}
	default void onStatusChange(@Nonnull StatusChangeEvent event) {}
	default void onException(@Nonnull ExceptionEvent event) {}

	//User Events
	default void onUserUpdateName(@Nonnull UserUpdateNameEvent event) {}
	default void onUserUpdateDiscriminator(@Nonnull UserUpdateDiscriminatorEvent event) {}
	default void onUserUpdateAvatar(@Nonnull UserUpdateAvatarEvent event) {}
	default void onUserUpdateOnlineStatus(@Nonnull UserUpdateOnlineStatusEvent event) {}
	default void onUserUpdateActivityOrder(@Nonnull UserUpdateActivityOrderEvent event) {}
	default void onUserTyping(@Nonnull UserTypingEvent event) {}
	default void onUserActivityStart(@Nonnull UserActivityStartEvent event) {}
	default void onUserActivityEnd(@Nonnull UserActivityEndEvent event) {}

	//Self Events. Fires only in relation to the currently logged in account.
	default void onSelfUpdateAvatar(@Nonnull SelfUpdateAvatarEvent event) {}
	default void onSelfUpdateEmail(@Nonnull SelfUpdateEmailEvent event) {}
	default void onSelfUpdateMFA(@Nonnull SelfUpdateMFAEvent event) {}
	default void onSelfUpdateName(@Nonnull SelfUpdateNameEvent event) {}
	default void onSelfUpdateVerified(@Nonnull SelfUpdateVerifiedEvent event) {}

	//Message Events
	//Guild (TextChannel) Message Events
	default void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {}
	default void onGuildMessageUpdate(@Nonnull GuildMessageUpdateEvent event) {}
	default void onGuildMessageDelete(@Nonnull GuildMessageDeleteEvent event) {}
	default void onGuildMessageEmbed(@Nonnull GuildMessageEmbedEvent event) {}
	default void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent event) {}
	default void onGuildMessageReactionRemove(@Nonnull GuildMessageReactionRemoveEvent event) {}
	default void onGuildMessageReactionRemoveAll(@Nonnull GuildMessageReactionRemoveAllEvent event) {}

	//Private Message Events
	default void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {}
	default void onPrivateMessageUpdate(@Nonnull PrivateMessageUpdateEvent event) {}
	default void onPrivateMessageDelete(@Nonnull PrivateMessageDeleteEvent event) {}
	default void onPrivateMessageEmbed(@Nonnull PrivateMessageEmbedEvent event) {}
	default void onPrivateMessageReactionAdd(@Nonnull PrivateMessageReactionAddEvent event) {}
	default void onPrivateMessageReactionRemove(@Nonnull PrivateMessageReactionRemoveEvent event) {}

	//Combined Message Events (Combines Guild and Private message into 1 event)
	default void onMessageReceived(@Nonnull MessageReceivedEvent event) {}
	default void onMessageUpdate(@Nonnull MessageUpdateEvent event) {}
	default void onMessageDelete(@Nonnull MessageDeleteEvent event) {}
	default void onMessageBulkDelete(@Nonnull MessageBulkDeleteEvent event) {}
	default void onMessageEmbed(@Nonnull MessageEmbedEvent event) {}
	default void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {}
	default void onMessageReactionRemove(@Nonnull MessageReactionRemoveEvent event) {}
	default void onMessageReactionRemoveAll(@Nonnull MessageReactionRemoveAllEvent event) {}

	//StoreChannel Events
	default void onStoreChannelDelete(@Nonnull StoreChannelDeleteEvent event) {}
	default void onStoreChannelUpdateName(@Nonnull StoreChannelUpdateNameEvent event) {}
	default void onStoreChannelUpdatePosition(@Nonnull StoreChannelUpdatePositionEvent event) {}
	default void onStoreChannelUpdatePermissions(@Nonnull StoreChannelUpdatePermissionsEvent event) {}
	default void onStoreChannelCreate(@Nonnull StoreChannelCreateEvent event) {}

	//TextChannel Events
	default void onTextChannelDelete(@Nonnull TextChannelDeleteEvent event) {}
	default void onTextChannelUpdateName(@Nonnull TextChannelUpdateNameEvent event) {}
	default void onTextChannelUpdateTopic(@Nonnull TextChannelUpdateTopicEvent event) {}
	default void onTextChannelUpdatePosition(@Nonnull TextChannelUpdatePositionEvent event) {}
	default void onTextChannelUpdatePermissions(@Nonnull TextChannelUpdatePermissionsEvent event) {}
	default void onTextChannelUpdateNSFW(@Nonnull TextChannelUpdateNSFWEvent event) {}
	default void onTextChannelUpdateParent(@Nonnull TextChannelUpdateParentEvent event) {}
	default void onTextChannelUpdateSlowmode(@Nonnull TextChannelUpdateSlowmodeEvent event) {}
	default void onTextChannelCreate(@Nonnull TextChannelCreateEvent event) {}

	//VoiceChannel Events
	default void onVoiceChannelDelete(@Nonnull VoiceChannelDeleteEvent event) {}
	default void onVoiceChannelUpdateName(@Nonnull VoiceChannelUpdateNameEvent event) {}
	default void onVoiceChannelUpdatePosition(@Nonnull VoiceChannelUpdatePositionEvent event) {}
	default void onVoiceChannelUpdateUserLimit(@Nonnull VoiceChannelUpdateUserLimitEvent event) {}
	default void onVoiceChannelUpdateBitrate(@Nonnull VoiceChannelUpdateBitrateEvent event) {}
	default void onVoiceChannelUpdatePermissions(@Nonnull VoiceChannelUpdatePermissionsEvent event) {}
	default void onVoiceChannelUpdateParent(@Nonnull VoiceChannelUpdateParentEvent event) {}
	default void onVoiceChannelCreate(@Nonnull VoiceChannelCreateEvent event) {}

	//Category Events
	default void onCategoryDelete(@Nonnull CategoryDeleteEvent event) {}
	default void onCategoryUpdateName(@Nonnull CategoryUpdateNameEvent event) {}
	default void onCategoryUpdatePosition(@Nonnull CategoryUpdatePositionEvent event) {}
	default void onCategoryUpdatePermissions(@Nonnull CategoryUpdatePermissionsEvent event) {}
	default void onCategoryCreate(@Nonnull CategoryCreateEvent event) {}

	//PrivateChannel Events
	default void onPrivateChannelCreate(@Nonnull PrivateChannelCreateEvent event) {}
	default void onPrivateChannelDelete(@Nonnull PrivateChannelDeleteEvent event) {}

	//Guild Events
	default void onGuildReady(@Nonnull GuildReadyEvent event) {}
	default void onGuildJoin(@Nonnull GuildJoinEvent event) {}
	default void onGuildLeave(@Nonnull GuildLeaveEvent event) {}
	default void onGuildAvailable(@Nonnull GuildAvailableEvent event) {}
	default void onGuildUnavailable(@Nonnull GuildUnavailableEvent event) {}
	default void onUnavailableGuildJoined(@Nonnull UnavailableGuildJoinedEvent event) {}
	default void onUnavailableGuildLeave(@Nonnull UnavailableGuildLeaveEvent event) {}
	default void onGuildBan(@Nonnull GuildBanEvent event) {}
	default void onGuildUnban(@Nonnull GuildUnbanEvent event) {}

	//Guild Update Events
	default void onGuildUpdateAfkChannel(@Nonnull GuildUpdateAfkChannelEvent event) {}
	default void onGuildUpdateSystemChannel(@Nonnull GuildUpdateSystemChannelEvent event) {}
	default void onGuildUpdateAfkTimeout(@Nonnull GuildUpdateAfkTimeoutEvent event) {}
	default void onGuildUpdateExplicitContentLevel(@Nonnull GuildUpdateExplicitContentLevelEvent event) {}
	default void onGuildUpdateIcon(@Nonnull GuildUpdateIconEvent event) {}
	default void onGuildUpdateMFALevel(@Nonnull GuildUpdateMFALevelEvent event) {}
	default void onGuildUpdateName(@Nonnull GuildUpdateNameEvent event){}
	default void onGuildUpdateNotificationLevel(@Nonnull GuildUpdateNotificationLevelEvent event) {}
	default void onGuildUpdateOwner(@Nonnull GuildUpdateOwnerEvent event) {}
	default void onGuildUpdateRegion(@Nonnull GuildUpdateRegionEvent event) {}
	default void onGuildUpdateSplash(@Nonnull GuildUpdateSplashEvent event) {}
	default void onGuildUpdateVerificationLevel(@Nonnull GuildUpdateVerificationLevelEvent event) {}
	default void onGuildUpdateFeatures(@Nonnull GuildUpdateFeaturesEvent event) {}
	default void onGuildUpdateVanityCode(@Nonnull GuildUpdateVanityCodeEvent event) {}
	default void onGuildUpdateBanner(@Nonnull GuildUpdateBannerEvent event) {}
	default void onGuildUpdateDescription(@Nonnull GuildUpdateDescriptionEvent event) {}
	default void onGuildUpdateBoostTier(@Nonnull GuildUpdateBoostTierEvent event) {}
	default void onGuildUpdateBoostCount(@Nonnull GuildUpdateBoostCountEvent event) {}
	default void onGuildUpdateMaxMembers(@Nonnull GuildUpdateMaxMembersEvent event) {}
	default void onGuildUpdateMaxPresences(@Nonnull GuildUpdateMaxPresencesEvent event) {}

	//Guild Member Events
	default void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent event) {}
	default void onGuildMemberLeave(@Nonnull GuildMemberLeaveEvent event) {}
	default void onGuildMemberRoleAdd(@Nonnull GuildMemberRoleAddEvent event) {}
	default void onGuildMemberRoleRemove(@Nonnull GuildMemberRoleRemoveEvent event) {}

	//Guild Member Update Events
	default void onGuildMemberUpdateNickname(@Nonnull GuildMemberUpdateNicknameEvent event) {}
	default void onGuildMemberUpdateBoostTime(@Nonnull GuildMemberUpdateBoostTimeEvent event) {}

	//Guild Voice Events
	default void onGuildVoiceUpdate(@Nonnull GuildVoiceUpdateEvent event) {}
	default void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {}
	default void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event) {}
	default void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {}
	default void onGuildVoiceMute(@Nonnull GuildVoiceMuteEvent event) {}
	default void onGuildVoiceDeafen(@Nonnull GuildVoiceDeafenEvent event) {}
	default void onGuildVoiceGuildMute(@Nonnull GuildVoiceGuildMuteEvent event) {}
	default void onGuildVoiceGuildDeafen(@Nonnull GuildVoiceGuildDeafenEvent event) {}
	default void onGuildVoiceSelfMute(@Nonnull GuildVoiceSelfMuteEvent event) {}
	default void onGuildVoiceSelfDeafen(@Nonnull GuildVoiceSelfDeafenEvent event) {}
	default void onGuildVoiceSuppress(@Nonnull GuildVoiceSuppressEvent event) {}

	//Role events
	default void onRoleCreate(@Nonnull RoleCreateEvent event) {}
	default void onRoleDelete(@Nonnull RoleDeleteEvent event) {}

	//Role Update Events
	default void onRoleUpdateColor(@Nonnull RoleUpdateColorEvent event) {}
	default void onRoleUpdateHoisted(@Nonnull RoleUpdateHoistedEvent event) {}
	default void onRoleUpdateMentionable(@Nonnull RoleUpdateMentionableEvent event) {}
	default void onRoleUpdateName(@Nonnull RoleUpdateNameEvent event) {}
	default void onRoleUpdatePermissions(@Nonnull RoleUpdatePermissionsEvent event) {}
	default void onRoleUpdatePosition(@Nonnull RoleUpdatePositionEvent event) {}

	//Emote Events
	default void onEmoteAdded(@Nonnull EmoteAddedEvent event) {}
	default void onEmoteRemoved(@Nonnull EmoteRemovedEvent event) {}

	//Emote Update Events
	default void onEmoteUpdateName(@Nonnull EmoteUpdateNameEvent event) {}
	default void onEmoteUpdateRoles(@Nonnull EmoteUpdateRolesEvent event) {}

	// Debug Events
	default void onHttpRequest(@Nonnull HttpRequestEvent event) {}

	//Generic Events
	default void onGenericMessage(@Nonnull GenericMessageEvent event) {}
	default void onGenericMessageReaction(@Nonnull GenericMessageReactionEvent event) {}
	default void onGenericGuildMessage(@Nonnull GenericGuildMessageEvent event) {}
	default void onGenericGuildMessageReaction(@Nonnull GenericGuildMessageReactionEvent event) {}
	default void onGenericPrivateMessage(@Nonnull GenericPrivateMessageEvent event) {}
	default void onGenericPrivateMessageReaction(@Nonnull GenericPrivateMessageReactionEvent event) {}
	default void onGenericUser(@Nonnull GenericUserEvent event) {}
	default void onGenericUserPresence(@Nonnull GenericUserPresenceEvent event) {}
	default void onGenericSelfUpdate(@Nonnull GenericSelfUpdateEvent event) {}
	default void onGenericStoreChannel(@Nonnull GenericStoreChannelEvent event) {}
	default void onGenericStoreChannelUpdate(@Nonnull GenericStoreChannelUpdateEvent event) {}
	default void onGenericTextChannel(@Nonnull GenericTextChannelEvent event) {}
	default void onGenericTextChannelUpdate(@Nonnull GenericTextChannelUpdateEvent event) {}
	default void onGenericVoiceChannel(@Nonnull GenericVoiceChannelEvent event) {}
	default void onGenericVoiceChannelUpdate(@Nonnull GenericVoiceChannelUpdateEvent event) {}
	default void onGenericCategory(@Nonnull GenericCategoryEvent event) {}
	default void onGenericCategoryUpdate(@Nonnull GenericCategoryUpdateEvent event) {}
	default void onGenericGuild(@Nonnull GenericGuildEvent event) {}
	default void onGenericGuildUpdate(@Nonnull GenericGuildUpdateEvent event) {}
	default void onGenericGuildMember(@Nonnull GenericGuildMemberEvent event) {}
	default void onGenericGuildMemberUpdate(@Nonnull GenericGuildMemberUpdateEvent event) {}
	default void onGenericGuildVoice(@Nonnull GenericGuildVoiceEvent event) {}
	default void onGenericRole(@Nonnull GenericRoleEvent event) {}
	default void onGenericRoleUpdate(@Nonnull GenericRoleUpdateEvent event) {}
	default void onGenericEmote(@Nonnull GenericEmoteEvent event) {}
	default void onGenericEmoteUpdate(@Nonnull GenericEmoteUpdateEvent event) {}

	@Override
	default void onEvent(@Nonnull GenericEvent event) {
		onGenericEvent(event);
		if (event instanceof UpdateEvent)
			onGenericUpdate((UpdateEvent<?, ?>) event);
		else if (event instanceof RawGatewayEvent)
			onRawGateway((RawGatewayEvent) event);

		//JDA Events
		if (event instanceof ReadyEvent)
			onReady((ReadyEvent) event);
		else if (event instanceof ResumedEvent)
			onResume((ResumedEvent) event);
		else if (event instanceof ReconnectedEvent)
			onReconnect((ReconnectedEvent) event);
		else if (event instanceof DisconnectEvent)
			onDisconnect((DisconnectEvent) event);
		else if (event instanceof ShutdownEvent)
			onShutdown((ShutdownEvent) event);
		else if (event instanceof StatusChangeEvent)
			onStatusChange((StatusChangeEvent) event);
		else if (event instanceof ExceptionEvent)
			onException((ExceptionEvent) event);
		else if (event instanceof GatewayPingEvent)
			onGatewayPing((GatewayPingEvent) event);

			//Message Events
			//Guild (TextChannel) Message Events
		else if (event instanceof GuildMessageReceivedEvent)
			onGuildMessageReceived((GuildMessageReceivedEvent) event);
		else if (event instanceof GuildMessageUpdateEvent)
			onGuildMessageUpdate((GuildMessageUpdateEvent) event);
		else if (event instanceof GuildMessageDeleteEvent)
			onGuildMessageDelete((GuildMessageDeleteEvent) event);
		else if (event instanceof GuildMessageEmbedEvent)
			onGuildMessageEmbed((GuildMessageEmbedEvent) event);
		else if (event instanceof GuildMessageReactionAddEvent)
			onGuildMessageReactionAdd((GuildMessageReactionAddEvent) event);
		else if (event instanceof GuildMessageReactionRemoveEvent)
			onGuildMessageReactionRemove((GuildMessageReactionRemoveEvent) event);
		else if (event instanceof GuildMessageReactionRemoveAllEvent)
			onGuildMessageReactionRemoveAll((GuildMessageReactionRemoveAllEvent) event);

			//Private Message Events
		else if (event instanceof PrivateMessageReceivedEvent)
			onPrivateMessageReceived((PrivateMessageReceivedEvent) event);
		else if (event instanceof PrivateMessageUpdateEvent)
			onPrivateMessageUpdate((PrivateMessageUpdateEvent) event);
		else if (event instanceof PrivateMessageDeleteEvent)
			onPrivateMessageDelete((PrivateMessageDeleteEvent) event);
		else if (event instanceof PrivateMessageEmbedEvent)
			onPrivateMessageEmbed((PrivateMessageEmbedEvent) event);
		else if (event instanceof PrivateMessageReactionAddEvent)
			onPrivateMessageReactionAdd((PrivateMessageReactionAddEvent) event);
		else if (event instanceof PrivateMessageReactionRemoveEvent)
			onPrivateMessageReactionRemove((PrivateMessageReactionRemoveEvent) event);

			//Combined Message Events (Combines Guild and Private message into 1 event)
		else if (event instanceof MessageReceivedEvent)
			onMessageReceived((MessageReceivedEvent) event);
		else if (event instanceof MessageUpdateEvent)
			onMessageUpdate((MessageUpdateEvent) event);
		else if (event instanceof MessageDeleteEvent)
			onMessageDelete((MessageDeleteEvent) event);
		else if (event instanceof MessageBulkDeleteEvent)
			onMessageBulkDelete((MessageBulkDeleteEvent) event);
		else if (event instanceof MessageEmbedEvent)
			onMessageEmbed((MessageEmbedEvent) event);
		else if (event instanceof MessageReactionAddEvent)
			onMessageReactionAdd((MessageReactionAddEvent) event);
		else if (event instanceof MessageReactionRemoveEvent)
			onMessageReactionRemove((MessageReactionRemoveEvent) event);
		else if (event instanceof MessageReactionRemoveAllEvent)
			onMessageReactionRemoveAll((MessageReactionRemoveAllEvent) event);

			//User Events
		else if (event instanceof UserUpdateNameEvent)
			onUserUpdateName((UserUpdateNameEvent) event);
		else if (event instanceof UserUpdateDiscriminatorEvent)
			onUserUpdateDiscriminator((UserUpdateDiscriminatorEvent) event);
		else if (event instanceof UserUpdateAvatarEvent)
			onUserUpdateAvatar((UserUpdateAvatarEvent) event);
		else if (event instanceof UserUpdateActivityOrderEvent)
			onUserUpdateActivityOrder((UserUpdateActivityOrderEvent) event);
		else if (event instanceof UserUpdateOnlineStatusEvent)
			onUserUpdateOnlineStatus((UserUpdateOnlineStatusEvent) event);
		else if (event instanceof UserTypingEvent)
			onUserTyping((UserTypingEvent) event);
		else if (event instanceof UserActivityStartEvent)
			onUserActivityStart((UserActivityStartEvent) event);
		else if (event instanceof UserActivityEndEvent)
			onUserActivityEnd((UserActivityEndEvent) event);

			//Self Events
		else if (event instanceof SelfUpdateAvatarEvent)
			onSelfUpdateAvatar((SelfUpdateAvatarEvent) event);
		else if (event instanceof SelfUpdateEmailEvent)
			onSelfUpdateEmail((SelfUpdateEmailEvent) event);
		else if (event instanceof SelfUpdateMFAEvent)
			onSelfUpdateMFA((SelfUpdateMFAEvent) event);
		else if (event instanceof SelfUpdateNameEvent)
			onSelfUpdateName((SelfUpdateNameEvent) event);
		else if (event instanceof SelfUpdateVerifiedEvent)
			onSelfUpdateVerified((SelfUpdateVerifiedEvent) event);

			//StoreChannel Events
		else if (event instanceof StoreChannelCreateEvent)
			onStoreChannelCreate((StoreChannelCreateEvent) event);
		else if (event instanceof StoreChannelDeleteEvent)
			onStoreChannelDelete((StoreChannelDeleteEvent) event);
		else if (event instanceof StoreChannelUpdateNameEvent)
			onStoreChannelUpdateName((StoreChannelUpdateNameEvent) event);
		else if (event instanceof StoreChannelUpdatePositionEvent)
			onStoreChannelUpdatePosition((StoreChannelUpdatePositionEvent) event);
		else if (event instanceof StoreChannelUpdatePermissionsEvent)
			onStoreChannelUpdatePermissions((StoreChannelUpdatePermissionsEvent) event);

			//TextChannel Events
		else if (event instanceof TextChannelCreateEvent)
			onTextChannelCreate((TextChannelCreateEvent) event);
		else if (event instanceof TextChannelUpdateNameEvent)
			onTextChannelUpdateName((TextChannelUpdateNameEvent) event);
		else if (event instanceof TextChannelUpdateTopicEvent)
			onTextChannelUpdateTopic((TextChannelUpdateTopicEvent) event);
		else if (event instanceof TextChannelUpdatePositionEvent)
			onTextChannelUpdatePosition((TextChannelUpdatePositionEvent) event);
		else if (event instanceof TextChannelUpdatePermissionsEvent)
			onTextChannelUpdatePermissions((TextChannelUpdatePermissionsEvent) event);
		else if (event instanceof TextChannelUpdateNSFWEvent)
			onTextChannelUpdateNSFW((TextChannelUpdateNSFWEvent) event);
		else if (event instanceof TextChannelUpdateParentEvent)
			onTextChannelUpdateParent((TextChannelUpdateParentEvent) event);
		else if (event instanceof TextChannelUpdateSlowmodeEvent)
			onTextChannelUpdateSlowmode((TextChannelUpdateSlowmodeEvent) event);
		else if (event instanceof TextChannelDeleteEvent)
			onTextChannelDelete((TextChannelDeleteEvent) event);

			//VoiceChannel Events
		else if (event instanceof VoiceChannelCreateEvent)
			onVoiceChannelCreate((VoiceChannelCreateEvent) event);
		else if (event instanceof VoiceChannelUpdateNameEvent)
			onVoiceChannelUpdateName((VoiceChannelUpdateNameEvent) event);
		else if (event instanceof VoiceChannelUpdatePositionEvent)
			onVoiceChannelUpdatePosition((VoiceChannelUpdatePositionEvent) event);
		else if (event instanceof VoiceChannelUpdateUserLimitEvent)
			onVoiceChannelUpdateUserLimit((VoiceChannelUpdateUserLimitEvent) event);
		else if (event instanceof VoiceChannelUpdateBitrateEvent)
			onVoiceChannelUpdateBitrate((VoiceChannelUpdateBitrateEvent) event);
		else if (event instanceof VoiceChannelUpdatePermissionsEvent)
			onVoiceChannelUpdatePermissions((VoiceChannelUpdatePermissionsEvent) event);
		else if (event instanceof VoiceChannelUpdateParentEvent)
			onVoiceChannelUpdateParent((VoiceChannelUpdateParentEvent) event);
		else if (event instanceof VoiceChannelDeleteEvent)
			onVoiceChannelDelete((VoiceChannelDeleteEvent) event);

			//Category Events
		else if (event instanceof CategoryCreateEvent)
			onCategoryCreate((CategoryCreateEvent) event);
		else if (event instanceof CategoryUpdateNameEvent)
			onCategoryUpdateName((CategoryUpdateNameEvent) event);
		else if (event instanceof CategoryUpdatePositionEvent)
			onCategoryUpdatePosition((CategoryUpdatePositionEvent) event);
		else if (event instanceof CategoryUpdatePermissionsEvent)
			onCategoryUpdatePermissions((CategoryUpdatePermissionsEvent) event);
		else if (event instanceof CategoryDeleteEvent)
			onCategoryDelete((CategoryDeleteEvent) event);

			//PrivateChannel Events
		else if (event instanceof PrivateChannelCreateEvent)
			onPrivateChannelCreate((PrivateChannelCreateEvent) event);
		else if (event instanceof PrivateChannelDeleteEvent)
			onPrivateChannelDelete((PrivateChannelDeleteEvent) event);

			//Guild Events
		else if (event instanceof GuildReadyEvent)
			onGuildReady((GuildReadyEvent) event);
		else if (event instanceof GuildJoinEvent)
			onGuildJoin((GuildJoinEvent) event);
		else if (event instanceof GuildLeaveEvent)
			onGuildLeave((GuildLeaveEvent) event);
		else if (event instanceof GuildAvailableEvent)
			onGuildAvailable((GuildAvailableEvent) event);
		else if (event instanceof GuildUnavailableEvent)
			onGuildUnavailable((GuildUnavailableEvent) event);
		else if (event instanceof UnavailableGuildJoinedEvent)
			onUnavailableGuildJoined((UnavailableGuildJoinedEvent) event);
		else if (event instanceof UnavailableGuildLeaveEvent)
			onUnavailableGuildLeave((UnavailableGuildLeaveEvent) event);
		else if (event instanceof GuildBanEvent)
			onGuildBan((GuildBanEvent) event);
		else if (event instanceof GuildUnbanEvent)
			onGuildUnban((GuildUnbanEvent) event);

			//Guild Update Events
		else if (event instanceof GuildUpdateAfkChannelEvent)
			onGuildUpdateAfkChannel((GuildUpdateAfkChannelEvent) event);
		else if (event instanceof GuildUpdateSystemChannelEvent)
			onGuildUpdateSystemChannel((GuildUpdateSystemChannelEvent) event);
		else if (event instanceof GuildUpdateAfkTimeoutEvent)
			onGuildUpdateAfkTimeout((GuildUpdateAfkTimeoutEvent) event);
		else if (event instanceof GuildUpdateExplicitContentLevelEvent)
			onGuildUpdateExplicitContentLevel((GuildUpdateExplicitContentLevelEvent) event);
		else if (event instanceof GuildUpdateIconEvent)
			onGuildUpdateIcon((GuildUpdateIconEvent) event);
		else if (event instanceof GuildUpdateMFALevelEvent)
			onGuildUpdateMFALevel((GuildUpdateMFALevelEvent) event);
		else if (event instanceof GuildUpdateNameEvent)
			onGuildUpdateName((GuildUpdateNameEvent) event);
		else if (event instanceof GuildUpdateNotificationLevelEvent)
			onGuildUpdateNotificationLevel((GuildUpdateNotificationLevelEvent) event);
		else if (event instanceof GuildUpdateOwnerEvent)
			onGuildUpdateOwner((GuildUpdateOwnerEvent) event);
		else if (event instanceof GuildUpdateRegionEvent)
			onGuildUpdateRegion((GuildUpdateRegionEvent) event);
		else if (event instanceof GuildUpdateSplashEvent)
			onGuildUpdateSplash((GuildUpdateSplashEvent) event);
		else if (event instanceof GuildUpdateVerificationLevelEvent)
			onGuildUpdateVerificationLevel((GuildUpdateVerificationLevelEvent) event);
		else if (event instanceof GuildUpdateFeaturesEvent)
			onGuildUpdateFeatures((GuildUpdateFeaturesEvent) event);
		else if (event instanceof GuildUpdateVanityCodeEvent)
			onGuildUpdateVanityCode((GuildUpdateVanityCodeEvent) event);
		else if (event instanceof GuildUpdateBannerEvent)
			onGuildUpdateBanner((GuildUpdateBannerEvent) event);
		else if (event instanceof GuildUpdateDescriptionEvent)
			onGuildUpdateDescription((GuildUpdateDescriptionEvent) event);
		else if (event instanceof GuildUpdateBoostTierEvent)
			onGuildUpdateBoostTier((GuildUpdateBoostTierEvent) event);
		else if (event instanceof GuildUpdateBoostCountEvent)
			onGuildUpdateBoostCount((GuildUpdateBoostCountEvent) event);
		else if (event instanceof GuildUpdateMaxMembersEvent)
			onGuildUpdateMaxMembers((GuildUpdateMaxMembersEvent) event);
		else if (event instanceof GuildUpdateMaxPresencesEvent)
			onGuildUpdateMaxPresences((GuildUpdateMaxPresencesEvent) event);

			//Guild Member Events
		else if (event instanceof GuildMemberJoinEvent)
			onGuildMemberJoin((GuildMemberJoinEvent) event);
		else if (event instanceof GuildMemberLeaveEvent)
			onGuildMemberLeave((GuildMemberLeaveEvent) event);
		else if (event instanceof GuildMemberRoleAddEvent)
			onGuildMemberRoleAdd((GuildMemberRoleAddEvent) event);
		else if (event instanceof GuildMemberRoleRemoveEvent)
			onGuildMemberRoleRemove((GuildMemberRoleRemoveEvent) event);

			//Guild Member Update Events
		else if (event instanceof GuildMemberUpdateNicknameEvent)
			onGuildMemberUpdateNickname((GuildMemberUpdateNicknameEvent) event);
		else if (event instanceof GuildMemberUpdateBoostTimeEvent)
			onGuildMemberUpdateBoostTime((GuildMemberUpdateBoostTimeEvent) event);

			//Guild Voice Events
		else if (event instanceof GuildVoiceJoinEvent)
			onGuildVoiceJoin((GuildVoiceJoinEvent) event);
		else if (event instanceof GuildVoiceMoveEvent)
			onGuildVoiceMove((GuildVoiceMoveEvent) event);
		else if (event instanceof GuildVoiceLeaveEvent)
			onGuildVoiceLeave((GuildVoiceLeaveEvent) event);
		else if (event instanceof GuildVoiceMuteEvent)
			onGuildVoiceMute((GuildVoiceMuteEvent) event);
		else if (event instanceof GuildVoiceDeafenEvent)
			onGuildVoiceDeafen((GuildVoiceDeafenEvent) event);
		else if (event instanceof GuildVoiceGuildMuteEvent)
			onGuildVoiceGuildMute((GuildVoiceGuildMuteEvent) event);
		else if (event instanceof GuildVoiceGuildDeafenEvent)
			onGuildVoiceGuildDeafen((GuildVoiceGuildDeafenEvent) event);
		else if (event instanceof GuildVoiceSelfMuteEvent)
			onGuildVoiceSelfMute((GuildVoiceSelfMuteEvent) event);
		else if (event instanceof GuildVoiceSelfDeafenEvent)
			onGuildVoiceSelfDeafen((GuildVoiceSelfDeafenEvent) event);
		else if (event instanceof GuildVoiceSuppressEvent)
			onGuildVoiceSuppress((GuildVoiceSuppressEvent) event);

			//Role Events
		else if (event instanceof RoleCreateEvent)
			onRoleCreate((RoleCreateEvent) event);
		else if (event instanceof RoleDeleteEvent)
			onRoleDelete((RoleDeleteEvent) event);

			//Role Update Events
		else if (event instanceof RoleUpdateColorEvent)
			onRoleUpdateColor(((RoleUpdateColorEvent) event));
		else if (event instanceof RoleUpdateHoistedEvent)
			onRoleUpdateHoisted(((RoleUpdateHoistedEvent) event));
		else if (event instanceof RoleUpdateMentionableEvent)
			onRoleUpdateMentionable((RoleUpdateMentionableEvent) event);
		else if (event instanceof RoleUpdateNameEvent)
			onRoleUpdateName(((RoleUpdateNameEvent) event));
		else if (event instanceof RoleUpdatePermissionsEvent)
			onRoleUpdatePermissions(((RoleUpdatePermissionsEvent) event));
		else if (event instanceof RoleUpdatePositionEvent)
			onRoleUpdatePosition(((RoleUpdatePositionEvent) event));

			//Emote Events
		else if (event instanceof EmoteAddedEvent)
			onEmoteAdded((EmoteAddedEvent) event);
		else if (event instanceof EmoteRemovedEvent)
			onEmoteRemoved((EmoteRemovedEvent) event);

			//Emote Update Events
		else if (event instanceof EmoteUpdateNameEvent)
			onEmoteUpdateName((EmoteUpdateNameEvent) event);
		else if (event instanceof EmoteUpdateRolesEvent)
			onEmoteUpdateRoles((EmoteUpdateRolesEvent) event);

			// Debug Events
		else if (event instanceof HttpRequestEvent)
			onHttpRequest((HttpRequestEvent) event);

		//Generic subclasses - combining multiple events
		if (event instanceof GuildVoiceUpdateEvent)
			onGuildVoiceUpdate((GuildVoiceUpdateEvent) event);

		//Generic Events
		//Start a new if statement so that these are no overridden by the above events.
		if (event instanceof GenericMessageReactionEvent)
			onGenericMessageReaction((GenericMessageReactionEvent) event);
		else if (event instanceof GenericPrivateMessageReactionEvent)
			onGenericPrivateMessageReaction((GenericPrivateMessageReactionEvent) event);
		else if (event instanceof GenericStoreChannelUpdateEvent)
			onGenericStoreChannelUpdate((GenericStoreChannelUpdateEvent) event);
		else if (event instanceof GenericTextChannelUpdateEvent)
			onGenericTextChannelUpdate((GenericTextChannelUpdateEvent) event);
		else if (event instanceof GenericCategoryUpdateEvent)
			onGenericCategoryUpdate((GenericCategoryUpdateEvent) event);
		else if (event instanceof GenericGuildMessageReactionEvent)
			onGenericGuildMessageReaction((GenericGuildMessageReactionEvent) event);
		else if (event instanceof GenericVoiceChannelUpdateEvent)
			onGenericVoiceChannelUpdate((GenericVoiceChannelUpdateEvent) event);
		else if (event instanceof GenericGuildUpdateEvent)
			onGenericGuildUpdate((GenericGuildUpdateEvent) event);
		else if (event instanceof GenericGuildMemberUpdateEvent)
			onGenericGuildMemberUpdate((GenericGuildMemberUpdateEvent) event);
		else if (event instanceof GenericGuildVoiceEvent)
			onGenericGuildVoice((GenericGuildVoiceEvent) event);
		else if (event instanceof GenericRoleUpdateEvent)
			onGenericRoleUpdate(((GenericRoleUpdateEvent) event));
		else if (event instanceof GenericEmoteUpdateEvent)
			onGenericEmoteUpdate((GenericEmoteUpdateEvent) event);
		else if (event instanceof GenericUserPresenceEvent)
			onGenericUserPresence((GenericUserPresenceEvent) event);

		//Generic events that have generic subclasses (the subclasses as above).
		if (event instanceof GenericMessageEvent)
			onGenericMessage((GenericMessageEvent) event);
		else if (event instanceof GenericPrivateMessageEvent)
			onGenericPrivateMessage((GenericPrivateMessageEvent) event);
		else if (event instanceof GenericGuildMessageEvent)
			onGenericGuildMessage((GenericGuildMessageEvent) event);
		else if (event instanceof GenericGuildMemberEvent)
			onGenericGuildMember((GenericGuildMemberEvent) event);
		else if (event instanceof GenericUserEvent)
			onGenericUser((GenericUserEvent) event);
		else if (event instanceof GenericSelfUpdateEvent)
			onGenericSelfUpdate((GenericSelfUpdateEvent) event);
		else if (event instanceof GenericStoreChannelEvent)
			onGenericStoreChannel((GenericStoreChannelEvent) event);
		else if (event instanceof GenericTextChannelEvent)
			onGenericTextChannel((GenericTextChannelEvent) event);
		else if (event instanceof GenericVoiceChannelEvent)
			onGenericVoiceChannel((GenericVoiceChannelEvent) event);
		else if (event instanceof GenericCategoryEvent)
			onGenericCategory((GenericCategoryEvent) event);
		else if (event instanceof GenericRoleEvent)
			onGenericRole((GenericRoleEvent) event);
		else if (event instanceof GenericEmoteEvent)
			onGenericEmote((GenericEmoteEvent) event);

		//Generic events that have 2 levels of generic subclasses
		if (event instanceof GenericGuildEvent)
			onGenericGuild((GenericGuildEvent) event);
	}

}
