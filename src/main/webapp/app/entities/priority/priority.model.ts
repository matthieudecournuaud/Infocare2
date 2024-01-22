export interface IPriority {
  id: number;
  name?: string | null;
  description?: string | null;
  colorCode?: string | null;
}

export type NewPriority = Omit<IPriority, 'id'> & { id: null };
