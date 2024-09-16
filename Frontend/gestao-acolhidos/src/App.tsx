import './global.css'
import RoutesReference from "./routes/Routes.tsx";
import { AuthProvider } from "./context/authContext/authContext.tsx";
import { TableDataProvider } from './context/tableData/tableData.tsx';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';

const queryClient = new QueryClient();

function App() {

  return (
    <>
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <TableDataProvider>
          <RoutesReference />
        </TableDataProvider>
      </AuthProvider>
    </QueryClientProvider>
    </>
  )
}

export default App
