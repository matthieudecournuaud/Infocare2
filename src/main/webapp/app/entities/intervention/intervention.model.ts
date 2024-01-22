import dayjs from 'dayjs/esm';
import { IProcedure } from 'app/entities/procedure/procedure.model';
import { ITicket } from 'app/entities/ticket/ticket.model';

export interface IIntervention {
  id: number;
  title?: string | null;
  description?: string | null;
  createdBy?: string | null;
  createdAt?: dayjs.Dayjs | null;
  attachments?: string | null;
  notes?: string | null;
  procedure?: IProcedure | null;
  ticket?: ITicket | null;
}

export type NewIntervention = Omit<IIntervention, 'id'> & { id: null };
