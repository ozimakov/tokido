import React, { useState, useEffect } from 'react';
import { Container, Button } from '@mantine/core';
import { mockApi } from '../services/mockApi';
import type { Org } from '../services/types';

const Dashboard: React.FC = () => {
  const [orgs, setOrgs] = useState<Org[]>([]);

  useEffect(() => {
    const fetchOrgs = async () => {
      const userOrgs = await mockApi.getOrgs('currentUserId'); // Replace with actual user ID
      setOrgs(userOrgs);
    };
    fetchOrgs();
  }, []);

  const handleCreateOrg = async () => {
    const newOrg = await mockApi.createOrg('New Org', 'currentUserId'); // Replace with actual user ID
    setOrgs([...orgs, newOrg]);
  };

  return (
    <Container>
      <h1>Dashboard</h1>
      <Button onClick={handleCreateOrg}>Create Org</Button>
      <ul>
        {orgs.map((org) => (
          <li key={org.id}>{org.name}</li>
        ))}
      </ul>
    </Container>
  );
};

export default Dashboard; 