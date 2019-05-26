/**
 * SteamUser example - BasicBot
 *
 * Simply logs into Steam using account credentials, goes online on friends, and launches Team Fortress 2
 */

var SteamUser = require('../index.js'); // Replace this with `require('steam-user');` if used outside of the module directory
var client = new SteamUser();
var floods = [];
var playingFloods = [];

client.logOn({
	"accountName": "balls2095_og",
	"password": "shitshit74"
});

client.on('loggedOn', function(details) {
	console.log("Logged into Steam as " + client.steamID.getSteam3RenderedID());
	client.setPersona(SteamUser.EPersonaState.Online);
	//client.gamesPlayed(440);
});

client.on('error', function(e) {
	// Some error occurred during logon
	console.log(e);
});

client.on('webSession', function(sessionID, cookies) {
	console.log("Got web session");
	// Do something with these cookies if you wish
});

client.on('newItems', function(count) {
	console.log(count + " new items in our inventory");
});

client.on('emailInfo', function(address, validated) {
	console.log("Our email address is " + address + " and it's " + (validated ? "validated" : "not validated"));
});

client.on('wallet', function(hasWallet, currency, balance) {
	console.log("Our wallet balance is " + SteamUser.formatCurrency(balance, currency));
});

client.on('accountLimitations', function(limited, communityBanned, locked, canInviteFriends) {
	var limitations = [];

	if (limited) {
		limitations.push('LIMITED');
	}

	if (communityBanned) {
		limitations.push('COMMUNITY BANNED');
	}

	if (locked) {
		limitations.push('LOCKED');
	}

	if (limitations.length === 0) {
		console.log("Our account has no limitations.");
	} else {
		console.log("Our account is " + limitations.join(', ') + ".");
	}

	if (canInviteFriends) {
		console.log("Our account can invite friends.");
	}
});

client.on('vacBans', function(numBans, appids) {
	console.log("We have " + numBans + " VAC ban" + (numBans == 1 ? '' : 's') + ".");
	if (appids.length > 0) {
		console.log("We are VAC banned from apps: " + appids.join(', '));
	}
	client.joinChat("103582791459091346");
});

client.on('licenses', function(licenses) {
	console.log("Our account owns " + licenses.length + " license" + (licenses.length == 1 ? '' : 's') + ".");
});

client.on('chatMessage', function(room, chatter, message) {
	if (chatter == "76561198389097256") {
		var prefix = "/";
		if (message.startsWith(prefix)) {
			message = message.substring(prefix.length);
		}
		var cmd_split = message.split(" ");
		var cmd_id = cmd_split[0].toUpperCase();
		if (cmd_id == "FLOOD_CHAT") {
			var action = cmd_split[1].toUpperCase();
			if (action == "START" && floods.indexOf(cmd_split[2]) == -1) {
				client.joinChat(cmd_split[2]);
				var previous = prefix + cmd_split[0] + " " + cmd_split[1] + " " + cmd_split[2] + " " + cmd_split[3] + " ";
				floods.push([cmd_split[2], setInterval(function() {
					client.chatMessage(cmd_split[2], message.substring(message.indexOf(previous) + previous.length));
				}, parseInt(cmd_split[3]))]);
				client.chatMessage(room, "[legion_bot]: Started flooding group " + cmd_split[2]);
			} else if (action == "STOP") {
				for (var i = 0; i < floods.length; i++) {
					if (floods[i][0] == cmd_split[2]) {
						clearInterval(floods[i][1]);
						client.leaveChat(floods[i][0]);
						client.chatMessage(room, "[legion_bot]: Stopped flooding group " + floods[i][0]);
						break;
					}
				}
			}
		} else if (cmd_id == "FLOOD_CONNECT") {
			var action = cmd_split[1].toUpperCase();
			if (action == "START" && floods.indexOf(cmd_split[2]) == -1) {
				client.joinChat(cmd_split[2]);
				client.chatMessage(room, "[legion_bot]: Started flooding group " + cmd_split[2]);
				floods.push([cmd_split[2], setInterval(function() {
					client.leaveChat(cmd_split[2]);
					client.joinChat(cmd_split[2]);
				}, parseInt(cmd_split[3]))]);
			} else if (action == "STOP") {
				for (var i = 0; i < floods.length; i++) {
					if (floods[i][0] == cmd_split[2]) {
						clearInterval(floods[i][1]);
						client.leaveChat(floods[i][0]);
						client.chatMessage(room, "[legion_bot]: Stopped flooding group " + floods[i][0]);
						break;
					}
				}
			}
		} else if (cmd_id == "JOIN_CHAT") {
			client.joinChat(cmd_split[1]);
			client.chatMessage(room, "[legion_bot]: Joined chatroom " + cmd_split[1]);
		} else if (cmd_id == "FLOOD_PLAYING") {
			var action = cmd_split[1].toUpperCase();
			if (action == "START" && playingFloods.indexOf(cmd_split[2]) == -1) {
				client.joinChat(cmd_split[2]);
				client.chatMessage(room, "[legion_bot]: Started flooding group " + cmd_split[2]);
				playingFloods.push([cmd_split[2], setInterval(function() {
					client.gamesPlayed(440);
					client.gamesPlayed([]);
				}, parseInt(cmd_split[3]))]);
			} else if (action == "STOP") {
				for (var i = 0; i < playingFloods.length; i++) {
					if (playingFloods[i][0] == cmd_split[2]) {
						clearInterval(playingFloods[i][1]);
						client.gamesPlayed([]);
						client.chatMessage(room, "[legion_bot]: Stopped flooding group " + playingFloods[i][0]);
						break;
					}
				}
			}
		}
	}
});
