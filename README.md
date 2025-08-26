# 📄 ContractGenie

**ContractGenie** is an AI-powered web application designed for the **finance industry** to simplify contract management and enhance decision-making.  
It allows users to **upload contracts** and interact with them through an **intelligent chatbot** that uses **RAG (Retrieval Augmented Generation)** for contract-specific queries and **general AI reasoning** for broader questions.  

---

## 🚀 Version 1.0 – Key Features

1. **Authentication**
   - Secure JWT-based Login/Signup.  
   - Role-based access for enterprise use cases.

2. **AI-Powered Chat**
   - Hybrid mode:
     - **RAG-based answers** for contract-specific queries.  
     - **General AI reasoning** for non-contract-related questions.  
   - Automatic intent classification with a **tooling function** to decide which mode to use.  
   - Memory-enabled chat for multi-turn conversations.  

3. **Contract Upload & Management**
   - Upload contracts in PDF, DOCX, or TXT formats.  
   - AI indexes documents into a vector database for fast retrieval.  
   - Secure contract storage with metadata in PostgreSQL.  

---

## 💡 Example Use Cases

- **Contract Queries (RAG-powered):**
  - “What is the termination clause in my loan agreement?”
  - “List all payment obligations in this contract.”
  - “Does this contract allow early settlement?”

- **General Queries (AI-powered):**
  - “Summarize Basel III regulations in simple terms.”
  - “Best practices for contract compliance in finance.”
  - “Create a risk assessment checklist.”

---

## 🛠️ Technology Stack

### Frontend
- Angular + PrimeNG  
- Responsive UI with modern components  

### Backend
- Spring Boot (REST APIs, JWT Authentication)  
- PostgreSQL (contract metadata, chat history)  
- pgvector (vector embeddings for RAG)  

### AI Layer
- Retrieval Augmented Generation (RAG) pipeline  
- General AI responses (via LLM APIs, e.g., OpenAI/GPT)  
- Tooling function for **prompt classification**  
- Conversation memory  


