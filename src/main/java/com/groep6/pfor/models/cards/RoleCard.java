package com.groep6.pfor.models.cards;

import com.groep6.pfor.models.cards.actions.IAction;
import javafx.scene.paint.Color;

/**
 * @author Bastiaan Jansen
 */
public class RoleCard extends Card {

    private final String roleCardName;
    private final Color roleCardColor;
    private final IAction roleCardAbility;

    /**
     * @param roleCardName
     * @param roleCardAbility
     */
    public RoleCard(String roleCardName, Color roleCardColor, IAction roleCardAbility) {
        this.roleCardName = roleCardName;
        this.roleCardColor = roleCardColor;
        this.roleCardAbility = roleCardAbility;
    }

    /**
     * Executes an IAction event
     */
    public void executeRoleCardAbility() {
        roleCardAbility.executeCard();
    }

    @Override
    public String getCardName() {
        return roleCardName;
    }

    public Color getRoleCardColor() {
        return roleCardColor;
    }

    public IAction getRoleCardAbility() {
        return roleCardAbility;
    }
}
