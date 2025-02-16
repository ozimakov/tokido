import React from 'react';
import { Container, Title, Button } from '@mantine/core';
import { Link } from 'react-router-dom';

const Home: React.FC = () => {
  return (
    <Container>
      <Title>Welcome to Tokido</Title>
      <Button component={Link} to="/signin">Sign In</Button>
      <Button component={Link} to="/signup">Sign Up</Button>
    </Container>
  );
};

export default Home; 