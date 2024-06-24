package org.openpanfu.gameserver.commands;

import org.openpanfu.gameserver.GameServer;
import org.openpanfu.gameserver.PanfuPacket;
import org.openpanfu.gameserver.User;
import org.openpanfu.gameserver.constants.Packets;

public class Summon implements ICommand {
    @Override
    public void onExecution(User invoker, String[] parameters) {
        if(parameters.length > 0 && !parameters[0].equals("")) {
            String mentionedUser = parameters[0];
            try {
                User userToSummon = GameServer.getUserByUsername(mentionedUser);
                if(userToSummon.isLoggedIn()){
                    int x = invoker.getX();
                    int y = invoker.getY();
                    if(userToSummon.getRoomId() == invoker.getRoomId()){
                        userToSummon.move(x, y, 250, 1);
                    }else{
                        String username = invoker.getUsername();
                        int roomID = invoker.getRoomId();
                        userToSummon.joinRoom(roomID);
                        userToSummon.setX(x);
                        userToSummon.setY(y);
                        userToSummon.sendAlert("You were magically summoned by " + username);
                    }
                }else{
                    invoker.sendAlert("Summon failed because user is offline.");
                }
            }
            catch(Exception e) {
                invoker.sendAlert("Summon failed because user is offline or doesn't exist.");
            }
            /*PanfuPacket movePacket = new PanfuPacket(Packets.RES_MOVE_AVATAR);
              movePacket.writeInt(invoker.getUserId());
              movePacket.writeString(mentionedUser);
              invoker.sendRoom(movePacket);*/
        }
    }

    @Override
    public String getDescription() {
        return "!summon (user) - Summons user to you.";
    }
}