/*
 ItemRestrict © 2023 by Raphael "raffel080108" Roehrig is licensed under CC BY-NC 4.0. To view a copy of this license, visit http://creativecommons.org/licenses/by-nc/4.0/
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
