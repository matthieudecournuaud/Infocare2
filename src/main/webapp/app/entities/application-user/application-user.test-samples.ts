import { IApplicationUser, NewApplicationUser } from './application-user.model';

export const sampleWithRequiredData: IApplicationUser = {
  id: 3865,
};

export const sampleWithPartialData: IApplicationUser = {
  id: 18982,
  phoneNumber: 'personnel profession',
  location: 'sombre',
  avatar: 'partout',
  notes: 'manger psitt en bas de',
};

export const sampleWithFullData: IApplicationUser = {
  id: 16104,
  phoneNumber: 'si gai spécialiste',
  location: 'au-dessous de corps enseignant',
  avatar: 'pin-pon',
  notes: 'concernant',
};

export const sampleWithNewData: NewApplicationUser = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
