package org.openpanfu.gameserver.handler;

import org.openpanfu.gameserver.GameServer;
import org.openpanfu.gameserver.PanfuPacket;
import org.openpanfu.gameserver.User;
import org.openpanfu.gameserver.constants.Packets;
import org.openpanfu.gameserver.database.dao.GameServerDAO;
import org.openpanfu.gameserver.util.Logger;

public class CMD_GOTO_PLAYER implements IHandler { // 23
    @Override
    public void handlePacket(PanfuPacket packet, User sender) {
        int[] illegalRooms = { 44, 45, 46, 47, 48, 51, 52, 53, 57, 61, 63};
        int userId = packet.readInt();
        int currentRoom = sender.getRoomId();
        try {
            User buddy = GameServer.getUserById(userId);
            if (buddy != null) {
                if(buddy.isLoggedIn()) {
                    int roomID = buddy.getRoomId();
                    int x = buddy.getX();
                    int y = buddy.getY();
                    if (roomID > -1) {
                        if(roomID != currentRoom){
                            // Check for quest rooms
                            for (int illegal_room : illegalRooms){
                                if(illegal_room == roomID){
                                    gotoReason(sender, userId, "notAllowed");
                                    return;
                                }
                            }
                            // Check if player tries to join treehouse
                            if(roomID > 1000){
                                //Check for locked house
                                if(GameServerDAO.getLockedStatus(roomID) == 0){
                                    sender.joinHome(roomID);
                                }else{
                                    gotoReason(sender, userId, "locked");
                                }
                            }else{
                                sender.joinRoom(roomID);
                            }
                            sender.setX(x);
                            sender.setY(y);
                            PanfuPacket gotoPlayerPacket = new PanfuPacket(Packets.RES_GOTO_PLAYER);
                            gotoPlayerPacket.writeInt(sender.getUserId());
                            sender.sendPacket(gotoPlayerPacket);

                            Logger.info("Player " + sender.getUserId() + " rocketed to " + buddy.getUsername());
                        }else{
                            gotoReason(sender, userId, "sameRoom");
                        }
                    }else{
                        // Technically, this will never happen as we don't unset player from room during gaming.
                        gotoReason(sender, userId, "gaming");
                    }
                }else{
                    gotoReason(sender, userId, "offline");
                }
            }
        }
        catch(Exception e) {
            gotoReason(sender, userId, "offline");
        }
    }

    private void gotoReason(User sender, int userID, String reason){
        PanfuPacket gotoPlayerPacket = new PanfuPacket(Packets.RES_GOTO_PLAYER);
        gotoPlayerPacket.writeInt(userID);
        gotoPlayerPacket.writeString(reason);
        sender.sendPacket(gotoPlayerPacket);
        /* Removes player status as it's stuck after failing to rocket */
        PanfuPacket response = new PanfuPacket(Packets.RES_PLAYER_STATUS_REQUEST);
        response.writeInt(sender.getUserId());
        response.writeInt(0);
        sender.sendRoom(response);
    }
}
