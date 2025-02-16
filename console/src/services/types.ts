export type User = {
  id: string;
  email: string;
  password: string;
};

export type Org = {
  id: string;
  name: string;
  ownerId: string;
}; 