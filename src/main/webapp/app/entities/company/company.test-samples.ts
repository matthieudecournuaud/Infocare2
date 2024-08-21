import { ICompany, NewCompany } from './company.model';

export const sampleWithRequiredData: ICompany = {
  id: 22832,
  name: 'assez',
  phone: '+33 228254401',
  siret: 14,
  address: 'équipe de recherche hors de',
};

export const sampleWithPartialData: ICompany = {
  id: 17420,
  name: 'nourrir clac sauter',
  phone: '+33 753448160',
  siret: 14,
  address: 'animer',
  email: 'Naudet.Petit@yahoo.fr',
  contactPerson: 'cueillir',
  contactPersonEmail: "à l'encontre de",
  size: 'lorsque établir',
};

export const sampleWithFullData: ICompany = {
  id: 4042,
  name: 'tant que',
  phone: '0612543687',
  siret: 14,
  address: 'de manière à ce que',
  email: 'Helene61@hotmail.fr',
  contactPerson: 'pin-pon en guise de communauté étudiante',
  contactPersonPhone: 'à peu près à cô',
  contactPersonEmail: 'gens clientèle désirer',
  size: 'administration apercevoir triangulaire',
  notes: 'y de peur de pour que',
};

export const sampleWithNewData: NewCompany = {
  name: 'étrangler du fait que',
  phone: '0708789454',
  siret: 14,
  address: 'perplexe en outre de',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
