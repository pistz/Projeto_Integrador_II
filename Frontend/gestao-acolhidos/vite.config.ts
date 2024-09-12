import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import dotenv from 'dotenv'

dotenv.config()
// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  define:{
    'process.env.BASE_URL':JSON.stringify(process.env.BASE_URL),
    'process.env.TOKEN_ID':JSON.stringify(process.env.TOKEN_ID)
  }
})