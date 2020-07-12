package joecord.seal.clapbot;

import net.dv8tion.jda.api.events.*;
import net.dv8tion.jda.api.events.channel.category.CategoryCreateEvent;
import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent;
import net.dv8tion.jda.api.events.channel.category.GenericCategoryEvent;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdateNameEvent;
import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdatePositionEvent;
import net.dv8tion.jda.api.events.channel.category.update.GenericCategoryUpdateEvent;
import net.dv8tion.jda.api.events.channel.priv.PrivateChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.priv.PrivateChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.store.GenericStoreChannelEvent;
import net.dv8tion.jda.api.events.channel.store.StoreChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.store.StoreChannelDeleteEvent;
import net.dv8tion.jda.api.events.channel.store.update.GenericStoreChannelUpdateEvent;
import net.dv8tion.jda.api.events.channel.store.update.StoreChannelUpdateNameEvent;
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
import net.dv8tion.jda.api.events.guild.invite.GenericGuildInviteEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.*;
import net.dv8tion.jda.api.events.guild.member.update.GenericGuildMemberUpdateEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateBoostTimeEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.guild.override.GenericPermissionOverrideEvent;
import net.dv8tion.jda.api.events.guild.override.PermissionOverrideCreateEvent;
import net.dv8tion.jda.api.events.guild.override.PermissionOverrideDeleteEvent;
import net.dv8tion.jda.api.events.guild.override.PermissionOverrideUpdateEvent;
import net.dv8tion.jda.api.events.guild.update.*;
import net.dv8tion.jda.api.events.guild.voice.*;
import net.dv8tion.jda.api.events.http.HttpRequestEvent;
import net.dv8tion.jda.api.events.message.*;
import net.dv8tion.jda.api.events.message.guild.*;
import net.dv8tion.jda.api.events.message.guild.react.*;
import net.dv8tion.jda.api.events.message.priv.*;
import net.dv8tion.jda.api.events.message.priv.react.GenericPrivateMessageReactionEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionRemoveEvent;
import net.dv8tion.jda.api.events.message.react.*;
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

/**
 * Clapbot's event listener. Calls the command handler's {@link
 * joecord.seal.clapbot.CommandHandler#onEvent CommandHandler#onEvent} method
 * whenever a {@link net.dv8tion.jda.api.events.GenericEvent JDA 
 * GenericEvent} is received.
 * 
 * The overriden {@code onEvent} method's code is directly copied from
 * {@link net.dv8tion.jda.api.hooks.ListenerAdapter#onEvent(GenericEvent)}
 * and ran through regex find and replace to hook into ClapBot's command
 * handler. If JDA's {@link
 * net.dv8tion.jda.api.hooks.ListenerAdapter#onEvent(GenericEvent)
 * ListenerAdapter#onEvent} is updated, copy the code back in, run find &
 * replace with the strings in this source code and go through each command
 * adding the message and isBot fields as necassary and removing depreceated
 * events.
 */
public class Listener implements EventListener {

    /* Find string */
    // (on[a-zA-Z]{5,40}\({1,3}([a-zA-Z]{5,40})\) event\){1,3};)

    /* Replace string */
    // handler.onEvent($2.class, (($2) event), null, false);

    private CommandHandler handler;

    public Listener(CommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public final void onEvent(GenericEvent event)
    {
        handler.onEvent(GenericEvent.class, event, null, false);

        if (event instanceof UpdateEvent)
            handler.onEvent(UpdateEvent.class, ((UpdateEvent<?, ?>) event), null, false);
        else if (event instanceof RawGatewayEvent)
            handler.onEvent(RawGatewayEvent.class, ((RawGatewayEvent) event), null, false);

        //JDA Events
        if (event instanceof ReadyEvent)
            handler.onEvent(ReadyEvent.class, ((ReadyEvent) event), null, false);
        else if (event instanceof ResumedEvent)
            handler.onEvent(ResumedEvent.class, ((ResumedEvent) event), null, false);
        else if (event instanceof ReconnectedEvent)
            handler.onEvent(ReconnectedEvent.class, ((ReconnectedEvent) event), null, false);
        else if (event instanceof DisconnectEvent)
            handler.onEvent(DisconnectEvent.class, ((DisconnectEvent) event), null, false);
        else if (event instanceof ShutdownEvent)
            handler.onEvent(ShutdownEvent.class, ((ShutdownEvent) event), null, false);
        else if (event instanceof StatusChangeEvent)
            handler.onEvent(StatusChangeEvent.class, ((StatusChangeEvent) event), null, false);
        else if (event instanceof ExceptionEvent)
            handler.onEvent(ExceptionEvent.class, ((ExceptionEvent) event), null, false);
        else if (event instanceof GatewayPingEvent)
            handler.onEvent(GatewayPingEvent.class, ((GatewayPingEvent) event), null, false);

        //Message Events
        //Guild (TextChannel) Message Events
        else if (event instanceof GuildMessageReceivedEvent)
            handler.onEvent(GuildMessageReceivedEvent.class, ((GuildMessageReceivedEvent) event), ((GuildMessageReceivedEvent) event).getMessage().getContentRaw(), ((GuildMessageReceivedEvent) event).getAuthor().isBot());
        else if (event instanceof GuildMessageUpdateEvent)
            handler.onEvent(GuildMessageUpdateEvent.class, ((GuildMessageUpdateEvent) event), ((GuildMessageUpdateEvent) event).getMessage().getContentRaw(), ((GuildMessageUpdateEvent) event).getAuthor().isBot());
        else if (event instanceof GuildMessageDeleteEvent)
            handler.onEvent(GuildMessageDeleteEvent.class, ((GuildMessageDeleteEvent) event), null, false);
        else if (event instanceof GuildMessageEmbedEvent)
            handler.onEvent(GuildMessageEmbedEvent.class, ((GuildMessageEmbedEvent) event), null, false);
        else if (event instanceof GuildMessageReactionAddEvent)
            handler.onEvent(GuildMessageReactionAddEvent.class, ((GuildMessageReactionAddEvent) event), null, ((GuildMessageReactionAddEvent) event).getUser().isBot());
        else if (event instanceof GuildMessageReactionRemoveEvent)
            handler.onEvent(GuildMessageReactionRemoveEvent.class, ((GuildMessageReactionRemoveEvent) event), null, ((GuildMessageReactionRemoveEvent) event).getUser().isBot());
        else if (event instanceof GuildMessageReactionRemoveAllEvent)
            handler.onEvent(GuildMessageReactionRemoveAllEvent.class, ((GuildMessageReactionRemoveAllEvent) event), null, false);
        else if (event instanceof GuildMessageReactionRemoveEmoteEvent)
            handler.onEvent(GuildMessageReactionRemoveEmoteEvent.class, ((GuildMessageReactionRemoveEmoteEvent) event), null, false);

        //Private Message Events
        else if (event instanceof PrivateMessageReceivedEvent)
            handler.onEvent(PrivateMessageReceivedEvent.class, ((PrivateMessageReceivedEvent) event), ((PrivateMessageReceivedEvent) event).getMessage().getContentRaw(), ((PrivateMessageReceivedEvent) event).getAuthor().isBot());
        else if (event instanceof PrivateMessageUpdateEvent)
            handler.onEvent(PrivateMessageUpdateEvent.class, ((PrivateMessageUpdateEvent) event), ((PrivateMessageUpdateEvent) event).getMessage().getContentDisplay(), ((PrivateMessageUpdateEvent) event).getAuthor().isBot());
        else if (event instanceof PrivateMessageDeleteEvent)
            handler.onEvent(PrivateMessageDeleteEvent.class, ((PrivateMessageDeleteEvent) event), null, false);
        else if (event instanceof PrivateMessageEmbedEvent)
            handler.onEvent(PrivateMessageEmbedEvent.class, ((PrivateMessageEmbedEvent) event), null, false);
        else if (event instanceof PrivateMessageReactionAddEvent)
            handler.onEvent(PrivateMessageReactionAddEvent.class, ((PrivateMessageReactionAddEvent) event), null, ((PrivateMessageReactionAddEvent) event).getUser().isBot());
        else if (event instanceof PrivateMessageReactionRemoveEvent)
            handler.onEvent(PrivateMessageReactionRemoveEvent.class, ((PrivateMessageReactionRemoveEvent) event), null, ((PrivateMessageReactionRemoveEvent) event).getUser().isBot());

        //Combined Message Events (Combines Guild and Private message into 1 event)
        else if (event instanceof MessageReceivedEvent)
            handler.onEvent(MessageReceivedEvent.class, ((MessageReceivedEvent) event), ((MessageReceivedEvent) event).getMessage().getContentRaw(), ((MessageReceivedEvent) event).getAuthor().isBot());
        else if (event instanceof MessageUpdateEvent)
            handler.onEvent(MessageUpdateEvent.class, ((MessageUpdateEvent) event), ((MessageUpdateEvent) event).getMessage().getContentRaw(), ((MessageUpdateEvent) event).getAuthor().isBot());
        else if (event instanceof MessageDeleteEvent)
            handler.onEvent(MessageDeleteEvent.class, ((MessageDeleteEvent) event), null, false);
        else if (event instanceof MessageBulkDeleteEvent)
            handler.onEvent(MessageBulkDeleteEvent.class, ((MessageBulkDeleteEvent) event), null, false);
        else if (event instanceof MessageEmbedEvent)
            handler.onEvent(MessageEmbedEvent.class, ((MessageEmbedEvent) event), null, false);
        else if (event instanceof MessageReactionAddEvent)
            handler.onEvent(MessageReactionAddEvent.class, ((MessageReactionAddEvent) event), null, ((MessageReactionAddEvent) event).getUser().isBot());
        else if (event instanceof MessageReactionRemoveEvent)
            handler.onEvent(MessageReactionRemoveEvent.class, ((MessageReactionRemoveEvent) event), null, ((MessageReactionRemoveEvent) event).getUser().isBot());
        else if (event instanceof MessageReactionRemoveAllEvent)
            handler.onEvent(MessageReactionRemoveAllEvent.class, ((MessageReactionRemoveAllEvent) event), null, false);
        else if (event instanceof MessageReactionRemoveEmoteEvent)
            handler.onEvent(MessageReactionRemoveEmoteEvent.class, ((MessageReactionRemoveEmoteEvent) event), null, false);

        //User Events
        else if (event instanceof UserUpdateNameEvent)
            handler.onEvent(UserUpdateNameEvent.class, ((UserUpdateNameEvent) event), null, ((UserUpdateNameEvent) event).getUser().isBot());
        else if (event instanceof UserUpdateDiscriminatorEvent)
            handler.onEvent(UserUpdateDiscriminatorEvent.class, ((UserUpdateDiscriminatorEvent) event), null, ((UserUpdateDiscriminatorEvent) event).getUser().isBot());
        else if (event instanceof UserUpdateAvatarEvent)
            handler.onEvent(UserUpdateAvatarEvent.class, ((UserUpdateAvatarEvent) event), null, ((UserUpdateAvatarEvent) event).getUser().isBot());
        else if (event instanceof UserUpdateActivityOrderEvent)
            handler.onEvent(UserUpdateActivityOrderEvent.class, ((UserUpdateActivityOrderEvent) event), null, ((UserUpdateActivityOrderEvent) event).getUser().isBot());
        else if (event instanceof UserUpdateOnlineStatusEvent)
            handler.onEvent(UserUpdateOnlineStatusEvent.class, ((UserUpdateOnlineStatusEvent) event), null, ((UserUpdateOnlineStatusEvent) event).getUser().isBot());
        else if (event instanceof UserTypingEvent)
            handler.onEvent(UserTypingEvent.class, ((UserTypingEvent) event), null, ((UserTypingEvent) event).getUser().isBot());
        else if (event instanceof UserActivityStartEvent)
            handler.onEvent(UserActivityStartEvent.class, ((UserActivityStartEvent) event), null, ((UserActivityStartEvent) event).getUser().isBot());
        else if (event instanceof UserActivityEndEvent)
            handler.onEvent(UserActivityEndEvent.class, ((UserActivityEndEvent) event), null, ((UserActivityEndEvent) event).getUser().isBot());
        else if (event instanceof UserUpdateFlagsEvent)
            handler.onEvent(UserUpdateFlagsEvent.class, ((UserUpdateFlagsEvent) event), null, ((UserUpdateFlagsEvent) event).getUser().isBot());

        //Self Events
        else if (event instanceof SelfUpdateAvatarEvent)
            handler.onEvent(SelfUpdateAvatarEvent.class, ((SelfUpdateAvatarEvent) event), null, ((SelfUpdateAvatarEvent) event).getSelfUser().isBot());
        else if (event instanceof SelfUpdateMFAEvent)
            handler.onEvent(SelfUpdateMFAEvent.class, ((SelfUpdateMFAEvent) event), null, ((SelfUpdateMFAEvent) event).getSelfUser().isBot());
        else if (event instanceof SelfUpdateNameEvent)
            handler.onEvent(SelfUpdateNameEvent.class, ((SelfUpdateNameEvent) event), null, ((SelfUpdateNameEvent) event).getSelfUser().isBot());
        else if (event instanceof SelfUpdateVerifiedEvent)
            handler.onEvent(SelfUpdateVerifiedEvent.class, ((SelfUpdateVerifiedEvent) event), null, ((SelfUpdateVerifiedEvent) event).getSelfUser().isBot());

        //PermissionOverride Events
        else if (event instanceof PermissionOverrideDeleteEvent)
            handler.onEvent(PermissionOverrideDeleteEvent.class, ((PermissionOverrideDeleteEvent) event), null, false);
        else if (event instanceof PermissionOverrideUpdateEvent)
            handler.onEvent(PermissionOverrideUpdateEvent.class, ((PermissionOverrideUpdateEvent) event), null, false);
        else if (event instanceof PermissionOverrideCreateEvent)
            handler.onEvent(PermissionOverrideCreateEvent.class, ((PermissionOverrideCreateEvent) event), null, false);

        //StoreChannel Events
        else if (event instanceof StoreChannelCreateEvent)
            handler.onEvent(StoreChannelCreateEvent.class, ((StoreChannelCreateEvent) event), null, false);
        else if (event instanceof StoreChannelDeleteEvent)
            handler.onEvent(StoreChannelDeleteEvent.class, ((StoreChannelDeleteEvent) event), null, false);
        else if (event instanceof StoreChannelUpdateNameEvent)
            handler.onEvent(StoreChannelUpdateNameEvent.class, ((StoreChannelUpdateNameEvent) event), null, false);
        else if (event instanceof StoreChannelUpdatePositionEvent)
            handler.onEvent(StoreChannelUpdatePositionEvent.class, ((StoreChannelUpdatePositionEvent) event), null, false);
        /* Depreciated
        else if (event instanceof StoreChannelUpdatePermissionsEvent)
            handler.onEvent(StoreChannelUpdatePermissionsEvent.class, ((StoreChannelUpdatePermissionsEvent) event), null, false); */

        //TextChannel Events
        else if (event instanceof TextChannelCreateEvent)
            handler.onEvent(TextChannelCreateEvent.class, ((TextChannelCreateEvent) event), null, false);
        else if (event instanceof TextChannelUpdateNameEvent)
            handler.onEvent(TextChannelUpdateNameEvent.class, ((TextChannelUpdateNameEvent) event), null, false);
        else if (event instanceof TextChannelUpdateTopicEvent)
            handler.onEvent(TextChannelUpdateTopicEvent.class, ((TextChannelUpdateTopicEvent) event), null, false);
        else if (event instanceof TextChannelUpdatePositionEvent)
            handler.onEvent(TextChannelUpdatePositionEvent.class, ((TextChannelUpdatePositionEvent) event), null, false);
        else if (event instanceof TextChannelUpdateNSFWEvent)
            handler.onEvent(TextChannelUpdateNSFWEvent.class, ((TextChannelUpdateNSFWEvent) event), null, false);
        else if (event instanceof TextChannelUpdateParentEvent)
            handler.onEvent(TextChannelUpdateParentEvent.class, ((TextChannelUpdateParentEvent) event), null, false);
        else if (event instanceof TextChannelUpdateSlowmodeEvent)
            handler.onEvent(TextChannelUpdateSlowmodeEvent.class, ((TextChannelUpdateSlowmodeEvent) event), null, false);
        else if (event instanceof TextChannelDeleteEvent)
            handler.onEvent(TextChannelDeleteEvent.class, ((TextChannelDeleteEvent) event), null, false);
        /* Depreceated
        else if (event instanceof TextChannelUpdatePermissionsEvent)
            handler.onEvent(TextChannelUpdatePermissionsEvent.class, ((TextChannelUpdatePermissionsEvent) event), null, false); */

        //VoiceChannel Events
        else if (event instanceof VoiceChannelCreateEvent)
            handler.onEvent(VoiceChannelCreateEvent.class, ((VoiceChannelCreateEvent) event), null, false);
        else if (event instanceof VoiceChannelUpdateNameEvent)
            handler.onEvent(VoiceChannelUpdateNameEvent.class, ((VoiceChannelUpdateNameEvent) event), null, false);
        else if (event instanceof VoiceChannelUpdatePositionEvent)
            handler.onEvent(VoiceChannelUpdatePositionEvent.class, ((VoiceChannelUpdatePositionEvent) event), null, false);
        else if (event instanceof VoiceChannelUpdateUserLimitEvent)
            handler.onEvent(VoiceChannelUpdateUserLimitEvent.class, ((VoiceChannelUpdateUserLimitEvent) event), null, false);
        else if (event instanceof VoiceChannelUpdateBitrateEvent)
            handler.onEvent(VoiceChannelUpdateBitrateEvent.class, ((VoiceChannelUpdateBitrateEvent) event), null, false);
        else if (event instanceof VoiceChannelUpdateParentEvent)
            handler.onEvent(VoiceChannelUpdateParentEvent.class, ((VoiceChannelUpdateParentEvent) event), null, false);
        else if (event instanceof VoiceChannelDeleteEvent)
            handler.onEvent(VoiceChannelDeleteEvent.class, ((VoiceChannelDeleteEvent) event), null, false);
        /* Depreceated
        else if (event instanceof VoiceChannelUpdatePermissionsEvent)
            handler.onEvent(VoiceChannelUpdatePermissionsEvent.class, ((VoiceChannelUpdatePermissionsEvent) event), null, false); */

        //Category Events
        else if (event instanceof CategoryCreateEvent)
            handler.onEvent(CategoryCreateEvent.class, ((CategoryCreateEvent) event), null, false);
        else if (event instanceof CategoryUpdateNameEvent)
            handler.onEvent(CategoryUpdateNameEvent.class, ((CategoryUpdateNameEvent) event), null, false);
        else if (event instanceof CategoryUpdatePositionEvent)
            handler.onEvent(CategoryUpdatePositionEvent.class, ((CategoryUpdatePositionEvent) event), null, false);
        else if (event instanceof CategoryDeleteEvent)
            handler.onEvent(CategoryDeleteEvent.class, ((CategoryDeleteEvent) event), null, false);
        /* Depreceated
        else if (event instanceof CategoryUpdatePermissionsEvent)
            handler.onEvent(CategoryUpdatePermissionsEvent.class, ((CategoryUpdatePermissionsEvent) event), null, false); */

        //PrivateChannel Events
        else if (event instanceof PrivateChannelCreateEvent)
            handler.onEvent(PrivateChannelCreateEvent.class, ((PrivateChannelCreateEvent) event), null, false);
        else if (event instanceof PrivateChannelDeleteEvent)
            handler.onEvent(PrivateChannelDeleteEvent.class, ((PrivateChannelDeleteEvent) event), null, false);

        //Guild Events
        else if (event instanceof GuildReadyEvent)
            handler.onEvent(GuildReadyEvent.class, ((GuildReadyEvent) event), null, false);
        else if (event instanceof GuildJoinEvent)
            handler.onEvent(GuildJoinEvent.class, ((GuildJoinEvent) event), null, ((GuildJoinEvent) event).getJDA().getSelfUser().isBot());
        else if (event instanceof GuildLeaveEvent)
            handler.onEvent(GuildLeaveEvent.class, ((GuildLeaveEvent) event), null, ((GuildLeaveEvent) event).getJDA().getSelfUser().isBot());
        else if (event instanceof GuildAvailableEvent)
            handler.onEvent(GuildAvailableEvent.class, ((GuildAvailableEvent) event), null, ((GuildAvailableEvent) event).getJDA().getSelfUser().isBot());
        else if (event instanceof GuildUnavailableEvent)
            handler.onEvent(GuildUnavailableEvent.class, ((GuildUnavailableEvent) event), null, ((GuildUnavailableEvent) event).getJDA().getSelfUser().isBot());
        else if (event instanceof UnavailableGuildJoinedEvent)
            handler.onEvent(UnavailableGuildJoinedEvent.class, ((UnavailableGuildJoinedEvent) event), null, ((UnavailableGuildJoinedEvent) event).getJDA().getSelfUser().isBot());
        else if (event instanceof UnavailableGuildLeaveEvent)
            handler.onEvent(UnavailableGuildLeaveEvent.class, ((UnavailableGuildLeaveEvent) event), null, ((UnavailableGuildLeaveEvent) event).getJDA().getSelfUser().isBot());
        else if (event instanceof GuildBanEvent)
            handler.onEvent(GuildBanEvent.class, ((GuildBanEvent) event), null, ((GuildBanEvent) event).getUser().isBot());
        else if (event instanceof GuildUnbanEvent)
            handler.onEvent(GuildUnbanEvent.class, ((GuildUnbanEvent) event), null, ((GuildUnbanEvent) event).getUser().isBot());
        else if (event instanceof GuildMemberRemoveEvent)
            handler.onEvent(GuildMemberRemoveEvent.class, ((GuildMemberRemoveEvent) event), null, ((GuildMemberRemoveEvent) event).getUser().isBot());

        //Guild Update Events
        else if (event instanceof GuildUpdateAfkChannelEvent)
            handler.onEvent(GuildUpdateAfkChannelEvent.class, ((GuildUpdateAfkChannelEvent) event), null, false);
        else if (event instanceof GuildUpdateSystemChannelEvent)
            handler.onEvent(GuildUpdateSystemChannelEvent.class, ((GuildUpdateSystemChannelEvent) event), null, false);
        else if (event instanceof GuildUpdateAfkTimeoutEvent)
            handler.onEvent(GuildUpdateAfkTimeoutEvent.class, ((GuildUpdateAfkTimeoutEvent) event), null, false);
        else if (event instanceof GuildUpdateExplicitContentLevelEvent)
            handler.onEvent(GuildUpdateExplicitContentLevelEvent.class, ((GuildUpdateExplicitContentLevelEvent) event), null, false);
        else if (event instanceof GuildUpdateIconEvent)
            handler.onEvent(GuildUpdateIconEvent.class, ((GuildUpdateIconEvent) event), null, false);
        else if (event instanceof GuildUpdateMFALevelEvent)
            handler.onEvent(GuildUpdateMFALevelEvent.class, ((GuildUpdateMFALevelEvent) event), null, false);
        else if (event instanceof GuildUpdateNameEvent)
            handler.onEvent(GuildUpdateNameEvent.class, ((GuildUpdateNameEvent) event), null, false);
        else if (event instanceof GuildUpdateNotificationLevelEvent)
            handler.onEvent(GuildUpdateNotificationLevelEvent.class, ((GuildUpdateNotificationLevelEvent) event), null, false);
        else if (event instanceof GuildUpdateOwnerEvent)
            handler.onEvent(GuildUpdateOwnerEvent.class, ((GuildUpdateOwnerEvent) event), null, false);
        else if (event instanceof GuildUpdateRegionEvent)
            handler.onEvent(GuildUpdateRegionEvent.class, ((GuildUpdateRegionEvent) event), null, false);
        else if (event instanceof GuildUpdateSplashEvent)
            handler.onEvent(GuildUpdateSplashEvent.class, ((GuildUpdateSplashEvent) event), null, false);
        else if (event instanceof GuildUpdateVerificationLevelEvent)
            handler.onEvent(GuildUpdateVerificationLevelEvent.class, ((GuildUpdateVerificationLevelEvent) event), null, false);
        else if (event instanceof GuildUpdateFeaturesEvent)
            handler.onEvent(GuildUpdateFeaturesEvent.class, ((GuildUpdateFeaturesEvent) event), null, false);
        else if (event instanceof GuildUpdateVanityCodeEvent)
            handler.onEvent(GuildUpdateVanityCodeEvent.class, ((GuildUpdateVanityCodeEvent) event), null, false);
        else if (event instanceof GuildUpdateBannerEvent)
            handler.onEvent(GuildUpdateBannerEvent.class, ((GuildUpdateBannerEvent) event), null, false);
        else if (event instanceof GuildUpdateDescriptionEvent)
            handler.onEvent(GuildUpdateDescriptionEvent.class, ((GuildUpdateDescriptionEvent) event), null, false);
        else if (event instanceof GuildUpdateBoostTierEvent)
            handler.onEvent(GuildUpdateBoostTierEvent.class, ((GuildUpdateBoostTierEvent) event), null, false);
        else if (event instanceof GuildUpdateBoostCountEvent)
            handler.onEvent(GuildUpdateBoostCountEvent.class, ((GuildUpdateBoostCountEvent) event), null, false);
        else if (event instanceof GuildUpdateMaxMembersEvent)
            handler.onEvent(GuildUpdateMaxMembersEvent.class, ((GuildUpdateMaxMembersEvent) event), null, false);
        else if (event instanceof GuildUpdateMaxPresencesEvent)
            handler.onEvent(GuildUpdateMaxPresencesEvent.class, ((GuildUpdateMaxPresencesEvent) event), null, false);

        //Guild Invite Events
        else if (event instanceof GuildInviteCreateEvent)
            handler.onEvent(GuildInviteCreateEvent.class, ((GuildInviteCreateEvent) event), null, ((GuildInviteCreateEvent) event).getInvite().getInviter().isBot());
        else if (event instanceof GuildInviteDeleteEvent)
            handler.onEvent(GuildInviteDeleteEvent.class, ((GuildInviteDeleteEvent) event), null, false);

        //Guild Member Events
        else if (event instanceof GuildMemberJoinEvent)
            handler.onEvent(GuildMemberJoinEvent.class, ((GuildMemberJoinEvent) event), null, ((GuildMemberJoinEvent) event).getUser().isBot());
        /* Depreceated
        else if (event instanceof GuildMemberLeaveEvent)
            handler.onEvent(GuildMemberLeaveEvent.class, ((GuildMemberLeaveEvent) event), null, false); */
        else if (event instanceof GuildMemberRoleAddEvent)
            handler.onEvent(GuildMemberRoleAddEvent.class, ((GuildMemberRoleAddEvent) event), null, ((GuildMemberRoleAddEvent) event).getUser().isBot());
        else if (event instanceof GuildMemberRoleRemoveEvent)
            handler.onEvent(GuildMemberRoleRemoveEvent.class, ((GuildMemberRoleRemoveEvent) event), null, ((GuildMemberRoleRemoveEvent) event).getUser().isBot());

        //Guild Member Update Events
        else if (event instanceof GuildMemberUpdateNicknameEvent)
            handler.onEvent(GuildMemberUpdateNicknameEvent.class, ((GuildMemberUpdateNicknameEvent) event), null, ((GuildMemberUpdateNicknameEvent) event).getUser().isBot());
        else if (event instanceof GuildMemberUpdateBoostTimeEvent)
            handler.onEvent(GuildMemberUpdateBoostTimeEvent.class, ((GuildMemberUpdateBoostTimeEvent) event), null, ((GuildMemberUpdateBoostTimeEvent) event).getUser().isBot());

        //Guild Voice Events
        else if (event instanceof GuildVoiceJoinEvent)
            handler.onEvent(GuildVoiceJoinEvent.class, ((GuildVoiceJoinEvent) event), null, ((GuildVoiceJoinEvent) event).getMember().getUser().isBot());
        else if (event instanceof GuildVoiceMoveEvent)
            handler.onEvent(GuildVoiceMoveEvent.class, ((GuildVoiceMoveEvent) event), null, ((GuildVoiceMoveEvent) event).getMember().getUser().isBot());
        else if (event instanceof GuildVoiceLeaveEvent)
            handler.onEvent(GuildVoiceLeaveEvent.class, ((GuildVoiceLeaveEvent) event), null, ((GuildVoiceLeaveEvent) event).getMember().getUser().isBot());
        else if (event instanceof GuildVoiceMuteEvent)
            handler.onEvent(GuildVoiceMuteEvent.class, ((GuildVoiceMuteEvent) event), null, ((GuildVoiceMuteEvent) event).getMember().getUser().isBot());
        else if (event instanceof GuildVoiceDeafenEvent)
            handler.onEvent(GuildVoiceDeafenEvent.class, ((GuildVoiceDeafenEvent) event), null, ((GuildVoiceDeafenEvent) event).getMember().getUser().isBot());
        else if (event instanceof GuildVoiceGuildMuteEvent)
            handler.onEvent(GuildVoiceGuildMuteEvent.class, ((GuildVoiceGuildMuteEvent) event), null, ((GuildVoiceGuildMuteEvent) event).getMember().getUser().isBot());
        else if (event instanceof GuildVoiceGuildDeafenEvent)
            handler.onEvent(GuildVoiceGuildDeafenEvent.class, ((GuildVoiceGuildDeafenEvent) event), null, ((GuildVoiceGuildDeafenEvent) event).getMember().getUser().isBot());
        else if (event instanceof GuildVoiceSelfMuteEvent)
            handler.onEvent(GuildVoiceSelfMuteEvent.class, ((GuildVoiceSelfMuteEvent) event), null, ((GuildVoiceSelfMuteEvent) event).getMember().getUser().isBot());
        else if (event instanceof GuildVoiceSelfDeafenEvent)
            handler.onEvent(GuildVoiceSelfDeafenEvent.class, ((GuildVoiceSelfDeafenEvent) event), null, ((GuildVoiceSelfDeafenEvent) event).getMember().getUser().isBot());
        else if (event instanceof GuildVoiceSuppressEvent)
            handler.onEvent(GuildVoiceSuppressEvent.class, ((GuildVoiceSuppressEvent) event), null, ((GuildVoiceSuppressEvent) event).getMember().getUser().isBot());
        else if (event instanceof GuildVoiceStreamEvent)
            handler.onEvent(GuildVoiceStreamEvent.class, ((GuildVoiceStreamEvent) event), null, ((GuildVoiceStreamEvent) event).getMember().getUser().isBot());

        //Role Events
        else if (event instanceof RoleCreateEvent)
            handler.onEvent(RoleCreateEvent.class, ((RoleCreateEvent) event), null, false);
        else if (event instanceof RoleDeleteEvent)
            handler.onEvent(RoleDeleteEvent.class, ((RoleDeleteEvent) event), null, false);

        //Role Update Events
        else if (event instanceof RoleUpdateColorEvent)
            handler.onEvent(RoleUpdateColorEvent.class, ((RoleUpdateColorEvent) event), null, false);
        else if (event instanceof RoleUpdateHoistedEvent)
            handler.onEvent(RoleUpdateHoistedEvent.class, ((RoleUpdateHoistedEvent) event), null, false);
        else if (event instanceof RoleUpdateMentionableEvent)
            handler.onEvent(RoleUpdateMentionableEvent.class, ((RoleUpdateMentionableEvent) event), null, false);
        else if (event instanceof RoleUpdateNameEvent)
            handler.onEvent(RoleUpdateNameEvent.class, ((RoleUpdateNameEvent) event), null, false);
        else if (event instanceof RoleUpdatePermissionsEvent)
            handler.onEvent(RoleUpdatePermissionsEvent.class, ((RoleUpdatePermissionsEvent) event), null, false);
        else if (event instanceof RoleUpdatePositionEvent)
            handler.onEvent(RoleUpdatePositionEvent.class, ((RoleUpdatePositionEvent) event), null, false);

        //Emote Events
        else if (event instanceof EmoteAddedEvent)
            handler.onEvent(EmoteAddedEvent.class, ((EmoteAddedEvent) event), null, false);
        else if (event instanceof EmoteRemovedEvent)
            handler.onEvent(EmoteRemovedEvent.class, ((EmoteRemovedEvent) event), null, false);

        //Emote Update Events
        else if (event instanceof EmoteUpdateNameEvent)
            handler.onEvent(EmoteUpdateNameEvent.class, ((EmoteUpdateNameEvent) event), null, false);
        else if (event instanceof EmoteUpdateRolesEvent)
            handler.onEvent(EmoteUpdateRolesEvent.class, ((EmoteUpdateRolesEvent) event), null, false);

        // Debug Events
        else if (event instanceof HttpRequestEvent)
            handler.onEvent(HttpRequestEvent.class, ((HttpRequestEvent) event), null, false);

        //Generic subclasses - combining multiple events
        if (event instanceof GuildVoiceUpdateEvent)
            handler.onEvent(GuildVoiceUpdateEvent.class, ((GuildVoiceUpdateEvent) event), null, false);

        //Generic Events
        //Start a new if statement so that these are no overridden by the above events.
        if (event instanceof GenericMessageReactionEvent)
            handler.onEvent(GenericMessageReactionEvent.class, ((GenericMessageReactionEvent) event), null, ((GenericMessageReactionEvent) event).getUser().isBot());
        else if (event instanceof GenericPrivateMessageReactionEvent)
            handler.onEvent(GenericPrivateMessageReactionEvent.class, ((GenericPrivateMessageReactionEvent) event), null, ((GenericPrivateMessageReactionEvent) event).getUser().isBot());
        else if (event instanceof GenericStoreChannelUpdateEvent)
            handler.onEvent(GenericStoreChannelUpdateEvent.class, ((GenericStoreChannelUpdateEvent<?>) event), null, false);
        else if (event instanceof GenericTextChannelUpdateEvent)
            handler.onEvent(GenericTextChannelUpdateEvent.class, ((GenericTextChannelUpdateEvent<?>) event), null, false);
        else if (event instanceof GenericCategoryUpdateEvent)
            handler.onEvent(GenericCategoryUpdateEvent.class, ((GenericCategoryUpdateEvent<?>) event), null, false);
        else if (event instanceof GenericGuildMessageReactionEvent)
            handler.onEvent(GenericGuildMessageReactionEvent.class, ((GenericGuildMessageReactionEvent) event), null, ((GenericGuildMessageReactionEvent) event).getUser().isBot());
        else if (event instanceof GenericVoiceChannelUpdateEvent)
            handler.onEvent(GenericVoiceChannelUpdateEvent.class, ((GenericVoiceChannelUpdateEvent<?>) event), null, false);
        else if (event instanceof GenericGuildUpdateEvent)
            handler.onEvent(GenericGuildUpdateEvent.class, ((GenericGuildUpdateEvent<?>) event), null, false);
        else if (event instanceof GenericGuildMemberUpdateEvent)
            handler.onEvent(GenericGuildMemberUpdateEvent.class, ((GenericGuildMemberUpdateEvent<?>) event), null, ((GenericGuildMemberUpdateEvent<?>) event).getUser().isBot());
        else if (event instanceof GenericGuildVoiceEvent)
            handler.onEvent(GenericGuildVoiceEvent.class, ((GenericGuildVoiceEvent) event), null, false);
        else if (event instanceof GenericRoleUpdateEvent)
            handler.onEvent(GenericRoleUpdateEvent.class, ((GenericRoleUpdateEvent<?>) event), null, false);
        else if (event instanceof GenericEmoteUpdateEvent)
            handler.onEvent(GenericEmoteUpdateEvent.class, ((GenericEmoteUpdateEvent<?>) event), null, false);
        else if (event instanceof GenericUserPresenceEvent)
            handler.onEvent(GenericUserPresenceEvent.class, ((GenericUserPresenceEvent) event), null, ((GenericUserPresenceEvent) event).getMember().getUser().isBot());
        else if (event instanceof GenericPermissionOverrideEvent)
            handler.onEvent(GenericPermissionOverrideEvent.class, ((GenericPermissionOverrideEvent) event), null, false);

        //Generic events that have generic subclasses (the subclasses as above).
        if (event instanceof GenericMessageEvent)
            handler.onEvent(GenericMessageEvent.class, ((GenericMessageEvent) event), null, false);
        else if (event instanceof GenericPrivateMessageEvent)
            handler.onEvent(GenericPrivateMessageEvent.class, ((GenericPrivateMessageEvent) event), null, false);
        else if (event instanceof GenericGuildMessageEvent)
            handler.onEvent(GenericGuildMessageEvent.class, ((GenericGuildMessageEvent) event), null, false);
        else if (event instanceof GenericGuildInviteEvent)
            handler.onEvent(GenericGuildInviteEvent.class, ((GenericGuildInviteEvent) event), null, false);
        else if (event instanceof GenericGuildMemberEvent)
            handler.onEvent(GenericGuildMemberEvent.class, ((GenericGuildMemberEvent) event), null, false);
        else if (event instanceof GenericUserEvent)
            handler.onEvent(GenericUserEvent.class, ((GenericUserEvent) event), null, ((GenericUserEvent) event).getUser().isBot());
        else if (event instanceof GenericSelfUpdateEvent)
            handler.onEvent(GenericSelfUpdateEvent.class, ((GenericSelfUpdateEvent<?>) event), null, ((GenericSelfUpdateEvent<?>) event).getSelfUser().isBot());
        else if (event instanceof GenericStoreChannelEvent)
            handler.onEvent(GenericStoreChannelEvent.class, ((GenericStoreChannelEvent) event), null, false);
        else if (event instanceof GenericTextChannelEvent)
            handler.onEvent(GenericTextChannelEvent.class, ((GenericTextChannelEvent) event), null, false);
        else if (event instanceof GenericVoiceChannelEvent)
            handler.onEvent(GenericVoiceChannelEvent.class, ((GenericVoiceChannelEvent) event), null, false);
        else if (event instanceof GenericCategoryEvent)
            handler.onEvent(GenericCategoryEvent.class, ((GenericCategoryEvent) event), null, false);
        else if (event instanceof GenericRoleEvent)
            handler.onEvent(GenericRoleEvent.class, ((GenericRoleEvent) event), null, false);
        else if (event instanceof GenericEmoteEvent)
            handler.onEvent(GenericEmoteEvent.class, ((GenericEmoteEvent) event), null, false);

        //Generic events that have 2 levels of generic subclasses
        if (event instanceof GenericGuildEvent)
            handler.onEvent(GenericGuildEvent.class, ((GenericGuildEvent) event), null, false);
    }

}