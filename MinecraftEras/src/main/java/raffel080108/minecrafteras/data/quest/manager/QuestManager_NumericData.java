/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use any contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
 */

package raffel080108.minecrafteras.data.quest.manager;

public abstract class QuestManager_NumericData extends QuestManager implements QuestUpdateDisplayer {
    protected double data;

    protected QuestManager_NumericData(String managedQuestName) {
        super(managedQuestName);
    }

    @Override
    public void setData(Object data) {
        if (data == null) {
            this.data = 0;
            return;
        }

        try {
            this.data = ((Number) data).doubleValue();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Invalid data type: " + data.getClass().getName(), e);
        }
    }

    @Override
    public Double getData() {
        return data;
    }
}
