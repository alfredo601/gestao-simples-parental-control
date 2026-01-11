# Gestão Simples — Parental Control

Aplicativo Android (nativo) para bloquear apps selecionados e impedir desinstalação, com portal web para configuração remota via Firebase.

## Recursos
- Bloqueio de apps via Accessibility Service
- Anti-desinstalação via Device Admin
- Sincronismo em tempo real (Firebase Realtime Database)
- Login do responsável (Firebase Auth)
- Portal web estático para configurar senha do app e lista de pacotes
- Pipeline CI (GitHub Actions) que gera `app-debug.apk`

## Estrutura
- Android: `app/`
- Portal web: `web_portal/`
- CI: `.github/workflows/android-ci.yml`

## Pré-requisitos (Android)
- Android Studio + JDK 17
- Colocar o arquivo real `app/google-services.json` do seu projeto Firebase (package: `com.vectortech.gestaosimples`)

## Execução (Android)
1. Abra o projeto no Android Studio
2. Build → Build APK(s) → `app/build/outputs/apk/debug/app-debug.apk`
3. Instale no dispositivo
4. Login com usuário do Firebase (Auth)
5. Ative Administrador do Dispositivo e o Serviço de Acessibilidade

## Portal Web
Abra `web_portal/index.html` no navegador. Conecte ao Firebase:
- Cole o JSON Web do Firebase no campo **Firebase Config** e clique **Conectar**
- Ou carregue `web_portal/config.local.json` com a configuração Web
- Preencha Email, Senha do App, ID do Dispositivo e a lista de pacotes, clique **Aplicar Lista**

## Realtime Database (estrutura)
```
devices/
  <DEVICE_ID>/
    email: string
    childPassword: string
    blockedApps: string[]
```

## CI — GitHub Actions
- Secret necessário: `FIREBASE_GOOGLE_SERVICES_JSON` com o conteúdo do `google-services.json`
- Workflow: compila `:app:assembleDebug` e publica `app-debug.apk` como artefato

## Licença
MIT — veja `LICENSE`

