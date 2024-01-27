import { IMaterial } from 'app/entities/material/material.model';

export interface ICompany {
  id: number;
  name?: string | null;
  phone?: string | null;
  siret?: string | null;
  address?: string | null;
  email?: string | null;
  contactPerson?: string | null;
  contactPersonPhone?: string | null;
  contactPersonEmail?: string | null;
  size?: string | null;
  notes?: string | null;
  materials?: IMaterial[] | null;
}

export type NewCompany = Omit<ICompany, 'id'> & { id: null };
