import { ICompany, NewCompany } from './company.model';

export const sampleWithRequiredData: ICompany = {
  id: 22832,
  name: 'assez',
  phone: '+33 228254401',
  siret: 'équipe de rech',
  address: 'tchou tchouu',
};

export const sampleWithPartialData: ICompany = {
  id: 8243,
  name: 'aïe',
  phone: '0327681953',
  siret: 'complètement c',
  address: 'secours guide glouglou',
  email: 'Alphonsine.Sanchez@gmail.com',
  contactPersonPhone: 'établir puisque',
  contactPersonEmail: 'sans que crac',
  size: 'quasi conseil municipal',
};

export const sampleWithFullData: ICompany = {
  id: 13867,
  name: 'afin que',
  phone: '+33 479849020',
  siret: 'à côté deXXXXX',
  address: 'spécialiste gens',
  email: 'Merlin.Adam@yahoo.fr',
  contactPerson: 'désirer comme',
  contactPersonPhone: 'apercevoir tria',
  contactPersonEmail: 'y de peur de pour que',
  size: 'étrangler du fait que',
  notes: 'cependant au-dessus de',
};

export const sampleWithNewData: NewCompany = {
  name: 'horrible simple',
  phone: '+33 782369891',
  siret: 'au cas où fort',
  address: 'bzzz',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
