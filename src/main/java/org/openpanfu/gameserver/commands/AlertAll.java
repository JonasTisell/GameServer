package org.openpanfu.gameserver.commands;

import org.openpanfu.gameserver.User;

import java.util.StringJoiner;

public class AlertAll implements ICommand {
	@Override
	public void onExecution(User invoker, String[] parameters) {
			StringJoiner joiner = new StringJoiner(" ");
			for(int i = 0; i < parameters.length; i++) {
				joiner.add(parameters[i]);
			}
			String message = joiner.toString();
			String alert = message + "\r\nYour Panfu Team";
			for (User u : invoker.getGameServer().getSessionManager().getUsers()) {
				u.sendAlert(alert);
			}
		}


	@Override
	public String getDescription() {
		return "!alert (message) - Sends message to everyone in server.";
	}
}
