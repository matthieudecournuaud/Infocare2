import { ICategory, NewCategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: 16958,
  name: 'identifier sous couleur de',
};

export const sampleWithPartialData: ICategory = {
  id: 10048,
  name: 'police si bien que hi',
  icon: 'tout',
};

export const sampleWithFullData: ICategory = {
  id: 1907,
  name: 'à travers',
  description: 'parce que oh',
  icon: 'meubler rentrer plouf',
};

export const sampleWithNewData: NewCategory = {
  name: 'extrêmement bang construire',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
