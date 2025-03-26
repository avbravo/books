# MVC 5


[Visual Studio Code Aspnet Mvc Quickstart - 3 Steps on Ubuntu](http://testbone.com/blog/vs-code-aspnet-mvc-quickstart-3-steps-ubuntu/)

[Instalación del SDK o el runtime de .NET en Ubuntu](https://learn.microsoft.com/es-es/dotnet/core/install/linux-ubuntu-install?tabs=dotnet8&pivots=os-linux-ubuntu-2204)

[net ](https://medium.com/latesttechupdates/understand-the-differences-between-asp-net-mvc-5-and-6-fd96a9d1df7e)

[ASP.NET MVC Docker Sample](https://github.com/microsoft/dotnet-framework-docker/blob/main/samples/aspnetmvcapp/README.md)

[How to create a docker image for containerizing an ASP.NET Core MVC 5.0 Web Application](https://bharatdwarkani.medium.com/how-to-create-a-docker-image-for-containerizing-an-asp-net-core-mvc-5-0-web-application-2ccfaa2b8b5f)

## Instalar

```
wget -q https://packages.microsoft.com/config/ubuntu/24.04/packages-microsoft-prod.deb -O packages-microsoft-prod.deb
sudo dpkg -i packages-microsoft-prod.deb
rm packages-microsoft-prod.deb
sudo add-apt-repository universe
sudo apt-get update
sudo apt-get install apt-transport-https
sudo apt-get update
sudo apt-get install aspnetcore-runtime-3.1

```

## Crear el proyecto

```
mkdir test-project

cd test-project

dotnet new MVC

```
