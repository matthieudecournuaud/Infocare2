import dayjs from 'dayjs/esm';

import { ITicket, NewTicket } from './ticket.model';

export const sampleWithRequiredData: ITicket = {
  id: 2486,
  title: 'séparer',
  description: 'mince',
  createdAt: dayjs('2024-01-21'),
};

export const sampleWithPartialData: ITicket = {
  id: 14156,
  title: 'environ',
  description: 'hirsute rectorat',
  createdAt: dayjs('2024-01-22'),
  resolutionDate: dayjs('2024-01-22'),
  closedAt: dayjs('2024-01-22'),
  limitDate: dayjs('2024-01-22'),
  attachments: 'auprès de oh multiple',
};

export const sampleWithFullData: ITicket = {
  id: 15857,
  title: 'grandement tant que aider',
  description: 'raide pas mal à la merci',
  createdAt: dayjs('2024-01-22'),
  resolutionDate: dayjs('2024-01-22'),
  closedAt: dayjs('2024-01-21'),
  limitDate: dayjs('2024-01-22'),
  impact: 'sitôt que près',
  resolution: 'de manière à ce que répondre',
  attachments: 'sauf à lors',
};

export const sampleWithNewData: NewTicket = {
  title: 'sans doute',
  description: 'mettre sage',
  createdAt: dayjs('2024-01-21'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
