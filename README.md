# TP11 – Authentification JDBC / JPA avec Spring Security



## 🎯 Objectif

Ce TP remplace l'authentification en mémoire des TP précédents par une **authentification persistante basée sur une base de données MySQL**.

L'étudiant apprend à :
- Créer une base MySQL contenant des utilisateurs et des rôles
- Configurer Spring Data JPA pour accéder à ces données
- Implémenter un `UserDetailsService` personnalisé pour charger les utilisateurs depuis la base
- Sécuriser les mots de passe avec **BCrypt**

---

## 🏗️ Structure du projet
```
src/
├── main/
│   ├── java/ma/fstg/security/
│   │   ├── SpringSecurityApplication.java
│   │   ├── config/
│   │   │   ├── SecurityConfig.java
│   │   │   └── DatabaseInitializer.java
│   │   ├── controller/
│   │   │   └── MainController.java
│   │   ├── entities/
│   │   │   ├── User.java
│   │   │   └── Role.java
│   │   ├── repositories/
│   │   │   ├── UserRepository.java
│   │   │   └── RoleRepository.java
│   │   └── services/
│   │       └── CustomUserDetailsService.java
│   └── resources/
│       ├── application.properties
│       └── templates/
│           ├── login.html
│           ├── home.html
│           ├── admin-dashboard.html
│           └── user-dashboard.html
```



## 🔐 Modèle de sécurité

### Rôles et accès

| Rôle | Routes accessibles |
|---|---|
| `ROLE_ADMIN` | `/home` · `/user/dashboard` · `/admin/dashboard` |
| `ROLE_USER` | `/home` · `/user/dashboard` |

> ✅ **L'admin peut accéder aux deux espaces** (user + admin).  
> 🔒 **L'utilisateur standard est limité à son espace** (`/user/dashboard` uniquement).

### Comptes de test

| Identifiant | Mot de passe | Rôle(s) |
|---|---|---|
| `admin` | `1234` | `ROLE_ADMIN` + `ROLE_USER` |
| `user` | `1111` | `ROLE_USER` |

---

## 🖥️ Aperçu des pages

### Page de connexion

<img width="647" height="860" alt="authentification " src="https://github.com/user-attachments/assets/fe1d4f71-150f-408a-9e94-a65cc5192f45" />


---

### Page d'accueil — après connexion

<img width="632" height="771" alt="admin" src="https://github.com/user-attachments/assets/c5f3f3b8-cfa3-40b1-ac56-06ceb458f9ea" />

---

### Espace Utilisateur — `/user/dashboard`

> 🔒 **L'utilisateur standard (`user`) est limité à cet espace uniquement.**  
> Il ne peut **pas** accéder à `/admin/dashboard` → **403 Forbidden**.

<img width="640" height="597" alt="user" src="https://github.com/user-attachments/assets/85cf7348-ef3f-4241-a016-6814258ea177" />


---

## 🔄 Flux d'authentification
```
Navigateur          Spring Security           Base MySQL
    │                     │                       │
    │──── POST /login ────►│                       │
    │                     │──findByUsername() ────►│
    │                     │◄─── User + Roles ──────│
    │                     │── BCrypt.matches() ────│
    │◄── redirect /home ──│  (succès)             │
    │◄── redirect /login?error (échec)            │
```

