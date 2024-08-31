# Supplier Registration System
## Descrição
O Supplier Registration System é uma aplicação desenvolvida para cadastro e gerenciamento de fornecedores. Permite o cadastro de empresas (pessoa jurídica) e prestadores de 
serviços (pessoa física), incluindo detalhes como o nome do fornecedor, o principal contato e a descrição da atividade. O sistema é composto por uma API backend em Java usando
Spring Boot e um frontend com Thymeleaf e JavaScript.

## Tecnologias
- **Backend**: Java, Spring Boot, MongoDB
- **Frontend**: Thymeleaf, JavaScript, AJAX
- **Banco de Dados**: MongoDB
- **Segurança**: JWT (JSON Web Tokens)
- **Conteinerização**: Docker

## Instalação
### Pré-requisitos
- Java 11
- Maven
- Docker (para a configuração do MongoDB)

## Passos para Instalação
### Clonar o Repositório
'''
git clone https://github.com/seu-usuario/supplier_registration.git
cd supplier_registration
'''

Configuração do MongoDB com Docker

Para iniciar o MongoDB, use o docker-compose:

bash
Copiar código
docker-compose up -d
Isso irá configurar o MongoDB com as credenciais especificadas no docker-compose.yml.

Configuração do Ambiente

Crie um arquivo application.properties na pasta src/main/resources com o seguinte conteúdo:

properties
Copiar código
spring.application.name=supplier_registration
spring.data.mongodb.database=supplier-database
spring.data.mongodb.uri=mongodb://admin:admin1234@localhost:27017/
api.security.token.secret=${JWT_SECRET:my-secret-key}
Instalação das Dependências

Utilize o Maven para instalar as dependências:

bash
Copiar código
mvn install
Executar a Aplicação

Para iniciar o servidor Spring Boot:

bash
Copiar código
mvn spring-boot:run
Uso
API
Endpoint de Login

URL: /auth/login

Método: POST

Corpo da Requisição:

json
Copiar código
{
  "email": "usuario@example.com",
  "password": "senha"
}
Resposta:

json
Copiar código
{
  "token": "jwt-token"
}
Endpoint de Registro de Fornecedor

URL: /supplier-register

Método: POST

Cabeçalhos:

text
Copiar código
Authorization: Bearer <jwt-token>
Corpo da Requisição:

json
Copiar código
{
  "supplierName": "Fornecedor Exemplo",
  "contactName": "Contato Exemplo",
  "contactEmail": "contato@example.com",
  "personType": "COMPANY",
  "documentNumber": "00.000.000/0000-00",
  "phoneNumbers": ["+55 (11) 99999-9999"],
  "addressDTO": {
    "cep": "00000-000",
    "logradouro": "Rua Exemplo",
    "numero": "123",
    "complemento": "Apto 45",
    "bairro": "Bairro Exemplo",
    "localidade": "Cidade Exemplo",
    "uf": "SP",
    "pais": "Brasil"
  },
  "activityDescription": "Descrição da atividade"
}
Resposta:

json
Copiar código
{
  "id": "unique-id",
  "supplierName": "Fornecedor Exemplo",
  "contactName": "Contato Exemplo",
  "contactEmail": "contato@example.com",
  "personType": "COMPANY",
  "documentNumber": "00.000.000/0000-00",
  "phoneNumbers": ["+55 (11) 99999-9999"],
  "addressDTO": {
    "cep": "00000-000",
    "logradouro": "Rua Exemplo",
    "numero": "123",
    "complemento": "Apto 45",
    "bairro": "Bairro Exemplo",
    "localidade": "Cidade Exemplo",
    "uf": "SP",
    "pais": "Brasil"
  },
  "activityDescription": "Descrição da atividade"
}
Frontend
Página de Login

URL: /login
Descrição: Página para autenticação de usuários.
Página de Registro de Fornecedor

URL: /supplier-register
Descrição: Página para cadastro de fornecedores, incluindo formulário para dados do fornecedor e do contato.
Página de Visualização de Fornecedor

URL: /view-supplier/{id}
Descrição: Página para visualizar os detalhes de um fornecedor específico.
Estrutura do Projeto
Backend
/src/main/java/edu/challengethree/supplier_registration
AuthenticationController.java: Controlador para autenticação e gerenciamento de usuários.
SupplierController.java: Controlador para operações relacionadas aos fornecedores.
PagesController.java: Controlador para páginas HTML e navegação no frontend.
Frontend
/src/main/resources/templates

login.html: Página de login.
supplier-register.html: Página de cadastro de fornecedor.
view-supplier.html: Página para visualizar detalhes do fornecedor.
edit-supplier.html: Página para editar fornecedores existentes.
home.html: Página inicial após login.
/src/main/resources/static/js

main.js: Script JavaScript para interações de frontend, incluindo máscaras de input e validação de tokens.
Testes
Execute os testes unitários com o Maven:

bash
Copiar código
mvn test
Contribuição
Contribuições são bem-vindas! Se você encontrar um bug ou tiver uma melhoria para sugerir, sinta-se à vontade para abrir uma issue ou enviar um pull request.

Licença
Este projeto está licenciado sob a MIT License.

Autores
Seu Nome
Referências
Spring Boot Documentation
MongoDB Documentation
Thymeleaf Documentation
