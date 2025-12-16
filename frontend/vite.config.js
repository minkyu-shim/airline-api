import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    react(),
    tailwindcss(),
  ],
  server: {
    port: 3000,
    proxy: {
      // This matches any request starting with '/api' (like /api/v1/planes)
      '/api': {
        target: 'http://localhost:8080', // Your backend URL
        changeOrigin: true,
        secure: false,
        // ⚠️ IMPORTANT: We do NOT use 'rewrite' here because our
        // backend controller is listening for '/api/v1/...', so we
        // want to send the '/api' part along with the request.
      }
    }
  }
})
