import { IStatus, NewStatus } from './status.model';

export const sampleWithRequiredData: IStatus = {
  id: 3551,
  name: 'cot cot',
  statusCode: 'pêcher',
};

export const sampleWithPartialData: IStatus = {
  id: 20893,
  name: 'tantôt de manière à ce que anéantir',
  statusCode: 'ha décrire foule',
  colorCode: 'jusque',
  isFinal: true,
};

export const sampleWithFullData: IStatus = {
  id: 19387,
  name: 'prestataire de services',
  statusCode: 'un peu insipide',
  description: 'large',
  colorCode: 'brave s',
  nextPossibleStatus: 'bien que',
  isFinal: false,
};

export const sampleWithNewData: NewStatus = {
  name: 'tant aux environs de',
  statusCode: 'ah',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
