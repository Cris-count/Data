# Data: Pipeline de Analitica Predictiva de Ventas

Aplicacion fullstack academica para registrar ventas historicas, consultar metricas comerciales y generar predicciones simples del siguiente mes con un modelo heuristico estadistico. No implementa machine learning avanzado: la prediccion usa promedio mensual, variacion del ultimo mes y reglas de clasificacion.

## Stack

- Backend: Spring Boot 3, Java 21, Spring Security, JWT, Spring Data JPA, PostgreSQL.
- Frontend: Angular 21, Reactive Forms, guards, interceptor JWT y servicios por dominio.
- Docker: backend dockerizado y PostgreSQL local con `docker compose`.
- Cloud recomendado: Google Cloud Run + Cloud SQL PostgreSQL en capa gratuita o creditos iniciales.

## Ejecucion local con Docker

```bash
docker compose up --build
```

Backend disponible en `http://localhost:8080`.

Usuario demo creado al iniciar:

- Correo: `demo@data.com`
- Contrasena: `Demo1234`

## Frontend local

```bash
npm install
npm start
```

Angular queda en `http://localhost:4200` y consume `http://localhost:8080/api`.

## Variables de entorno backend

Ver `backend/.env.example`.

- `DATABASE_URL`: JDBC URL de PostgreSQL.
- `DATABASE_USERNAME`: usuario de base de datos.
- `DATABASE_PASSWORD`: contrasena de base de datos.
- `JWT_SECRET`: secreto largo para firmar tokens.
- `SERVER_PORT`: puerto HTTP, por defecto 8080.
- `CORS_ALLOWED_ORIGINS`: origenes permitidos para Angular.
- `SEED_DEMO_DATA`: activa datos demo.
- `JPA_DDL_AUTO`: `update` para desarrollo, migraciones reales para produccion.

## Endpoints principales

Auth:

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/me`

Ventas:

- `POST /api/sales`
- `GET /api/sales`
- `GET /api/sales/{id}`
- `PUT /api/sales/{id}`
- `DELETE /api/sales/{id}`

Analitica:

- `GET /api/analytics/summary`
- `GET /api/analytics/monthly-sales`
- `GET /api/analytics/by-category`
- `GET /api/analytics/by-region`
- `GET /api/analytics/top-products`

Predicciones:

- `POST /api/predictions/sales/next-month`
- `GET /api/predictions/sales`
- `GET /api/predictions/sales/{id}`
- `DELETE /api/predictions/sales/{id}`

Las rutas de ventas, analitica y predicciones requieren `Authorization: Bearer <token>`.

## Como probar por fases

1. Auth: registrar o iniciar sesion con el usuario demo. Verificar que `/api/auth/login` devuelve JWT y que `/api/auth/me` responde solo con token.
2. Ventas: crear una venta desde Angular o `POST /api/sales`; confirmar que `totalAmount` se calcula automaticamente.
3. Analitica: consultar `/api/analytics/summary`, `/monthly-sales`, `/by-category` y `/by-region`.
4. Prediccion: llamar `POST /api/predictions/sales/next-month`; revisar tendencia, confianza, mensaje y recomendacion.
5. Frontend: entrar a dashboard, ventas, analitica y predicciones; confirmar que el interceptor envia el JWT.

## Despliegue cloud recomendado: Google Cloud Run

Google Cloud Run es la opcion mas sencilla entre los proveedores permitidos porque despliega contenedores HTTP sin administrar servidores.

1. Crear proyecto en Google Cloud y activar Artifact Registry, Cloud Run y Cloud SQL Admin.
2. Crear una instancia PostgreSQL en Cloud SQL. Para pruebas academicas, usar la configuracion mas pequena disponible y apagar recursos cuando no se usen.
3. Crear base de datos `data_sales` y un usuario con contrasena segura.
4. Construir imagen:

```bash
gcloud auth login
gcloud config set project TU_PROYECTO
gcloud artifacts repositories create data-repo --repository-format=docker --location=us-central1
gcloud builds submit ./backend --tag us-central1-docker.pkg.dev/TU_PROYECTO/data-repo/data-backend:1.0
```

5. Desplegar en Cloud Run:

```bash
gcloud run deploy data-backend \
  --image us-central1-docker.pkg.dev/TU_PROYECTO/data-repo/data-backend:1.0 \
  --region us-central1 \
  --allow-unauthenticated \
  --set-env-vars SERVER_PORT=8080,JPA_DDL_AUTO=update,SEED_DEMO_DATA=false,CORS_ALLOWED_ORIGINS=http://localhost:4200 \
  --set-env-vars DATABASE_URL='jdbc:postgresql://IP_O_HOST:5432/data_sales',DATABASE_USERNAME='usuario',DATABASE_PASSWORD='contrasena',JWT_SECRET='secreto-largo'
```

6. Si se usa Cloud SQL con conexion privada o conector, configurar Cloud Run con la conexion de instancia y adaptar `DATABASE_URL` segun el metodo elegido.
7. Para frontend local, agregar el URL de Cloud Run a `CORS_ALLOWED_ORIGINS` si se cambia el origen de Angular.

## Notas academicas

La prediccion es un modelo heuristico basico:

- Agrupa ventas por mes.
- Calcula ingresos y unidades promedio.
- Compara el ultimo mes contra el promedio.
- Clasifica tendencia con reglas: `DECREASING`, `STABLE`, `GROWING`, `HIGH_GROWTH` o `LOW`.
- Ajusta la estimacion del siguiente mes con una fraccion de la variacion observada.

Esto es adecuado para explicar un pipeline predictivo introductorio sin afirmar que hay IA avanzada.
