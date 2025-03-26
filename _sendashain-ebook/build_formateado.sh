echo '______________________________________________________'
echo 'Generando pdf con tamaño de pagina, margenes y fuentes'
pandoc *.md -o /home/avbravo/Descargas/sendashain-formateado.pdf --css=style.css --table-of-contents  -V linkcolor:blue -V papersize:a4 -V margin-left:3cm -V margin-right:2cm -V margin-top:3cm  -V margin-bottom:2cm -V fontsize:12pt

echo 'Book creado en /home/avbravo/Descargas/sendashain-formateado.pdf'
echo '______________________________________________________'