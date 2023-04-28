/*
 *   Copyright 2023 Raphael "raffel080108" Roehrig. All rights reserved.
 *   Licensed under the Creative Commons Attribution-NonCommercial 4.0 International License.
 *   You may not use any contents of this file or any other file that is part of this software, except in compliance with the license.
 *   You can obtain a copy of the license at https://creativecommons.org/licenses/by-nc/4.0/legalcode
 */

package raffel080108.minecrafteras.data.config;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Villager;

import java.util.*;
import java.util.List;

public final class ConfigData {
    private static ConfigData configDataInstance;
    private final List<Era> eras = new ArrayList<>();
    private boolean blockMiningUnlockingEnabled;
    private boolean entityAttackingUnlockingEnabled;
    private boolean recipeCraftingUnlockingEnabled;
    private final Map<Material, DepositableItemConfig> itemDepositAmounts = new Hashtable<>();
    private boolean campMasterInvincible;
    private Component campMasterDisplayName;
    private int campMasterBehaviour;
    private Villager.Type campMasterType;
    private Villager.Profession campMasterProfession;
    private int maxActiveQuests;

    private ConfigData() {}

    public static ConfigData getInstance() {
        if (configDataInstance == null)
            configDataInstance = new ConfigData();

        return configDataInstance;
    }


    public List<Era> getEras() {
        return eras;
    }

    public boolean isBlockMiningUnlockingEnabled() {
        return blockMiningUnlockingEnabled;
    }

    public void setBlockMiningUnlockingEnabled(boolean blockMiningUnlockingEnabled) {
        this.blockMiningUnlockingEnabled = blockMiningUnlockingEnabled;
    }

    public boolean isEntityAttackingUnlockingEnabled() {
        return entityAttackingUnlockingEnabled;
    }

    public void setEntityAttackingUnlockingEnabled(boolean entityAttackingUnlockingEnabled) {
        this.entityAttackingUnlockingEnabled = entityAttackingUnlockingEnabled;
    }

    public boolean isRecipeCraftingUnlockingEnabled() {
        return recipeCraftingUnlockingEnabled;
    }

    public void setRecipeCraftingUnlockingEnabled(boolean recipeCraftingUnlockingEnabled) {
        this.recipeCraftingUnlockingEnabled = recipeCraftingUnlockingEnabled;
    }

    public Map<Material, DepositableItemConfig> getItemDepositAmounts() {
        return itemDepositAmounts;
    }

    public boolean isCampMasterInvincible() {
        return campMasterInvincible;
    }

    public void setCampMasterInvincible(boolean campMasterInvincible) {
        this.campMasterInvincible = campMasterInvincible;
    }

    public Component getCampMasterDisplayName() {
        return campMasterDisplayName;
    }

    public void setCampMasterDisplayName(Component campMasterDisplayName) {
        this.campMasterDisplayName = campMasterDisplayName;
    }

    public int getCampMasterBehaviour() {
        return campMasterBehaviour;
    }

    public void setCampMasterBehaviour(int campMasterBehaviour) {
        this.campMasterBehaviour = campMasterBehaviour;
    }

    public Villager.Type getCampMasterType() {
        return campMasterType;
    }

    public void setCampMasterType(Villager.Type campMasterType) {
        this.campMasterType = campMasterType;
    }

    public Villager.Profession getCampMasterProfession() {
        return campMasterProfession;
    }

    public void setCampMasterProfession(Villager.Profession campMasterProfession) {
        this.campMasterProfession = campMasterProfession;
    }

    public int getMaxActiveQuests() {
        return maxActiveQuests;
    }

    public void setMaxActiveQuests(int maxActiveQuests) {
        this.maxActiveQuests = maxActiveQuests;
    }
}
