# Input Manager PD - Project Documentation

## What Was Done First

The project started with the foundational setup: choosing the technology stack (Quarkus for backend, Vue.js 3 for frontend, H2 for development database), defining the database schema as XML, and organizing the MVC architecture for both backend and frontend.

The repository was initialized with a `.gitignore`, `.editorconfig`, and `README.md` to establish coding standards from day one.

## Sprint Breakdown

### Sprint 1: Backend Setup + CRUD

**Goal**: Establish the backend foundation with full CRUD operations.

**Steps Followed**:
1. Created the Maven project with `pom.xml` and Quarkus 3.17.5 dependencies (REST, Hibernate ORM Panache, H2, Validation, JWT, Testing)
2. Configured `application.properties` with H2 in-memory database, CORS for the Vue.js frontend, and a PostgreSQL production profile
3. Created entity models (`RawMaterial`, `Product`, `ProductComposition`) using Hibernate Panache with BigDecimal precision (19,4) for industrial accuracy
4. Created DTOs to decouple the API contract from JPA entities, preventing circular serialization
5. Implemented `GlobalExceptionHandler` mapping exceptions to proper HTTP status codes (400, 404, 500)
6. Created repositories with Panache pattern and domain-specific queries (`findByCode`)
7. Implemented services with full CRUD, DTO mapping, duplicate code validation, and cascade delete protection
8. Created REST endpoints at `/api/raw-materials`, `/api/products`, and `/api/optimization/optimize`
9. Added seed data via `import.sql` with 5 raw materials and 3 products simulating a food factory

### Sprint 2: Frontend Setup + CRUD Integration

**Goal**: Build the complete Vue.js frontend with CRUD integration.

**Steps Followed**:
1. Initialized Vue 3 project with Vite, Vue Router (lazy loading), Pinia (state management), Vue I18n (PT-BR/EN-US), and Axios
2. Configured Vite proxy to route `/api` calls to the Quarkus backend at port 8080
3. Implemented the 60-30-10 color scheme: `#F8FAFC` (ice gray background), `#002D54` (dark blue sidebar), `#0066FF` (vibrant blue action buttons)
4. Created layout components: `AppSidebar` (dark blue navigation), `AppHeader` (with language switcher), `MainLayout` (responsive flex layout)
5. Created reusable UI components: `BaseButton`, `BaseCard`, `BaseModal`, `BaseInput`, `BaseAlert`, `DataTable` (sortable with actions)
6. Created Pinia stores for raw materials, products, and optimization with centralized API calls
7. Created feature components: `RawMaterialList/Form`, `ProductList/Form`, `CompositionEditor` (dynamic ingredient management with duplicate prevention)
8. Created views: `DashboardView` (summary cards), `RawMaterialsView`, `ProductsView`, `OptimizationView`

### Sprint 3: Production Optimization Algorithm + UI

**Goal**: Implement the core production optimization with Branch and Bound.

**Steps Followed**:
1. Modeled the problem as a multi-dimensional knapsack (Integer Linear Programming)
2. Implemented Branch and Bound algorithm in `ProductionOptimizationService`:
   - Built constraint matrix A[material][product], objective vector (prices), resource vector (stock)
   - Used best-first search with PriorityQueue ordered by LP relaxation upper bound
   - Added safety limits: 10-second timeout + 100,000 node limit
   - Handled edge cases: no stock, no products, products without composition
3. Created the optimization result DTO with production plan, material usage, and computation time
4. Built the frontend `OptimizationResult` component with:
   - Revenue summary card with gradient background
   - Production plan table (sorted by subtotal, zero-production items grayed out)
   - Material usage progress bars (green < 50%, yellow 50-80%, red > 80%)

### Sprint 4: Testing, i18n, Security, Polish

**Goal**: Add comprehensive tests, internationalization, and security.

**Steps Followed**:
1. **Backend Tests** (JUnit 5 + REST Assured):
   - `ProductionOptimizationServiceTest`: 8 test cases covering single product, competing products, no stock, no products, multiple constraints, material usage, computation time, and products without composition
   - `RawMaterialResourceTest`: 7 test cases for CRUD endpoints, validation errors, and 404s
   - `ProductResourceTest`: 6 test cases for product CRUD and optimization endpoint
2. **Frontend Tests** (Vitest + Vue Test Utils):
   - `rawMaterial.test.js`: Store tests with mocked API (fetch, create, remove, error handling)
   - `DataTable.test.js`: Component tests for rendering, empty state, loading, event emission
3. **Internationalization**:
   - Vue I18n with `pt-BR.json` and `en-US.json` locale files
   - Language switcher in the header with localStorage persistence
   - All UI text uses `$t()` translation keys
4. **Security**:
   - `AuthResource` with login endpoint (`/api/auth/login`)
   - Auth store with localStorage token persistence
   - Login view with form validation
   - CORS restricted to frontend origin
   - Prepared JWT structure via SmallRye JWT (disabled in dev, ready for production activation)

## Security Measures Applied

### 1. Input Validation
- **Backend**: Hibernate Validator annotations (`@NotBlank`, `@NotNull`, `@DecimalMin`) on all entity fields and DTOs. The `@Valid` annotation on REST endpoint parameters triggers automatic validation before the method body executes.
- **Frontend**: Client-side validation in all forms provides immediate feedback before server submission.

### 2. SQL Injection Prevention
- Hibernate ORM with Panache exclusively uses parameterized queries. There is zero raw SQL string concatenation anywhere in the codebase. All database access goes through the Panache repository pattern.

### 3. CORS (Cross-Origin Resource Sharing)
- Configured in `application.properties` to allow only `http://localhost:5173` (the Vite dev server).
- In production, this must be updated to the actual frontend domain via the `%prod` profile or environment variables.

### 4. Authentication
- JWT authentication structure via SmallRye JWT with RSA-256 signing (prepared but disabled in development mode).
- MVP login with `AuthResource` endpoint.
- Frontend route guards and token management via Pinia auth store.

### 5. Data Integrity
- Cascade delete protection: the system checks for composition references before allowing raw material deletion, returning HTTP 409 with a clear error message.
- Unique constraint on `product_composition(product_id, raw_material_id)` prevents duplicate ingredients.
- Unique codes on both raw materials and products prevent confusion.

### 6. API Security Best Practices
- No sensitive data in URLs: all mutations use POST/PUT with JSON bodies.
- Error responses use a consistent format (`{ error: "message", status: code }`) without exposing internal stack traces.
- GlobalExceptionHandler catches unexpected exceptions and returns generic messages.

## Architecture Overview

```
[Vue.js Frontend]  --HTTP/JSON-->  [Quarkus REST API]  --JPA-->  [H2 Database]
  (Port 5173)                       (Port 8080)

  Views (Router)                    Controllers (REST Resources)
    |                                 |
  Stores (Pinia)                    Services (Business Logic)
    |                                 |
  API Client (Axios)               Repositories (Panache)
                                      |
                                    Entities (JPA)
```

## How to Run

### Prerequisites
- Java 17+
- Node.js 18+
- Maven 3.9+ (or use the Maven wrapper)

### Backend
```bash
cd backend
./mvnw quarkus:dev
```

### Frontend
```bash
cd frontend
npm install
npm run dev
```

### Running Tests
```bash
# Backend tests
cd backend
./mvnw test

# Frontend tests
cd frontend
npm test
```

## Database Migration to PostgreSQL

1. Start a PostgreSQL instance (or use the docker-compose.yml)
2. Set environment variables: `DB_URL`, `DB_USER`, `DB_PASSWORD`
3. Run with production profile: `./mvnw quarkus:dev -Dquarkus.profile=prod`
