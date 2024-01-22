import dayjs from 'dayjs/esm';

import { IProcedure, NewProcedure } from './procedure.model';

export const sampleWithRequiredData: IProcedure = {
  id: 12113,
};

export const sampleWithPartialData: IProcedure = {
  id: 14986,
  description: 'biathlète',
  category: 'coac coac lorsque',
  requiredTools: 'négliger',
  skillsRequired: 'déjeuner au point que attendrir',
  reviewedBy: 'prou moyennant police',
  attachments: 'adepte aussitôt que ah',
};

export const sampleWithFullData: IProcedure = {
  id: 28607,
  name: 'au défaut de',
  description: 'touriste tic-tac',
  category: 'miam patientèle communauté étudiante',
  procedureId: 29464,
  stepByStepGuide: 'aïe durant',
  estimatedTime: 31325,
  requiredTools: 'tant que incognito',
  skillsRequired: 'alimenter trier comme',
  safetyInstructions: 'étranger imposer crac',
  lastReviewed: dayjs('2024-01-21'),
  reviewedBy: 'au-delà peut-être',
  attachments: 'au cas où',
};

export const sampleWithNewData: NewProcedure = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
