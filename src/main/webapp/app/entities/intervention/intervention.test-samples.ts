import dayjs from 'dayjs/esm';

import { IIntervention, NewIntervention } from './intervention.model';

export const sampleWithRequiredData: IIntervention = {
  id: 9125,
  createdBy: 'ouah',
  createdAt: dayjs('2024-01-22'),
};

export const sampleWithPartialData: IIntervention = {
  id: 11565,
  title: 'danser membre titulaire conseil municipal',
  createdBy: 'bang ouf',
  createdAt: dayjs('2024-01-21'),
  notes: 'déborder dring',
};

export const sampleWithFullData: IIntervention = {
  id: 14440,
  title: 'pauvre si bien que infime',
  description: 'avant en dedans de',
  createdBy: 'après-demain franco grrr',
  createdAt: dayjs('2024-01-22'),
  attachments: 'raisonner en faveur de',
  notes: 'à la merci atchoum au défaut de',
};

export const sampleWithNewData: NewIntervention = {
  createdBy: 'si parmi',
  createdAt: dayjs('2024-01-21'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
