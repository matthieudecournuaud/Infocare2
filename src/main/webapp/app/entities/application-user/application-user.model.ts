import { IUser } from 'app/entities/user/user.model';
import { ITicket } from 'app/entities/ticket/ticket.model';

export interface IApplicationUser {
  id: number;
  phoneNumber?: string | null;
  location?: string | null;
  avatar?: string | null;
  notes?: string | null;
  user?: Pick<IUser, 'id'> | null;
  tickets?: ITicket[] | null;
}

export type NewApplicationUser = Omit<IApplicationUser, 'id'> & { id: null };
