import dayjs from 'dayjs/esm';
import { ICompany } from 'app/entities/company/company.model';
import { ITicket } from 'app/entities/ticket/ticket.model';

export interface IMaterial {
  id: number;
  name?: string | null;
  type?: string | null;
  purchaseDate?: dayjs.Dayjs | null;
  warrantyEndDate?: dayjs.Dayjs | null;
  manufacturer?: string | null;
  model?: string | null;
  statusMaterial?: string | null;
  lastMaintenanceDate?: dayjs.Dayjs | null;
  note?: string | null;
  serialNumber?: number | null;
  company?: ICompany | null;
  ticket?: ITicket | null;
}

export type NewMaterial = Omit<IMaterial, 'id'> & { id: null };
