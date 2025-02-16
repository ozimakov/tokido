import React, { useState } from 'react';
import { Button, TextInput, Container } from '@mantine/core';
import { mockApi } from '../services/mockApi';

const SignIn: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleEmailSignIn = async () => {
    try {
      const user = await mockApi.signIn(email, password);
      console.log('Signed in user:', user);
      // Redirect to dashboard
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <Container>
      <TextInput placeholder="Email" value={email} onChange={(e) => setEmail(e.currentTarget.value)} />
      <TextInput placeholder="Password" type="password" value={password} onChange={(e) => setPassword(e.currentTarget.value)} />
      <Button onClick={handleEmailSignIn}>Sign In</Button>
    </Container>
  );
};

export default SignIn; 