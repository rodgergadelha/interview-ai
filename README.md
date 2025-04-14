# 📚 Interview AI API

Bem-vindo à **Interview AI API**, uma interface simples e 
direta para gerenciar perguntas de entrevista em sequência. 
Esta API foi criada para ajudar na preparação de entrevistas, permitindo que você
gere perguntas, responda-as e receba feedbacks detalhados sobre suas respostas.

---

## Pré-requisitos
Certifique-se de que você possui os seguintes itens instalados:
- **Java 21** ou superior
- **Maven** (para build e gerenciamento de dependências)
- **PostgreSQL** (banco de dados utilizado pelo projeto)
- Variáveis de ambiente necessárias:
    - `SPRING_DATASOURCE_USERNAME`: Usuário do banco de dados
    - `SPRING_DATASOURCE_PASSWORD`: Senha do banco de dados
    - `OPENAI_API_KEY`: Chave de API para integração com o OpenAI

## 🚀 Endpoints

### Esses endpoints também estão documentados com Swagger, acessível em `/swagger-ui/index.html`

### 📌 Criar perguntas e obter a primeira

**POST** `/api/interviews`

Gera todas as perguntas da entrevista via Chat GPT com os parâmetros informados e retorna a **primeira pergunta** da sequência.

#### 🔁 Exemplo de corpo da requisição:
```json
{
  "jobTitle": "Desenvolvedor Java Backend",
  "jobLevel": "Júnior",
  "interviewLanguage": "pt-BR",
  "numberOfQuestions": 2
}
```

#### 🔁 Exemplo de resposta:
```json
{
  "interviewUuid": "123e4567-e89b-12f3-a456-426614174000",
  "questionUuid": "123e4567-e89b-12d3-a456-426614174000",
  "question": "O que é o Java Virtual Machine (JVM) e qual é o seu papel na execução de aplicações Java?"
}
```

### 📌 Responder uma pergunta e obter próxima

**PATCH** `/api/interview-questions/{uuid}`

Responde a pergunta atual e retorna a **próxima pergunta** da sequência.

#### 🔁 Exemplo de corpo da requisição:
```json
{
  "questionUuid": "123e4567-e89b-12d3-a456-426614174000",
  "answer": "A Java Virtual Machine (JVM) é uma máquina virtual responsável por executar programas Java, atuando como uma camada intermediária entre o código compilado em bytecode e o sistema operacional. Quando um programa Java é compilado, ele não é convertido diretamente em código de máquina nativo, mas sim em bytecode, que é um formato intermediário interpretado ou compilado just-in-time pela JVM. O principal papel da JVM é proporcionar portabilidade, permitindo que o mesmo bytecode seja executado em qualquer sistema que possua uma implementação da JVM, além de gerenciar recursos como memória, threads e garbage collection, garantindo segurança, desempenho e independência de plataforma."
}
```

#### 🔁 Exemplo de resposta

```json
{
  "questionUuid": "3456e789-e89b-12d3-a456-426614174000",
  "question": "Como você pode gerenciar dependências em um projeto Java? Cite uma ferramenta que pode ser utilizada para isso."
}
```

Caso não haja mais perguntas, a resposta será 204 (No Content).

### 📌 Obter feedbacks

**GET** `/api/interviews/{interviewUuid}/feedbacks`

Obtém feedbacks para cada resposta da entrevista com o uuid informado.

#### 🔁 Exemplo de resposta:
```json
[
  {
    "question": "Como você pode gerenciar dependências em um projeto Java? Cite uma ferramenta que pode ser utilizada para isso.",
    "answer": "O gerenciamento de dependências em um projeto Java é feito por meio da definição e controle das bibliotecas externas que o projeto necessita para funcionar corretamente, evitando conflitos de versões, facilitando atualizações e garantindo reprodutibilidade. Uma das ferramentas mais utilizadas para esse fim é o Maven, que permite declarar as dependências em um arquivo `pom.xml`, e automaticamente baixa, atualiza e organiza essas bibliotecas a partir de repositórios centralizados, além de oferecer suporte a ciclos de build, testes, empacotamento e outras fases do desenvolvimento.",
    "feedback": "Sua resposta apresenta uma explicação clara e concisa sobre o gerenciamento de dependências em projetos Java, abordando aspectos importantes como a definição e controle das bibliotecas, a prevenção de conflitos de versões e a importância da reprodutibilidade. A menção ao Maven como ferramenta principal é acertada, e você destaca corretamente suas funcionalidades, como a declaração de dependências no `pom.xml` e a automação de processos. Para aprimorar ainda mais sua resposta, você poderia incluir um exemplo prático de como declarar uma dependência no `pom.xml`, o que demonstraria um entendimento mais profundo e prático da ferramenta, além de enriquecer a resposta com detalhes que podem ser valorizados em uma entrevista."
  },
  {
    "question": "O que é o Java Virtual Machine (JVM) e qual é o seu papel na execução de aplicações Java?",
    "answer": "A Java Virtual Machine (JVM) é uma máquina virtual responsável por executar programas Java, atuando como uma camada intermediária entre o código compilado em bytecode e o sistema operacional. Quando um programa Java é compilado, ele não é convertido diretamente em código de máquina nativo, mas sim em bytecode, que é um formato intermediário interpretado ou compilado just-in-time pela JVM. O principal papel da JVM é proporcionar portabilidade, permitindo que o mesmo bytecode seja executado em qualquer sistema que possua uma implementação da JVM, além de gerenciar recursos como memória, threads e garbage collection, garantindo segurança, desempenho e independência de plataforma.",
    "feedback": "Sua resposta está bem estruturada e aborda os pontos principais sobre a Java Virtual Machine (JVM) de forma clara e concisa. Você explicou que a JVM atua como uma camada intermediária entre o bytecode e o sistema operacional, além de mencionar a importância da portabilidade e do gerenciamento de recursos. No entanto, seria interessante incluir um exemplo prático de como a JVM permite a execução de aplicativos em diferentes plataformas, ou mencionar brevemente as diferenças entre a interpretação e a compilação just-in-time, para enriquecer ainda mais sua resposta. No geral, mostrou um bom entendimento do tema, adequado para um nível júnior."
  }
]
```