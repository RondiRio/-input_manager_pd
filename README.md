# Gerenciador de Insumos PD
Sistema de gerenciamento de matérias-primas industriais e otimização de produção construído para a Projedata.

## Tecnologias usadas
- **Backend**: Java 17 + Quarkus 3.x
- **Frontend**: Vue.js 3 + Vite
- **Banco de Dados**: H2 (desenvolvimento) / PostgreSQL (produção)
- **Testes**: JUnit 5 + REST Assured (backend), Vitest + Vue Test Utils (frontend)

## Pré-requisitos
- Java 17+
- Node.js 18+
- Maven 3.9+ (ou use o wrapper do Maven incluído)

## Executando Localmente
### Backend
```bash
cd backend
./mvn quarkus:dev
```
O backend inicia em `http://localhost:8080`. O console de desenvolvimento do H2 está disponível em `http://localhost:8080/q/dev`.

### Frontend
```bash
cd frontend
npm install
npm run dev
```
O frontend inicia em `http://localhost:5173` e faz proxy das chamadas de API para o backend.

## Executando Testes
### Backend
```bash
cd backend
./mvn test
```

### Frontend
```bash
cd frontend
npm test
```

## Estrutura do Projeto
```
input-manager-pd/
  backend/ API REST em Java/Quarkus
  frontend/ SPA em Vue.js 3
```

## Endpoints da API
| Método | Caminho | Descrição |
|--------|-------------------------------|---------------------------------|
| GET | /api/raw-materials | Listar todas as matérias-primas |
| GET | /api/raw-materials/{id} | Obter matéria-prima por ID |
| POST | /api/raw-materials | Criar matéria-prima |
| PUT | /api/raw-materials/{id} | Atualizar matéria-prima |
| DELETE | /api/raw-materials/{id} | Excluir matéria-prima |
| GET | /api/products | Listar todos os produtos |
| GET | /api/products/{id} | Obter produto por ID |
| POST | /api/products | Criar produto com composição |
| PUT | /api/products/{id} | Atualizar produto com composição |
| DELETE | /api/products/{id} | Excluir produto |
| POST | /api/optimization/optimize | Executar otimização de produção |
| POST | /api/auth/login | Autenticar e obter token JWT |

## Segurança
- Autenticação JWT com assinatura RSA-256
- Validação de entrada via Hibernate Validator
- Prevenção de injeção SQL via consultas parametrizadas (Hibernate/Panache)
- CORS restrito à origem do frontend

## Licença
Desenvolvido por Rondineli Oliveira


English Version 
# Input Manager PD

Industrial raw materials management and production optimization system built for Projedata.

## Techs

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
./mvn quarkus:dev
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
./mvn test
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

Developed by Rondineli Oliveira
