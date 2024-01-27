import dayjs from 'dayjs/esm';

import { IMaterial, NewMaterial } from './material.model';

export const sampleWithRequiredData: IMaterial = {
  id: 19577,
  name: 'étrangler repentir',
  type: 'd’autant que parce que',
};

export const sampleWithPartialData: IMaterial = {
  id: 9136,
  name: 'administration pin-pon',
  type: 'encourager rectorat pacifique',
  purchaseDate: dayjs('2024-01-21'),
  manufacturer: 'à peine jusqu’à ce que',
  model: 'dessous',
  statusMaterial: 'tchou tchouu maigre plouf',
  note: 'puisque',
};

export const sampleWithFullData: IMaterial = {
  id: 2747,
  name: 'absolument',
  type: 'administration déranger alentour',
  purchaseDate: dayjs('2024-01-22'),
  warrantyEndDate: dayjs('2024-01-22'),
  manufacturer: 'encore délectable soit',
  model: 'maintenant hier',
  statusMaterial: 'sympathique ferme sans',
  lastMaintenanceDate: dayjs('2024-01-22'),
  note: 'antagoniste alors que au moyen de',
  serialNumber: 14925,
};

export const sampleWithNewData: NewMaterial = {
  name: 'glouglou simple',
  type: 'déjà',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
