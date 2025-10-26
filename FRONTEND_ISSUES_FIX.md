# Frontend Issues Fix

## Issues Found:
1. **CSP blocking phone.email script**
2. **API path duplication** (`/api/api/astrologers`)
3. **403 Authentication errors**
4. **Missing favicon**

## Fixes Applied:

### 1. Updated CSP Policy
Added `https://www.phone.email` to:
- `script-src` - Allow phone.email authentication script
- `connect-src` - Allow API calls to phone.email
- `frame-src` - Allow phone.email iframes

### 2. API Path Issues
The frontend is calling `/api/api/astrologers` (double `/api/`).

**Frontend Fix Needed:**
Check your Angular service files and remove duplicate `/api/` in URLs:
```typescript
// Wrong
this.http.get('/api/api/astrologers/available')

// Correct  
this.http.get('/api/astrologers/available')
```

### 3. Authentication Issues (403 errors)
The API requires JWT tokens. Frontend needs to:
- Login first to get JWT token
- Include token in Authorization header
- Handle token expiration

### 4. Missing Favicon
Add `favicon.ico` to backend `src/main/resources/static/` folder.

## Quick Test:
1. **Backend**: http://localhost:5000/api/auth/test
2. **Login**: POST to http://localhost:5000/api/auth/login
3. **Use JWT token** in subsequent requests

## Frontend Service Example:
```typescript
// Add JWT token to requests
const headers = {
  'Authorization': `Bearer ${this.getToken()}`
};
this.http.get('/api/astrologers/available', { headers })
```