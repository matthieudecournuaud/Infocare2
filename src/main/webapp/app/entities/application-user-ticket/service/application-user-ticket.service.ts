import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IApplicationUserTicket, NewApplicationUserTicket } from '../application-user-ticket.model';

export type PartialUpdateApplicationUserTicket = Partial<IApplicationUserTicket> & Pick<IApplicationUserTicket, 'id'>;

export type EntityResponseType = HttpResponse<IApplicationUserTicket>;
export type EntityArrayResponseType = HttpResponse<IApplicationUserTicket[]>;

@Injectable({ providedIn: 'root' })
export class ApplicationUserTicketService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/application-user-tickets');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(applicationUserTicket: NewApplicationUserTicket): Observable<EntityResponseType> {
    return this.http.post<IApplicationUserTicket>(this.resourceUrl, applicationUserTicket, { observe: 'response' });
  }

  update(applicationUserTicket: IApplicationUserTicket): Observable<EntityResponseType> {
    return this.http.put<IApplicationUserTicket>(
      `${this.resourceUrl}/${this.getApplicationUserTicketIdentifier(applicationUserTicket)}`,
      applicationUserTicket,
      { observe: 'response' },
    );
  }

  partialUpdate(applicationUserTicket: PartialUpdateApplicationUserTicket): Observable<EntityResponseType> {
    return this.http.patch<IApplicationUserTicket>(
      `${this.resourceUrl}/${this.getApplicationUserTicketIdentifier(applicationUserTicket)}`,
      applicationUserTicket,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IApplicationUserTicket>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IApplicationUserTicket[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getApplicationUserTicketIdentifier(applicationUserTicket: Pick<IApplicationUserTicket, 'id'>): number {
    return applicationUserTicket.id;
  }

  compareApplicationUserTicket(o1: Pick<IApplicationUserTicket, 'id'> | null, o2: Pick<IApplicationUserTicket, 'id'> | null): boolean {
    return o1 && o2 ? this.getApplicationUserTicketIdentifier(o1) === this.getApplicationUserTicketIdentifier(o2) : o1 === o2;
  }

  addApplicationUserTicketToCollectionIfMissing<Type extends Pick<IApplicationUserTicket, 'id'>>(
    applicationUserTicketCollection: Type[],
    ...applicationUserTicketsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const applicationUserTickets: Type[] = applicationUserTicketsToCheck.filter(isPresent);
    if (applicationUserTickets.length > 0) {
      const applicationUserTicketCollectionIdentifiers = applicationUserTicketCollection.map(
        applicationUserTicketItem => this.getApplicationUserTicketIdentifier(applicationUserTicketItem)!,
      );
      const applicationUserTicketsToAdd = applicationUserTickets.filter(applicationUserTicketItem => {
        const applicationUserTicketIdentifier = this.getApplicationUserTicketIdentifier(applicationUserTicketItem);
        if (applicationUserTicketCollectionIdentifiers.includes(applicationUserTicketIdentifier)) {
          return false;
        }
        applicationUserTicketCollectionIdentifiers.push(applicationUserTicketIdentifier);
        return true;
      });
      return [...applicationUserTicketsToAdd, ...applicationUserTicketCollection];
    }
    return applicationUserTicketCollection;
  }
}
