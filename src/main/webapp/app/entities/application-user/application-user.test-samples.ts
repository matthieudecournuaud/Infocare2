import { IApplicationUser, NewApplicationUser } from './application-user.model';

export const sampleWithRequiredData: IApplicationUser = {
  id: 2474,
};

export const sampleWithPartialData: IApplicationUser = {
  id: 13549,
  location: 'inviter',
  avatar: 'lectorat corps enseignant a',
  notes: 'longtemps crac',
};

export const sampleWithFullData: IApplicationUser = {
  id: 16231,
  phoneNumber: 'suggérer',
  location: 'trop turquoise',
  avatar: 'population du Québec même si ronron',
  notes: 'en',
};

export const sampleWithNewData: NewApplicationUser = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
