/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use any contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
 */

package raffel080108.minecrafteras.data.quest.manager;

public enum QuestManagerType {
    ENTER_DIMENSION(false),
    ENTER_WORLD(false),
    GET_ADVANCMENTS(true),
    KILL_ENTITIES(true),
    KILL_SPECIFIC_ENTITIES(true),
    MINE_BLOCKS(true),
    MINE_SPECIFIC_BLOCKS(true),
    TRAVEL_BLOCKS(true);
    
    private final boolean trackable;
    
    QuestManagerType(boolean trackable) {
        this.trackable = trackable;
    }

    public boolean isTrackable() {
        return trackable;
    }
}
