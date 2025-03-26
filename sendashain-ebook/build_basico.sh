echo '______________________________________________________'
echo 'Generando pdf simple'
pandoc *.md -o /home/avbravo/Descargas/sendashain-simple.pdf --css=style.css --table-of-contents  -V linkcolor:blue 

echo 'Book creado en /home/avbravo/Descargas/sendashain-simple.pdf'
echo '______________________________________________________'