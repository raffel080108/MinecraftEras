/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use the contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
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
