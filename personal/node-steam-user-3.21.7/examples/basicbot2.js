/**
 * SteamUser example - BasicBot
 *
 * Simply logs into Steam using account credentials, goes online on friends, and launches Team Fortress 2
 */

var SteamUser = require('../index.js'); // Replace this with `require('steam-user');` if used outside of the module directory
var client = new SteamUser();

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
	client.joinChat("103582791434672565");
	client.joinChat("103582791455161763");
	client.joinChat("103582791429522385");
});

client.on('licenses', function(licenses) {
	console.log("Our account owns " + licenses.length + " license" + (licenses.length == 1 ? '' : 's') + ".");
});

client.on('chatMessage', function(room, chatter, message) {
	if (chatter == "76561198195581890" || chatter == "76561198129991312" || chatter == "76561198276040978") {
		var altQuotes = ["funny how ther is a new attacker suddenly.", "im a fire jumper. its my day off. im not being litteral in case someone is confused.", "im not saying incorrect. im nice.", "real gamers play arma", "i do my best to save. 1 survives out of the marked?! HOLY! and its because i saved them.", "to be honest its good money if i goto work today on my day off.", "im sad for yahoo.", "the worst op ever. no really. she has a handycap. but. this is her first op. and she is going hard as fuck.", "if we reflect forever", "if we reflect forever lets do this", "no im not a fire jumper. im waxing poetic.", "oh. i said an evil alt thing again. apparently.", "in the last 30 min has anyone told me anything about themself? that was true?", "im a fire jumper.", "its not judging its simply datamineing.", "i wish you could acctually \"see\" how i see.", "wana read my book? it will be in code.", "in the last 30 min has anyone told me anything about themself?", "dont waste a spawn. if it happens like you think you will be splashed regardless."];
		var responseQuote = altQuotes[Math.floor(Math.random() * (altQuotes.length - 1 + 1))];
		setTimeout(function() {
			client.chatMessage(room, responseQuote);
		}, (responseQuote.length + 1) * 100);
	} else if (chatter == "76561198074237739") {
		setTimeout(function() {
			client.chatMessage(room, "lmao nice 9gag profile pic McCy more like McFagit");
		}, 500);
	}
});
