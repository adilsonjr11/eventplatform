# Event Platform

Plataforma de processamento de eventos orientada a microsserviços, projetada para atender requisitos de alta vazão, baixa latência, resiliência e evolução contínua, priorizando o desacoplamento entre componentes e escalabilidade horizontal em ambientes financeiros de missão crítica.

---

## 🛠️ Stack Tecnológica

| Tecnologia | Finalidade |
| :--- | :--- |
| **Java 21** | Linguagem principal |
| **Spring Boot 3** | Framework backend |
| **Kafka** | Mensageria e streaming de eventos |
| **PostgreSQL** | Persistência transacional e auditoria |
| **Redis** | Cache e controle de idempotência |
| **Docker / Docker Compose** | Containerização e orquestração local |
| **Resilience4j** | Padrões de resiliência (Circuit Breaker, Retry, etc.) |
| **Spring Boot Actuator** | Monitoramento e coleta de métricas |
| **Prometheus / Grafana** | Coleta de métricas e dashboards |
| **JUnit 5 / Mockito** | Testes unitários |
| **Testcontainers** | Testes de integração |
| **k6** | Testes de carga |

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
* **Java 21** instalado.
* **Maven** instalado.
* **Docker** e **Docker Desktop** rodando na máquina.

---

### Passo 1: Subir a Infraestrutura (Docker)
A aplicação depende do PostgreSQL, Redis e Kafka. Na raiz do projeto, acesse a pasta onde está o arquivo de configuração do Docker e suba os containers em segundo plano:

```bash
cd docker
docker compose up -d

## 📂 Estrutura do Projeto

```text
src/main/java/br/com/itau/eventplatform
├── api
│   ├── controller
│   ├── request
│   └── response
├── application
│   ├── service
│   └── usecase
├── domain
│   ├── model
│   └── repository
├── infrastructure
│   ├── kafka
│   ├── redis
│   ├── persistence
│   └── external
└── config

