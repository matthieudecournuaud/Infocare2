import dayjs from 'dayjs/esm';

export interface IProcedure {
  id: number;
  name?: string | null;
  description?: string | null;
  category?: string | null;
  procedureId?: number | null;
  stepByStepGuide?: string | null;
  estimatedTime?: number | null;
  requiredTools?: string | null;
  skillsRequired?: string | null;
  safetyInstructions?: string | null;
  lastReviewed?: dayjs.Dayjs | null;
  reviewedBy?: string | null;
  attachments?: string | null;
}

export type NewProcedure = Omit<IProcedure, 'id'> & { id: null };
