# INSTALACION

Para levantar **PostgreSQL** con **Docker Compose** y cargar automáticamente una base de datos de ejemplo al iniciar por primera vez, puedes usar el directorio `/docker-entrypoint-initdb.d/` del contenedor oficial. Cualquier archivo `.sql` o `.sh` colocado ahí se ejecuta automáticamente cuando se crea el volumen de datos por primera vez.

A continuación tienes una configuración completa usando **PostgreSQL 18** (con soporte hasta noviembre de 2030; también puedes usar `postgres:19`, lanzado en noviembre de 2026, o `postgres:17`).

---

### 1. Crea la estructura de carpetas y archivos

Crea una carpeta para tu proyecto y dentro coloca `docker-compose.yml` y `init.sql`:

```text
postgres-demo/
├── docker-compose.yml
└── init.sql

```

### 2. Crea el archivo `docker-compose.yml`

```yaml
services:
  db:
    image: postgres:18
    container_name: postgres_ejemplo
    restart: unless-stopped
    environment:
      POSTGRES_USER: devuser
      POSTGRES_PASSWORD: devpassword123
      POSTGRES_DB: tienda_ejemplo
      TZ: America/Panama
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql:ro
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U devuser -d tienda_ejemplo"]
      interval: 5s
      timeout: 5s
      retries: 5

volumes:
  pgdata:

```

> **Nota importante:** Los scripts en `/docker-entrypoint-initdb.d/` **solo se ejecutan cuando el volumen `pgdata` está vacío** (en el primer arranque). Si modificas `init.sql` más adelante y quieres volver a cargarlo desde cero, debes detener y borrar el volumen con `docker compose down -v`.

### 3. Crea el archivo `init.sql` con datos de ejemplo

Este script crea tablas de clientes, productos y pedidos con datos de prueba:

```sql
CREATE TABLE IF NOT EXISTS clientes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(120) UNIQUE NOT NULL,
    ciudad VARCHAR(80),
    creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS productos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    precio NUMERIC(10, 2) NOT NULL CHECK (precio >= 0),
    stock INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0)
);

CREATE TABLE IF NOT EXISTS pedidos (
    id SERIAL PRIMARY KEY,
    cliente_id INTEGER NOT NULL REFERENCES clientes(id) ON DELETE CASCADE,
    producto_id INTEGER NOT NULL REFERENCES productos(id),
    cantidad INTEGER NOT NULL CHECK (cantidad > 0),
    fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO clientes (nombre, correo, ciudad) VALUES
    ('Ana Martínez', 'ana.martinez@ejemplo.com', 'Ciudad de Panamá'),
    ('Carlos Mendoza', 'carlos.mendoza@ejemplo.com', 'David'),
    ('Sofía Herrera', 'sofia.herrera@ejemplo.com', 'Las Tablas')
ON CONFLICT (correo) DO NOTHING;

INSERT INTO productos (nombre, precio, stock) VALUES
    ('Teclado Mecánico', 79.99, 25),
    ('Mouse Inalámbrico', 34.50, 40),
    ('Monitor 27 Pulgadas', 249.00, 12)
ON CONFLICT DO NOTHING;

INSERT INTO pedidos (cliente_id, producto_id, cantidad) VALUES
    (1, 1, 1),
    (1, 2, 2),
    (2, 3, 1);

```

### 4. Levanta el servicio y verifica que funcione

1. **Inicia el contenedor en segundo plano:**
```bash
docker compose up -d

```


*Verificación:* Ejecuta `docker compose ps` y comprueba que `postgres_ejemplo` aparece con estado `Up` y `(healthy)`.
2. **Conéctate a la base de datos de ejemplo con `psql`:**
```bash
docker compose exec db psql -U devuser -d tienda_ejemplo

```


*Verificación:* Deberías ver el prompt `tienda_ejemplo=#` en la terminal.
3. **Consulta las tablas y los datos cargados:**
```sql
\dt
SELECT c.nombre AS cliente, p.nombre AS producto, pe.cantidad, (p.precio * pe.cantidad) AS total
FROM pedidos pe
JOIN clientes c ON c.id = pe.cliente_id
JOIN productos p ON p.id = pe.producto_id;

```


*Verificación:* `\dt` listará `clientes`, `productos` y `pedidos`, y la consulta devolverá 3 filas con los totales calculados. 
Escribe `\q` para salir de `psql`.

