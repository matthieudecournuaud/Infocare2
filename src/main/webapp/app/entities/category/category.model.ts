export interface ICategory {
  id: number;
  name?: string | null;
  description?: string | null;
  icon?: string | null;
}

export type NewCategory = Omit<ICategory, 'id'> & { id: null };
