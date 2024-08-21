import { IUser } from 'app/entities/user/user.model';

export interface IApplicationUser {
  id: number;
  phoneNumber?: string | null;
  location?: string | null;
  avatar?: string | null;
  notes?: string | null;
  user?: Pick<IUser, 'id'> | null;
}

export type NewApplicationUser = Omit<IApplicationUser, 'id'> & { id: null };
