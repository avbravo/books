# Para conocer el IP ejecute

IP=$(ip route get 1.1.1.1 2>/dev/null | awk '{print $7; exit}')


echo $IP
