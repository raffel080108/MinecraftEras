/*
 MinecraftEras © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
 */

package raffel080108.minecrafteras.data.message;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.Hashtable;
import java.util.Map;
import java.util.Map;

public final class MessagesData {
    private static MessagesData messagesDataInstance;
    private final Map<Message, String> messages = new Hashtable<>();

    private MessagesData() {}

    public static MessagesData getInstance() {
        if (messagesDataInstance == null)
            messagesDataInstance = new MessagesData();
        
        return messagesDataInstance;
    }

    public Map<Message, String> getMessages() {
        return messages;
    }

    public Component getMessage(Message message) {
        return MiniMessage.miniMessage().deserialize(messages.get(message));
    }

    public Component getMessage(Message message, TagResolver... tagResolvers) {
        return MiniMessage.miniMessage().deserialize(messages.get(message), tagResolvers);
    }
}
