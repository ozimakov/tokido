import React from 'react';
import { MantineProvider, Container } from '@mantine/core';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './components/Home';
import Dashboard from './components/Dashboard';

function App() {
  return (
    <MantineProvider>
      <Router>
        <Container>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/dashboard" element={<Dashboard />} />
          </Routes>
        </Container>
      </Router>
    </MantineProvider>
  );
}

export default App; 