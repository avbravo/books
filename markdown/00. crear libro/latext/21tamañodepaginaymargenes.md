# Tamaño de pagina y margenes

pandoc -V papersize:a4 -V margin-left:3cm \
    -V margin-right:2cm -V margin-top:3cm \
    -V margin-bottom:2cm \
documento.md -o documento.pdf