import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITicket, NewTicket } from '../ticket.model';

export type PartialUpdateTicket = Partial<ITicket> & Pick<ITicket, 'id'>;

type RestOf<T extends ITicket | NewTicket> = Omit<T, 'createdAt' | 'resolutionDate' | 'closedAt' | 'limitDate'> & {
  createdAt?: string | null;
  resolutionDate?: string | null;
  closedAt?: string | null;
  limitDate?: string | null;
};

export type RestTicket = RestOf<ITicket>;

export type NewRestTicket = RestOf<NewTicket>;

export type PartialUpdateRestTicket = RestOf<PartialUpdateTicket>;

export type EntityResponseType = HttpResponse<ITicket>;
export type EntityArrayResponseType = HttpResponse<ITicket[]>;

@Injectable({ providedIn: 'root' })
export class TicketService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/tickets');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(ticket: NewTicket): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ticket);
    return this.http
      .post<RestTicket>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ticket: ITicket): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ticket);
    return this.http
      .put<RestTicket>(`${this.resourceUrl}/${this.getTicketIdentifier(ticket)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ticket: PartialUpdateTicket): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ticket);
    return this.http
      .patch<RestTicket>(`${this.resourceUrl}/${this.getTicketIdentifier(ticket)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestTicket>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  queryRecent(): Observable<EntityArrayResponseType> {
    return this.http
      .get<ITicket[]>(`${this.resourceUrl}/recent`, { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServerForRecent(res)));
  }

  protected convertResponseArrayFromServerForRecent(res: HttpResponse<ITicket[]>): HttpResponse<ITicket[]> {
    return res.clone({
      body: res.body ? res.body.map(ticket => this.convertDateFromServer(ticket as any)) : null,
    });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestTicket[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTicketIdentifier(ticket: Pick<ITicket, 'id'>): number {
    return ticket.id;
  }

  compareTicket(o1: Pick<ITicket, 'id'> | null, o2: Pick<ITicket, 'id'> | null): boolean {
    return o1 && o2 ? this.getTicketIdentifier(o1) === this.getTicketIdentifier(o2) : o1 === o2;
  }

  addTicketToCollectionIfMissing<Type extends Pick<ITicket, 'id'>>(
    ticketCollection: Type[],
    ...ticketsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const tickets: Type[] = ticketsToCheck.filter(isPresent);
    if (tickets.length > 0) {
      const ticketCollectionIdentifiers = ticketCollection.map(ticketItem => this.getTicketIdentifier(ticketItem)!);
      const ticketsToAdd = tickets.filter(ticketItem => {
        const ticketIdentifier = this.getTicketIdentifier(ticketItem);
        if (ticketCollectionIdentifiers.includes(ticketIdentifier)) {
          return false;
        }
        ticketCollectionIdentifiers.push(ticketIdentifier);
        return true;
      });
      return [...ticketsToAdd, ...ticketCollection];
    }
    return ticketCollection;
  }

  protected convertDateFromClient<T extends ITicket | NewTicket | PartialUpdateTicket>(ticket: T): RestOf<T> {
    return {
      ...ticket,
      createdAt: ticket.createdAt?.format(DATE_FORMAT) ?? null,
      resolutionDate: ticket.resolutionDate?.format(DATE_FORMAT) ?? null,
      closedAt: ticket.closedAt?.format(DATE_FORMAT) ?? null,
      limitDate: ticket.limitDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restTicket: RestTicket): ITicket {
    return {
      ...restTicket,
      createdAt: restTicket.createdAt ? dayjs(restTicket.createdAt) : undefined,
      resolutionDate: restTicket.resolutionDate ? dayjs(restTicket.resolutionDate) : undefined,
      closedAt: restTicket.closedAt ? dayjs(restTicket.closedAt) : undefined,
      limitDate: restTicket.limitDate ? dayjs(restTicket.limitDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestTicket>): HttpResponse<ITicket> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestTicket[]>): HttpResponse<ITicket[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
