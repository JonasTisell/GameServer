package org.openpanfu.gameserver.commands;

import org.openpanfu.gameserver.GameServer;
import org.openpanfu.gameserver.PanfuPacket;
import org.openpanfu.gameserver.User;
import org.openpanfu.gameserver.constants.Packets;

public class Kick implements ICommand {
    @Override
    public void onExecution(User invoker, String[] parameters) {
        if(parameters.length > 0 && !parameters[0].equals("")) {
            String mentionedUser = parameters[0];
            try {
                User userToKick = GameServer.getUserByUsername(mentionedUser);
                if(userToKick.isLoggedIn()){
                    userToKick.disconnect();
                }else{
                    invoker.sendAlert("Kick failed because user is offline.");
                }
            }
            catch(Exception e) {
                invoker.sendAlert("Kicking failed because user is offline or doesn't exist.");
            }
        }
    }

    @Override
    public String getDescription() {
        return "!kick (user) - Kicks user.";
    }
}