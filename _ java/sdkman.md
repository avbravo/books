Installing SDKMAN! on UNIX is a breeze. It effortlessly sets up on macOS, Linux and Windows (with WSL). Plus, it's compatible with both Bash and ZSH shells.

Just launch a new terminal and type in:

```shell
curl -s "https://get.sdkman.io" | bash

```

Follow the on-screen instructions to wrap up the installation. Afterward, open a new terminal or run the following in the same shell:
```shell
source "$HOME/.sdkman/bin/sdkman-init.sh"
```
Lastly, run the following snippet to confirm the installation's success:

```shell
sdk version
```

Edite el archivo /etc/profile

```shell
sudo gnome-text-editor /etc/profile
```

Agregue
```shell
# SDKMan
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

Default

```shell
sdk default scala 3.4.2

```

