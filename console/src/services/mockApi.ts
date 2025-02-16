import type { User, Org } from './types';

let users: User[] = [];
let orgs: Org[] = [];

export const mockApi = {
  signIn: async (email: string, password: string) => {
    const user = users.find(u => u.email === email && u.password === password);
    if (!user) throw new Error('Invalid credentials');
    return user;
  },
  signUp: async (email: string, password: string) => {
    const user = { id: Date.now().toString(), email, password };
    users.push(user);
    return user;
  },
  createOrg: async (name: string, ownerId: string) => {
    const org = { id: Date.now().toString(), name, ownerId };
    orgs.push(org);
    return org;
  },
  getOrgs: async (ownerId: string) => {
    return orgs.filter(org => org.ownerId === ownerId);
  },
  // Add more mock methods as needed
}; 