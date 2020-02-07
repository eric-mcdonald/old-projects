#pragma once

#include <vector>
#include <string>

#include "IRCSocket.h"
#include "projectx-shared.h"

class IRCClient;

extern std::vector<std::string> split(std::string const&, char);

struct PROJECTXSHARED_API IRCCommandPrefix
{
	void Parse(std::string data)
	{
		if (data == "")
			return;

		prefix = data.substr(1, data.find(" ") - 1);
		std::vector<std::string> tokens;

		if (prefix.find("@") != std::string::npos)
		{
			tokens = split(prefix, '@');
			nick = tokens.at(0);
			host = tokens.at(1);
		}
		if (nick != "" && nick.find("!") != std::string::npos)
		{
			tokens = split(nick, '!');
			nick = tokens.at(0);
			user = tokens.at(1);
		}
	};

	std::string prefix;
	std::string nick;
	std::string user;
	std::string host;
};

struct PROJECTXSHARED_API IRCMessage
{
	IRCMessage();
	IRCMessage(std::string cmd, IRCCommandPrefix p, std::vector<std::string> params) :
		command(cmd), prefix(p), parameters(params) {};

	std::string command;
	IRCCommandPrefix prefix;
	std::vector<std::string> parameters;
};

struct PROJECTXSHARED_API IRCCommandHook
{
	IRCCommandHook() : function(NULL) {};

	std::string command;
	void(*function)(IRCMessage /*message*/, IRCClient* /*client*/);
};
typedef PROJECTXSHARED_API void(*IrcCmdCallbackFn)(IRCMessage /*message*/, IRCClient* /*client*/);
