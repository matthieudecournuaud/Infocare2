export interface IApplicationUserTicket {
  id: number;
}

export type NewApplicationUserTicket = Omit<IApplicationUserTicket, 'id'> & { id: null };
