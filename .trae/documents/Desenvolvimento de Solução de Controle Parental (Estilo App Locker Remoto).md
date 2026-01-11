# Plano de Implementação: Solução de Controle Parental B2C

Este plano visa criar um MVP (Produto Mínimo Viável) de um aplicativo de controle parental com gerenciamento remoto, focado em bloqueio de apps e prevenção de desinstalação, sem necessidade de formatação (MDM Enterprise).

## 1. Análise e Seleção da Base (Estado Atual)
Encontrei projetos open-source que atendem a 90% dos seus requisitos. Não precisamos reinventar a roda, apenas integrar as melhores partes:
*   **Base Principal:** **`KidSafe`** (GitHub: xMansour/KidSafe). Já possui a arquitetura "Pai (Admin) x Filho (Dispositivo)" com bloqueio remoto e uso do Firebase.
*   **Lógica de Bloqueio:** **`AppLock`** (GitHub: PranavPurwar/AppLock). Possui uma UI mais moderna e lógica robusta de detecção de apps via `AccessibilityService`.

## 2. Arquitetura da Solução
A solução será composta por três partes:
1.  **App Android (O "Agente"):** Instalado no celular da criança.
    *   *Funcionalidade:* Roda em segundo plano, verifica qual app está aberto, bloqueia se estiver na "lista negra", impede desinstalação.
2.  **Portal do Cliente (Painel dos Pais):** Web App (React/Next.js ou o próprio Firebase Console simplificado inicialmente).
    *   *Funcionalidade:* O pai define a senha do app e escolhe quais apps bloquear.
3.  **Super Admin (Sua Console):**
    *   *Funcionalidade:* Você cria os logins para os Pais (Clientes) e gerencia as assinaturas.

## 3. Etapas de Execução

### Fase 1: Setup e Prova de Conceito (PoC)
1.  **Clonagem e Análise:** Baixar o código do `KidSafe` para entender a comunicação com o Firebase.
2.  **Configuração do Ambiente:** Configurar o projeto Android localmente e conectar a um projeto Firebase de teste.
3.  **Teste de Bloqueio:** Implementar/Verificar o `AccessibilityService` para garantir que ele detecta a abertura de apps (ex: YouTube) e joga uma tela de bloqueio por cima.

### Fase 2: Implementação das Regras de Negócio (Sua "Dor")
1.  **Sistema de Login Unificado:** Alterar o login do app para autenticar contra o banco de dados que você gerencia (não cadastro livre).
2.  **Sincronização de Senha:** Fazer o app baixar a "Senha de Desbloqueio" definida no portal.
3.  **Anti-Desinstalação (Hardening):**
    *   Implementar `DeviceAdminReceiver` (impede desinstalação simples).
    *   Adicionar lógica para bloquear o app "Configurações" (Settings) do Android se o usuário tentar remover a permissão de admin.

### Fase 3: Portal Web (Básico)
1.  **Dashboard Simples:** Criar uma página web onde se insere o ID do dispositivo e seleciona-se a lista de apps proibidos (com base no package name, ex: `com.instagram.android`).

## Requisitos Técnicos
*   **Linguagem Mobile:** Kotlin (Android Nativo) ou Java (O KidSafe é em Java, podemos migrar ou manter).
*   **Backend:** Firebase (Auth + Realtime Database) - Ideal para começar rápido e suporta a sincronização em tempo real que você quer.

## Pergunta para Confirmação
Você prefere que eu tente clonar o repositório `KidSafe` agora para usarmos como base, ou prefere que eu crie um projeto "limpo" apenas com as funcionalidades essenciais (Bloqueio + Admin) para ter um código mais enxuto e fácil de manter?

*(Recomendo criar um limpo baseado nos exemplos, pois códigos prontos antigos podem ter muitas dependências quebradas, mas fico à sua disposição).*