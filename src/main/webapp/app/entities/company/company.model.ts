export interface ICompany {
  id: number;
  name?: string | null;
  phone?: string | null;
  siret?: number | null;
  address?: string | null;
  email?: string | null;
  contactPerson?: string | null;
  contactPersonPhone?: string | null;
  contactPersonEmail?: string | null;
  size?: string | null;
  notes?: string | null;
}

export type NewCompany = Omit<ICompany, 'id'> & { id: null };
