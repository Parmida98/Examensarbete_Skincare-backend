# Skincare Backend API
## 🫧 Skin Match 🫧

---
## Idea
This project is a backend API for a skincare application.  
The purpose is to help users understand which ingredients are suitable for different skin types, based on structured and quality-assured data.

The application is intended to function as a data source for a frontend (web/app) where the user can:
- select a skin type
- receive suitable ingredients for the specific skin type
- search for ingredients
- read descriptions

---

## MVP (Minimum Viable Product)
The MVP focuses on the following functionality:

- List all skin types
- Retrieve ingredients filtered by skin type
- Support for search and pagination
- Stable and consistent API responses (DTO-based)
- Centralized error handling
- Automatic import of ingredient data from JSON on startup

---

## Technology & Architecture
- **Java + Spring Boot**
- **Spring Web (REST API)**
- **Spring Data JPA**
- **PostgreSQL**
- **DTO**
- **Global Exception Handling (`@RestControllerAdvice`)**
- **Tests**
- **Docker**

---

## Dataset & Import
- Ingredients and their relationships to skin types are loaded from a JSON file
- Only new or modified data is saved
- Relationships are kept synchronized in both directions

---

## API Documentation
Complete documentation of endpoints, parameters, and responses can be found in:

**`API.md`**

---

### Sources

`https://arsenaultaesthetics.com/skin-care/skincare-ingredients-101/`

`https://www.theskininstitute.org/natural-skin-care-ingredients/`

`https://www.dodoskin.com/pages/k-beauty-ingredients-dictionary?srsltid=AfmBOoqqhUtkgS6JDoBhOeN5cXZ5mTh69Zm64sR6Ukzbrz2c0EUhnDEF`

`https://www.tirabeauty.com/article/articles/dr-monica-jacobs-beginner-friendly-guide-to-korean-skincare-ingredients?srsltid=AfmBOoqddCuBOpFYFirbKjfK00R2AZJdzZs8DyOA-0ytBTwNaBKL9vim`

`https://www.byrdie.com/skincare-ingredients-glossary-4800556`

---
