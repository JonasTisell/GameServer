package org.openpanfu.gameserver.commands;

import org.openpanfu.gameserver.User;

import java.util.StringJoiner;

public class AlertRoom implements ICommand {
	@Override
	public void onExecution(User invoker, String[] parameters) {
			StringJoiner joiner = new StringJoiner(" ");
			for(int i = 0; i < parameters.length; i++) {
				joiner.add(parameters[i]);
			}
			String message = joiner.toString();
			String alert = message + "\r\nYour Panfu Team";
			for (User u : invoker.getGameServer().getSessionManager().getUsersInRoom(invoker.getRoomId(), invoker.isInHome(), invoker.getSubRoom())) {
				u.sendAlert(alert);
			}
		}


	@Override
	public String getDescription() {
		return "!roomalert (message) - Sends message to everyone in room.";
	}
}
