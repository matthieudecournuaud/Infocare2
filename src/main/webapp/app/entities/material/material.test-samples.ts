import dayjs from 'dayjs/esm';

import { IMaterial, NewMaterial } from './material.model';

export const sampleWithRequiredData: IMaterial = {
  id: 26509,
  name: 'brusque commis de cuisine',
  type: 'coupable',
};

export const sampleWithPartialData: IMaterial = {
  id: 15846,
  name: 'nonobstant fonctionnaire charitable',
  type: 'incognito',
  warrantyEndDate: dayjs('2024-01-21'),
  model: 'timide considérable étant donné que',
  statusMaterial: 'rattraper aventurer au prix de',
  note: 'malgré cot cot fermer',
  serialNumber: 13148,
};

export const sampleWithFullData: IMaterial = {
  id: 26224,
  name: 'derrière retracer ronron',
  type: 'déboucher encore',
  purchaseDate: dayjs('2024-01-21'),
  warrantyEndDate: dayjs('2024-01-21'),
  manufacturer: 'prout',
  model: "timide vouh d'après",
  statusMaterial: 'trop accrocher au-dedans de',
  lastMaintenanceDate: dayjs('2024-01-22'),
  note: 'interpréter',
  serialNumber: 11264,
};

export const sampleWithNewData: NewMaterial = {
  name: 'glouglou',
  type: 'hypocrite',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
