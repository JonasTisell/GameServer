package org.openpanfu.gameserver.commands;

import org.openpanfu.gameserver.PanfuPacket;
import org.openpanfu.gameserver.User;
import org.openpanfu.gameserver.constants.Packets;

public class Mass implements ICommand {
    @Override
    public void onExecution(User invoker, String[] parameters) {
        if(parameters.length > 0 && !parameters[0].equals("")) {
            String action = parameters[0];
            for(User u : invoker.getGameServer().getSessionManager().getUsersInRoom(invoker.getRoomId(), invoker.isInHome(), invoker.getSubRoom())) {
                PanfuPacket massPacket = new PanfuPacket(Packets.RES_DO_ACTION);
                massPacket.writeInt(u.getUserId());
                massPacket.writeString(action);
                invoker.sendRoom(massPacket);
            }
        }
    }

    @Override
    public String getDescription() {
        return "!mass (action) - Forces all users in the room to do an action.";
    }
}