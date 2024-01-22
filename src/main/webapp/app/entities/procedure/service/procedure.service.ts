import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProcedure, NewProcedure } from '../procedure.model';

export type PartialUpdateProcedure = Partial<IProcedure> & Pick<IProcedure, 'id'>;

type RestOf<T extends IProcedure | NewProcedure> = Omit<T, 'lastReviewed'> & {
  lastReviewed?: string | null;
};

export type RestProcedure = RestOf<IProcedure>;

export type NewRestProcedure = RestOf<NewProcedure>;

export type PartialUpdateRestProcedure = RestOf<PartialUpdateProcedure>;

export type EntityResponseType = HttpResponse<IProcedure>;
export type EntityArrayResponseType = HttpResponse<IProcedure[]>;

@Injectable({ providedIn: 'root' })
export class ProcedureService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/procedures');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(procedure: NewProcedure): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(procedure);
    return this.http
      .post<RestProcedure>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(procedure: IProcedure): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(procedure);
    return this.http
      .put<RestProcedure>(`${this.resourceUrl}/${this.getProcedureIdentifier(procedure)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(procedure: PartialUpdateProcedure): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(procedure);
    return this.http
      .patch<RestProcedure>(`${this.resourceUrl}/${this.getProcedureIdentifier(procedure)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProcedure>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProcedure[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProcedureIdentifier(procedure: Pick<IProcedure, 'id'>): number {
    return procedure.id;
  }

  compareProcedure(o1: Pick<IProcedure, 'id'> | null, o2: Pick<IProcedure, 'id'> | null): boolean {
    return o1 && o2 ? this.getProcedureIdentifier(o1) === this.getProcedureIdentifier(o2) : o1 === o2;
  }

  addProcedureToCollectionIfMissing<Type extends Pick<IProcedure, 'id'>>(
    procedureCollection: Type[],
    ...proceduresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const procedures: Type[] = proceduresToCheck.filter(isPresent);
    if (procedures.length > 0) {
      const procedureCollectionIdentifiers = procedureCollection.map(procedureItem => this.getProcedureIdentifier(procedureItem)!);
      const proceduresToAdd = procedures.filter(procedureItem => {
        const procedureIdentifier = this.getProcedureIdentifier(procedureItem);
        if (procedureCollectionIdentifiers.includes(procedureIdentifier)) {
          return false;
        }
        procedureCollectionIdentifiers.push(procedureIdentifier);
        return true;
      });
      return [...proceduresToAdd, ...procedureCollection];
    }
    return procedureCollection;
  }

  protected convertDateFromClient<T extends IProcedure | NewProcedure | PartialUpdateProcedure>(procedure: T): RestOf<T> {
    return {
      ...procedure,
      lastReviewed: procedure.lastReviewed?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restProcedure: RestProcedure): IProcedure {
    return {
      ...restProcedure,
      lastReviewed: restProcedure.lastReviewed ? dayjs(restProcedure.lastReviewed) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProcedure>): HttpResponse<IProcedure> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProcedure[]>): HttpResponse<IProcedure[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
