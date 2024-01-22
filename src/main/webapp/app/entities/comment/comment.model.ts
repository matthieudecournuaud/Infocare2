import dayjs from 'dayjs/esm';
import { ITicket } from 'app/entities/ticket/ticket.model';

export interface IComment {
  id: number;
  title?: string | null;
  type?: string | null;
  visibility?: string | null;
  description?: string | null;
  editedBy?: string | null;
  editedAt?: dayjs.Dayjs | null;
  attachments?: string | null;
  responseToCommentId?: number | null;
  ticket?: ITicket | null;
}

export type NewComment = Omit<IComment, 'id'> & { id: null };
