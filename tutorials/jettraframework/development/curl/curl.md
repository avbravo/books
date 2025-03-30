# Curl



curl -X GET -i http://localhost:8080/employees


curl -X GET -i http://localhost:8080/hello

curl -X GET -i http://localhost:8080/employees/1

curl -X POST -i http://localhost:8080/employees -H 'Content-Type: application/json' -d '{"firstname":"Davor","lastname":"Suker", "jobTitle": "Electrician"}'


curl -X PUT -i http://localhost:8080/employees/2 -H 'Content-Type: application/json' -d '{"firstname": "Shawn", "lastname":"Michaels", "jobTitle": "Admin"}'


curl -X DELETE -i http://localhost:8080/employees/3 -H "Accept: application/json"




### Paso 3: Probar los Endpoints
Una vez que el servidor esté en ejecución, puedes probar los endpoints utilizando herramientas como `curl` o Postman.

#### Ejemplo de Solicitudes

1. **Agregar un Usuario**
   - Método: `POST`
   - URL: `http://localhost:8080/users/add`
   - Body: `"Alice"`

   ```bash
   curl -X POST http://localhost:8080/users/add -d "Alice"
   ```

2. **Obtener un Usuario**
   - Método: `GET`
   - URL: `http://localhost:8080/users/get?id=123`

   ```bash
   curl -X GET "http://localhost:8080/users/get?id=123"
   ```

3. **Agregar un País**
   - Método: `POST`
   - URL: `http://localhost:8080/countries/add`
   - Body: `"Mexico"`

   ```bash
   curl -X POST http://localhost:8080/countries/add -d "Mexico"
   ```

4. **Obtener un País**
   - Método: `GET`
   - URL: `http://localhost:8080/countries/get?id=MX`

   ```bash
   curl -X GET "http://localhost:8080/countries/get?id=MX"
   ```

---