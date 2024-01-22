import { IPriority, NewPriority } from './priority.model';

export const sampleWithRequiredData: IPriority = {
  id: 23544,
  name: 'alentour pourpre vide',
};

export const sampleWithPartialData: IPriority = {
  id: 23440,
  name: 'd’autant que naguère',
  description: 'quelque',
  colorCode: 'toujour',
};

export const sampleWithFullData: IPriority = {
  id: 26951,
  name: 'immense oups',
  description: 'aussitôt que quitte à',
  colorCode: 'pin-pon',
};

export const sampleWithNewData: NewPriority = {
  name: 'rose bientôt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
