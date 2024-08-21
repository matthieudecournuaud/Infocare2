import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { map, Observable } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIntervention, NewIntervention } from '../intervention.model';

export type PartialUpdateIntervention = Partial<IIntervention> & Pick<IIntervention, 'id'>;

type RestOf<T extends IIntervention | NewIntervention> = Omit<T, 'createdAt'> & {
  createdAt?: string | null;
};

export type RestIntervention = RestOf<IIntervention>;

export type NewRestIntervention = RestOf<NewIntervention>;

export type PartialUpdateRestIntervention = RestOf<PartialUpdateIntervention>;

export type EntityResponseType = HttpResponse<IIntervention>;
export type EntityArrayResponseType = HttpResponse<IIntervention[]>;

@Injectable({ providedIn: 'root' })
export class InterventionService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/interventions');

  create(intervention: NewIntervention): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(intervention);
    return this.http
      .post<RestIntervention>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(intervention: IIntervention): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(intervention);
    return this.http
      .put<RestIntervention>(`${this.resourceUrl}/${this.getInterventionIdentifier(intervention)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(intervention: PartialUpdateIntervention): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(intervention);
    return this.http
      .patch<RestIntervention>(`${this.resourceUrl}/${this.getInterventionIdentifier(intervention)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestIntervention>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestIntervention[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInterventionIdentifier(intervention: Pick<IIntervention, 'id'>): number {
    return intervention.id;
  }

  compareIntervention(o1: Pick<IIntervention, 'id'> | null, o2: Pick<IIntervention, 'id'> | null): boolean {
    return o1 && o2 ? this.getInterventionIdentifier(o1) === this.getInterventionIdentifier(o2) : o1 === o2;
  }

  addInterventionToCollectionIfMissing<Type extends Pick<IIntervention, 'id'>>(
    interventionCollection: Type[],
    ...interventionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const interventions: Type[] = interventionsToCheck.filter(isPresent);
    if (interventions.length > 0) {
      const interventionCollectionIdentifiers = interventionCollection.map(interventionItem =>
        this.getInterventionIdentifier(interventionItem),
      );
      const interventionsToAdd = interventions.filter(interventionItem => {
        const interventionIdentifier = this.getInterventionIdentifier(interventionItem);
        if (interventionCollectionIdentifiers.includes(interventionIdentifier)) {
          return false;
        }
        interventionCollectionIdentifiers.push(interventionIdentifier);
        return true;
      });
      return [...interventionsToAdd, ...interventionCollection];
    }
    return interventionCollection;
  }

  protected convertDateFromClient<T extends IIntervention | NewIntervention | PartialUpdateIntervention>(intervention: T): RestOf<T> {
    return {
      ...intervention,
      createdAt: intervention.createdAt?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restIntervention: RestIntervention): IIntervention {
    return {
      ...restIntervention,
      createdAt: restIntervention.createdAt ? dayjs(restIntervention.createdAt) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestIntervention>): HttpResponse<IIntervention> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestIntervention[]>): HttpResponse<IIntervention[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
