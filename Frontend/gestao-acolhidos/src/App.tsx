import './global.css'
import RoutesReference from "./routes/Routes.tsx";
import { AuthProvider } from "./context/authContext/authContext.tsx";

function App() {

  return (
    <>
    <AuthProvider>
      <RoutesReference />
    </AuthProvider>
    </>
  )
}

export default App
