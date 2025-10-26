# CSP (Content Security Policy) Fix

## Problem
The frontend Angular app was blocked by overly restrictive CSP headers from the backend.

## Solution Applied
Updated `SecurityHeadersConfig.java` to allow:

1. **Angular inline styles**: `'unsafe-inline'` in `style-src`
2. **Angular scripts**: `'unsafe-inline'` and `'unsafe-eval'` in `script-src`  
3. **Razorpay integration**: Added `https://checkout.razorpay.com` and `https://api.razorpay.com`
4. **Frame embedding**: Changed from `DENY` to `SAMEORIGIN`

## New CSP Policy
```
default-src 'self';
script-src 'self' 'unsafe-inline' 'unsafe-eval' https://checkout.razorpay.com;
style-src 'self' 'unsafe-inline';
img-src 'self' data: https:;
font-src 'self' data:;
connect-src 'self' https://api.razorpay.com;
frame-src 'self' https://api.razorpay.com
```

## CORS Configuration
Added `CorsConfig.java` to allow frontend-backend communication on localhost.

## Result
- ✅ Angular app loads without CSP errors
- ✅ Razorpay integration works
- ✅ Inline styles and scripts allowed
- ✅ Frontend can communicate with backend API

## Security Note
This configuration is suitable for development. For production:
- Use nonces instead of `'unsafe-inline'`
- Restrict origins to specific domains
- Consider using a reverse proxy for additional security