package org.openpanfu.gameserver.commands;

import org.openpanfu.gameserver.PanfuPacket;
import org.openpanfu.gameserver.User;
import org.openpanfu.gameserver.constants.Packets;

public class Action implements ICommand {
    @Override
    public void onExecution(User invoker, String[] parameters) {
        if(parameters.length > 0 && !parameters[0].equals("")) {
            String action = parameters[0];
            PanfuPacket actionPacket = new PanfuPacket(Packets.RES_DO_ACTION);
            actionPacket.writeInt(invoker.getUserId());
            actionPacket.writeString(action);
            invoker.sendRoom(actionPacket);
        }
    }

    @Override
    public String getDescription() {
        return "!action (action) - Does the specified action.";
    }
}