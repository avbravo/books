
# Instalar

[https://docs.flutter.dev/get-started/install/linux/android](https://docs.flutter.dev/get-started/install/linux/android )

1. Verify that you have the following tools installed: bash, file, mkdir, rm, which
 
```shelll
which bash file mkdir rm which

```

resultado

```shell
/bin/bash
/usr/bin/file
/bin/mkdir
/bin/rm
which: shell built-in command
```

2. Install the following packages: curl, git, unzip, xz-utils, zip, libglu1-mesa

```shell
sudo apt-get update -y && sudo apt-get upgrade -y;
sudo apt-get install -y curl git unzip xz-utils zip libglu1-mesa

```


3. To develop Android apps:

Install the following prequisite packages for Android Studio: libc6:i386, libncurses5:i386, libstdc++6:i386, lib32z1, libbz2-1.0:i386

```shell
sudo apt-get install  libc6:i386 libncurses5:i386  libstdc++6:i386 lib32z1  libbz2-1.0:i386
```


## Install Android Studio

1. Descarge Android Studio desde [https://developer.android.com/studio?hl=es-419](https://developer.android.com/studio?hl=es-419 ) 


2. Descomprima el archivo

3. Ingresar a 
```shell

cd android-studio

cd bin

./studio.sh
```

4. Al ejecutarse la instalación seleccionar todos los paquetes para instalarse.
Esperar a que termine de descargarse los elementos.



To install Android Studio on Linux, follow these steps:

Unpack the .zip file you downloaded to an appropriate location for your applications, such as within /usr/local/ for your user profile or /opt/ for shared users.
For a 64-bit version of Linux, first install the required libraries for 64-bit machines.

To launch Android Studio, open a terminal, navigate to the android-studio/bin/ directory, and execute studio.sh.

Select whether you want to import previous Android Studio settings, then click OK.

Complete the Android Studio Setup Wizard, which includes downloading the Android SDK components that are required for development.

## Tutorial
[https://developer.android.com/codelabs/basic-android-kotlin-compose-first-app?hl=es-419#0](https://developer.android.com/codelabs/basic-android-kotlin-compose-first-app?hl=es-419#0)

## Flutter

[https://docs.flutter.dev/get-started/install/linux/android](https://docs.flutter.dev/get-started/install/linux/android)
