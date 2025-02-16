import React, { useState } from 'react';
import { Button, TextInput, Container } from '@mantine/core';
import { mockApi } from '../services/mockApi';

const SignUp: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleSignUp = async () => {
    try {
      const user = await mockApi.signUp(email, password);
      console.log('Signed up user:', user);
      // Redirect to dashboard or show success message
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <Container>
      <TextInput placeholder="Email" value={email} onChange={(e) => setEmail(e.currentTarget.value)} />
      <TextInput placeholder="Password" type="password" value={password} onChange={(e) => setPassword(e.currentTarget.value)} />
      <Button onClick={handleSignUp}>Sign Up</Button>
    </Container>
  );
};

export default SignUp; 