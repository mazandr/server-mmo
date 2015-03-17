package com.win.strategy.battle.configuration.factory;

import com.win.strategy.battle.model.entity.BMField;
import com.win.strategy.battle.model.entity.basic.actions.BMAbilitylType;
import com.win.strategy.battle.model.entity.basic.actions.BMActionProperties;
import com.win.strategy.battle.model.entity.basic.resources.BMResource;
import com.win.strategy.battle.model.entity.basic.skills.BMAbilitySkill;
import com.win.strategy.battle.model.entity.basic.skills.BMBaseSkill;
import com.win.strategy.battle.model.entity.basic.skills.BMCommandSkill;
import com.win.strategy.common.utils.ProtocolStrings;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 *
 * @author okopach
 */
public class BMSkillInstanceFactory extends BMBaseInstanceFactory {

    /**
     * Create a skill
     *
     * @param skillIdentifier
     * @param skillTypeLvl
     * @return skill
     *
     * <b>Template</b>: "skills": { "skill_id": { "lvls": { "lvl1": { "cost":
     * {"energy":200}, "clDwn": 10, "actions": [ { "skT": "damage", "acT": { "type":
     * "instant", "delay": 0, "time": 10000, "tick": 2000 }, "apT": "area",
     * "value": 10 } ] } } } }
     *
     */
    public BMBaseSkill createAbilitySkill(String skillIdentifier, Map<String, Object> skillTypeLvl) {
        Collection<BMResource> cost = new HashSet<>();
        int cooldown;
        try {
            Map<String, Object> costMap = (Map<String, Object>) skillTypeLvl.get(ProtocolStrings.COST);
            if (costMap != null && !costMap.isEmpty()) {
                for (Map.Entry costEntry : costMap.entrySet()) {
                    cost.add(new BMResource(String.valueOf(costEntry.getKey()), Double.valueOf(String.valueOf(costEntry.getValue())).intValue()));
                }
            }
            cooldown = Double.valueOf(String.valueOf(skillTypeLvl.get(ProtocolStrings.CL_DWN))).intValue();
        } catch (Exception e) {
            cost = new HashSet<>();
            cooldown = 0;
        }
        List<Map<String, Object>> actionList;
        try {
            actionList = (List<Map<String, Object>>) skillTypeLvl.get(ProtocolStrings.ACTIONS);
        } catch (Exception e) {
            return null;
        }
        BMBaseSkill skill;
        if (isCommand(actionList)) {
            skill = new BMCommandSkill(getField());

            int radius = 18;
            int expTime = 5000;
            try {
                for (Map<String, Object> item : actionList) {
                    radius = Double.valueOf(String.valueOf(item.get(ProtocolStrings.SDR))).intValue();
                    Map<String, Object> acT = (Map<String, Object>) item.get(ProtocolStrings.AC_T);
                    expTime = Double.valueOf(String.valueOf(acT.get(ProtocolStrings.TIME))).intValue();
                }
            } catch (Exception e) {
                radius = 18;
                expTime = 5000;
            }
            BMCommandSkill cmdSkill = (BMCommandSkill) skill;
            cmdSkill.setRadius(radius);
            cmdSkill.setExpireTime(expTime);
        } else {
            skill = new BMAbilitySkill(getField());
            BMAbilitySkill abilityStill = (BMAbilitySkill) skill;

            for (Map<String, Object> item : actionList) {
                BMActionProperties skillItem = createActionProperty(item);
                skillItem.setFraction(BMField.ALLY_FRACTION_IDX);
                abilityStill.addItem(skillItem);
            }
        }
        
        skill.setIdentifier(skillIdentifier);
        skill.setCost(cost);
        skill.setCooldown(cooldown);

        return skill;
    }

    private boolean isCommand(List<Map<String, Object>> actionList) {
        try {
            for (Map<String, Object> item : actionList) {
                BMAbilitylType abilitylType = BMAbilitylType.valueOf(String.valueOf(item.get(ProtocolStrings.AB_T)));
                if (BMAbilitylType.command_move.equals(abilitylType)) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
