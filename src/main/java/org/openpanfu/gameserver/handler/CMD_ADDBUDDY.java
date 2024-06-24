package org.openpanfu.gameserver.handler;

import org.openpanfu.gameserver.PanfuPacket;
import org.openpanfu.gameserver.User;
import org.openpanfu.gameserver.constants.Packets;

public class CMD_ADDBUDDY implements IHandler {
	@Override
	public void handlePacket(PanfuPacket packet, User sender) {
		int buddyId = packet.readInt();
		String yourUsername = packet.readString();
		try {
			User buddy = GameServer.getUserById(buddyId);
			if (buddy != null) {
				PanfuPacket inviteMessage = new PanfuPacket(Packets.RES_ADD_BUDDY);
				inviteMessage.writeInt(sender.getUserId());
				inviteMessage.writeString(sender.getUsername());
				buddy.sendPacket(inviteMessage);
			}
		}
		catch(Exception e) {
			sender.sendAlert("ADD_TO_BUDDYLIST_FAILED");
		}
	}
}
