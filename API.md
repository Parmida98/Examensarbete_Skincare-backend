# Skincare Backend (Skin Match)

This document describes the REST API used by the Next.js frontend.

## Base URL (local)
- Backend: `http://localhost:8080`
- Frontend (Next.js): `http://localhost:3000`

## CORS (local development)
CORS is configured to allow requests from:
- `http://localhost:3000`

No cookies/sessions are used, so `credentials` are not required.

---

## Endpoints

### 1) Get all skin types
**GET** `/skin-match/skin-types`

#### Query params
None

#### Response 200 (JSON)
```json
[
  {
    "label": "DRY",
    "types": "Dry skin",
    "description": "Often feels tight, may flake, needs more moisture."
  },
  {
    "label": "OILY",
    "types": "Oily skin",
    "description": "More sebum production, can look shiny, prone to clogged pores."
  }
]
```
---

### 2) Get ingredients by skin type (with search + pagination)
***GET*** `/skin-match/ingredients`

#### Query params
* skinType (required) — one of: NORMAL, DRY, OILY, COMBINATION, SENSITIVE
* search (optional) — text search in ingredient name and description
* page (optional, default 0) — page index (0-based)
* size (optional, default 15) — page size (max 50 enforced by backend)
* sort (optional) — supported by Spring Pageable (default is inciName)


#### Example request:
`/skin-match/ingredients?skinType=DRY&search=acid&page=0&size=15`

#### Response 200 (JSON) — PageResponseDTO<IngredientDTO>
````
{
  "items": [
    {
      "inciName": "Glycerin",
      "description": "A humectant that helps draw water into the skin and supports hydration."
    },
    {
      "inciName": "Niacinamide",
      "description": "Supports skin barrier function and can help improve uneven tone."
    }
  ],
  "page": 0,
  "size": 15,
  "totalItems": 42,
  "totalPages": 3,
  "hasNext": true,
  "hasPrevious": false
}
````

### 3) Error format (ApiErrorDTO)
All errors return JSON with the same structure.

#### Error 400 — missing required query param
````
{
  "timestamp": "2026-01-06T00:10:00.000+01:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Missing required parameter: skinType",
  "path": "/skin-match/ingredients",
  "violations": []
}
````

#### Error 400 — invalid skinType
````
{
  "timestamp": "2026-01-06T00:10:00.000+01:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid skinType: INVALID",
  "path": "/skin-match/ingredients",
  "violations": []
}
````


#### Error 404 — endpoint not found
````
{
  "timestamp": "2026-01-06T00:10:00.000+01:00",
  "status": 404,
  "error": "Not Found",
  "message": "Endpoint not found",
  "path": "/skin-match/unknown-endpoint",
  "violations": []
}
````
