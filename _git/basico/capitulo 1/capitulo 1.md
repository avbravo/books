# Git
Libro sobre git
Crear Libro sobre Markdown
Fuente:
https://github.com/TheFern2/markdown-book

Instalar pandoc
sudo apt install pandoc
Para exportat a PDF
sudo apt install texlive-latex-base

Descargar el archivo export_book.py
del repositorio
https://github.com/TheFern2/markdown-book

Bajar el archivo tile.txt, pandoc.css, github.css



COMANDOS GIT
git add . 

git commit -m "capitulos"  

 git push  



Eliminar un archivo
Por ejemplo deseamos eliminar la carpeta ch3_1
git rm miarchivo.java

Ejecutar el commit

git commit -m "Removiendo ch3_1"

Ejecutar push



Pasos
Crear el repositorio en github
Clonar el proyecto
git clone https://github.com/avbravo/Building-JakartaEE-Applications-Microservices.gitç


3.Configurar el usuario



  git config --global user.name "avbravo"
 git config --global user.email "avbravo@gmail.com"


4. Crear las carpetas para los capítulos 
mkdir chapter01 chapter02 chapter03 chapter04 chapter05 chapter06 chapter07 chapter08 chapter09 chapter10 chapter11

5. Agregar el archivo readme a cada carpeta
6. Ejecutar

   git add chapter01
   git add chapter02
   git add chapter03
  git add chapter04
   git add chapter05
   git add chapter06
   git add chapter07
   git add chapter08
   git add chapter09
   git add chapter10
   git add chapter11




 
7. status
 git status

8. commit
 git commit -m "book"

9. push
git push

Eliminar una carpeta
Por ejemplo deseamos eliminar la carpeta ch3_1
git rm -r ch3_1

Ejecutar el commit

git commit -m "Removiendo ch3_1"

Ejecutar push
git push


Eliminar un archivo
Por ejemplo deseamos eliminar la carpeta ch3_1
git rm miarchivo.java

Ejecutar el commit

git commit -m "Removiendo ch3_1"

Ejecutar push




Cuando envia el error
Git: fatal: Unable to create '.git/index.lock': File exists.

entrar al directorio y ejecutar
cd /home/avbravo/NetBeansProjects/bpbonline/Building-JakartaEE-Applications-Microservices

Ejecutar
rm -f .git/index.lock






git add . 

git commit -m "capitulos"  

 git push  



