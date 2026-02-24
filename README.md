# Input Manager PD

Industrial raw materials management and production optimization system built for Projedata.

## Tech Stack

- **Backend**: Java 17 + Quarkus 3.x
- **Frontend**: Vue.js 3 + Vite
- **Database**: H2 (development) / PostgreSQL (production)
- **Testing**: JUnit 5 + REST Assured (backend), Vitest + Vue Test Utils (frontend)

## Prerequisites

- Java 17+
- Node.js 18+
- Maven 3.9+ (or use the included Maven wrapper)

## Running Locally

### Backend

```bash
cd backend
./mvnw quarkus:dev
```

The backend starts at `http://localhost:8080`. The H2 dev console is available at `http://localhost:8080/q/dev`.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

The frontend starts at `http://localhost:5173` and proxies API calls to the backend.

## Running Tests

### Backend

```bash
cd backend
./mvnw test
```

### Frontend

```bash
cd frontend
npm test
```

## Project Structure

```
input-manager-pd/
  backend/       Java/Quarkus REST API
  frontend/      Vue.js 3 SPA
```

## API Endpoints

| Method | Path                          | Description                     |
|--------|-------------------------------|---------------------------------|
| GET    | /api/raw-materials            | List all raw materials          |
| GET    | /api/raw-materials/{id}       | Get raw material by ID          |
| POST   | /api/raw-materials            | Create raw material             |
| PUT    | /api/raw-materials/{id}       | Update raw material             |
| DELETE | /api/raw-materials/{id}       | Delete raw material             |
| GET    | /api/products                 | List all products               |
| GET    | /api/products/{id}            | Get product by ID               |
| POST   | /api/products                 | Create product with composition |
| PUT    | /api/products/{id}            | Update product with composition |
| DELETE | /api/products/{id}            | Delete product                  |
| POST   | /api/optimization/optimize    | Run production optimization     |
| POST   | /api/auth/login               | Authenticate and get JWT token  |

## Security

- JWT authentication with RSA-256 signing
- Input validation via Hibernate Validator
- SQL injection prevention via parameterized queries (Hibernate/Panache)
- CORS restricted to frontend origin

## License

Proprietary - Rondineli Oliveira
