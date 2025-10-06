<div align="center">

# 🎓 API REST - MetaUni (Gestión Académica Universitaria)

![Java](https://img.shields.io/badge/Java-17-red)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.6-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![JWT](https://img.shields.io/badge/Seguridad-JWT-orange)
![Render](https://img.shields.io/badge/Deploy-Render-purple)
![Estado](https://img.shields.io/badge/Estado-En%20Desarrollo-yellow)

**API REST académica desarrollada con Spring Boot y PostgreSQL para la gestión universitaria de carreras, materias y usuarios.**

</div>

---

## 📖 Descripción

Backend del sistema MetaUni, desarrollado con Spring Boot 3.5.6, que provee una API REST para la gestión académica universitaria.
Permite manejar usuarios, carreras, materias y autenticación JWT, con persistencia en PostgreSQL y despliegue en Render.

Este proyecto nació como una práctica personal, con la idea de motivar a los estudiantes a seguir su progreso académico de una manera visual y entretenida.
Además, lo desarrollé para aprender la conexión entre backend y frontend y llevar una aplicación completa a la web.

Este backend es consumido por un **frontend en React**:  
🔗 [Proyecto-MetaUni-Frontend](https://github.com/joacomendiola/Proyecto-MetaUni-Frontend)

---

## ✨ Características

- **CRUD completo** para usuarios, carreras y materias  
- **Autenticación y autorización JWT**  
- **Arquitectura en capas:** Controller · DTO · Model · Repository · Security  
- **Validación de acceso por usuario logueado (Principal)**  
- **Configuración CORS** para comunicación con frontend  
- **Despliegue en Render** con PostgreSQL en la nube  
- **Logs de acciones** en consola (creación, edición y eliminación)  

---

## 🏗️ Stack Tecnológico

- ☕ **Java 17**  
- 🍃 **Spring Boot 3.5.6**  
- 🐘 **PostgreSQL (Render)**  
- 🗄️ **Spring Data JPA / Hibernate**  
- 🔐 **JWT (JSON Web Token)**  
- ⚙️ **Spring Security + BCrypt**  
- 📦 **Maven**  

---

## 📂 Estructura del Proyecto

```text
src/main/java/com/metauni/proyecto6/
├── controller/
│   ├── AuthController.java
│   ├── CarreraController.java
│   ├── MateriaController.java
│   └── UsuarioController.java
│
├── dto/
│   ├── CarreraDTO.java
│   ├── MateriaDTO.java
│   └── UsuarioDTO.java
│
├── model/
│   ├── Carrera.java
│   ├── Configuracion.java
│   ├── Materia.java
│   └── Usuario.java
│
├── repository/
│   ├── CarreraRepository.java
│   ├── ConfiguracionRepository.java
│   ├── MateriaRepository.java
│   └── UsuarioRepository.java
│
├── security/
│   ├── JwtFilter.java
│   ├── JwtUtil.java
│   └── WebSecurityConfig.java
│
└── Proyecto6MetaUniApplication.java
```

---

## 🔗 Endpoints Principales

### 🔒 Autenticación (`/api/auth`)
| Método | Endpoint | Descripción |
|--------|-----------|-------------|
| POST | `/api/auth/register` | Registro de nuevo usuario |
| POST | `/api/auth/login` | Inicio de sesión |
| GET  | `/api/auth/test-cors` | Prueba de conexión CORS |

---

### 🎓 Carreras (`/api/carreras`)
| Método | Endpoint | Descripción |
|--------|-----------|-------------|
| GET | `/api/carreras` | Listar todas las carreras |
| GET | `/api/carreras/{id}` | Obtener carrera por ID |
| POST | `/api/carreras` | Crear nueva carrera |
| PUT | `/api/carreras/{id}` | Actualizar carrera existente |
| DELETE | `/api/carreras/{id}` | Eliminar carrera |

---

### 📘 Materias (`/api/materias`)
| Método | Endpoint | Descripción |
|--------|-----------|-------------|
| GET | `/api/materias/carrera/{carreraId}` | Listar materias por carrera (usuario logueado) |
| POST | `/api/materias/{carreraId}` | Crear materia en una carrera |
| PUT | `/api/materias/{id}` | Editar materia existente |
| DELETE | `/api/materias/{id}` | Eliminar materia |

---

### 👤 Usuarios (`/api/usuarios`)
| Método | Endpoint | Descripción |
|--------|-----------|-------------|
| GET | `/api/usuarios` | Listar todos los usuarios |
| GET | `/api/usuarios/{id}` | Obtener usuario por ID |
| PUT | `/api/usuarios/{id}` | Editar perfil de usuario |
| DELETE | `/api/usuarios/{id}` | Eliminar usuario |


---

## 💻 Ejemplos Reales de Uso

### 🔐 Registro de usuario
```http
POST /api/auth/register
Content-Type: application/json

{
  "nombre": "Joaco",
  "email": "joaco@metauni.edu",
  "password": "1234"
}
```

### 🎓 Crear carrera
```http
POST /api/carreras
Content-Type: application/json

{
  "nombre": "Ingeniería en Sistemas",
  "totalMaterias": 40,
  "colorBarra": "#6366f1",
  "usuario": { "id": 1 }
}
```

### 📘 Crear materia
```http
POST /api/materias/1
Content-Type: application/json

{
  "nombre": "Programación I",
  "notaFinal": 9
}
```

### 👤 Editar perfil de usuario
```http
PUT /api/usuarios/1
Content-Type: application/json

{
  "nombre": "Joaquín Mendiola",
  "email": "joacomendiola@example.com"
}
```

---

## 🔐 Seguridad

- Autenticación **JWT** con `JwtUtil` y `JwtFilter`  
- Contraseñas encriptadas con **BCryptPasswordEncoder**  
- Validación del propietario (usuario logueado) al acceder a recursos  
- Configuración de seguridad en `WebSecurityConfig`  

---

## 🌐 Despliegue en Render

El backend se encuentra configurado para ser desplegado en **Render** con variables de entorno y PostgreSQL gestionado en la nube.  
Incluye health check con **Spring Actuator** para mantener el servicio activo.

---

## 👨‍💻 Autor

Desarrollado por **Joaquín Mendiola**  
🌐 GitHub: [joacomendiola](https://github.com/joacomendiola)

