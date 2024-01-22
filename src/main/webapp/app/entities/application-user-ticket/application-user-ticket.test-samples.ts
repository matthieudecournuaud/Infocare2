import { IApplicationUserTicket, NewApplicationUserTicket } from './application-user-ticket.model';

export const sampleWithRequiredData: IApplicationUserTicket = {
  id: 32627,
};

export const sampleWithPartialData: IApplicationUserTicket = {
  id: 31648,
};

export const sampleWithFullData: IApplicationUserTicket = {
  id: 10996,
};

export const sampleWithNewData: NewApplicationUserTicket = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
