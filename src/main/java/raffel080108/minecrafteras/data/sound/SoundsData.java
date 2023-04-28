/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use the contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
 */

package raffel080108.minecrafteras.data.sound;

import net.kyori.adventure.sound.Sound;

import java.util.Hashtable;
import java.util.Map;
import java.util.Map;

public final class SoundsData {
    private static SoundsData soundsDataInstance;
    private final Map<SoundKey, Sound> sounds = new Hashtable<>();

    private SoundsData() {}

    public static SoundsData getInstance() {
        if (soundsDataInstance == null)
            soundsDataInstance = new SoundsData();

        return soundsDataInstance;
    }

    public Map<SoundKey, Sound> getSounds() {
        return sounds;
    }

    public Sound getSound(SoundKey key) {
        return sounds.get(key);
    }
}
