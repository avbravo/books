## CURL
curl --location --request POST 'http://localhost:8080/products/' \
--header 'Content-Type: application/json' \
--data-raw '{"id": 1, "name": "banana", "description": "a fruit", "rating": 5}'

curl --location --request POST 'http://localhost:8080/products/' \
--header 'Content-Type: application/json' \
--data-raw '{"id": 2, "name": "watermelon", "description": "watermelon sugar ahh", "rating": 4}'

curl --location --request GET 'http://localhost:8080/products/'

curl --location --request GET 'http://localhost:8080/products/1'

curl --location --request DELETE 'http://localhost:8080/products/1'

## Formatear la salida

curl http://localhost:8080/api/pais/'http://localhost:8080/api/pais | json_pp
