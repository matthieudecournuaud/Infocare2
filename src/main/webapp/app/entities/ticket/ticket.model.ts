import dayjs from 'dayjs/esm';
import { IMaterial } from 'app/entities/material/material.model';
import { IComment } from 'app/entities/comment/comment.model';
import { IIntervention } from 'app/entities/intervention/intervention.model';
import { ICategory } from 'app/entities/category/category.model';
import { IStatus } from 'app/entities/status/status.model';
import { IPriority } from 'app/entities/priority/priority.model';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';

export interface ITicket {
  id: number;
  title?: string | null;
  description?: string | null;
  createdAt?: dayjs.Dayjs | null;
  resolutionDate?: dayjs.Dayjs | null;
  closedAt?: dayjs.Dayjs | null;
  limitDate?: dayjs.Dayjs | null;
  impact?: string | null;
  resolution?: string | null;
  attachments?: string | null;
  materials?: IMaterial[] | null;
  comments?: IComment[] | null;
  interventions?: IIntervention[] | null;
  category?: ICategory | null;
  status?: IStatus | null;
  priority?: IPriority | null;
  applicationUsers?: IApplicationUser[] | null;
}

export type NewTicket = Omit<ITicket, 'id'> & { id: null };
