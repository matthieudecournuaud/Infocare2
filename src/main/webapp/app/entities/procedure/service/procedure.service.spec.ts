import { TestBed } from '@angular/core/testing';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IProcedure } from '../procedure.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../procedure.test-samples';

import { ProcedureService, RestProcedure } from './procedure.service';

const requireRestSample: RestProcedure = {
  ...sampleWithRequiredData,
  lastReviewed: sampleWithRequiredData.lastReviewed?.format(DATE_FORMAT),
};

describe('Procedure Service', () => {
  let service: ProcedureService;
  let httpMock: HttpTestingController;
  let expectedResult: IProcedure | IProcedure[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ProcedureService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Procedure', () => {
      const procedure = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(procedure).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Procedure', () => {
      const procedure = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(procedure).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Procedure', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Procedure', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Procedure', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProcedureToCollectionIfMissing', () => {
      it('should add a Procedure to an empty array', () => {
        const procedure: IProcedure = sampleWithRequiredData;
        expectedResult = service.addProcedureToCollectionIfMissing([], procedure);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(procedure);
      });

      it('should not add a Procedure to an array that contains it', () => {
        const procedure: IProcedure = sampleWithRequiredData;
        const procedureCollection: IProcedure[] = [
          {
            ...procedure,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProcedureToCollectionIfMissing(procedureCollection, procedure);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Procedure to an array that doesn't contain it", () => {
        const procedure: IProcedure = sampleWithRequiredData;
        const procedureCollection: IProcedure[] = [sampleWithPartialData];
        expectedResult = service.addProcedureToCollectionIfMissing(procedureCollection, procedure);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(procedure);
      });

      it('should add only unique Procedure to an array', () => {
        const procedureArray: IProcedure[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const procedureCollection: IProcedure[] = [sampleWithRequiredData];
        expectedResult = service.addProcedureToCollectionIfMissing(procedureCollection, ...procedureArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const procedure: IProcedure = sampleWithRequiredData;
        const procedure2: IProcedure = sampleWithPartialData;
        expectedResult = service.addProcedureToCollectionIfMissing([], procedure, procedure2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(procedure);
        expect(expectedResult).toContain(procedure2);
      });

      it('should accept null and undefined values', () => {
        const procedure: IProcedure = sampleWithRequiredData;
        expectedResult = service.addProcedureToCollectionIfMissing([], null, procedure, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(procedure);
      });

      it('should return initial array if no Procedure is added', () => {
        const procedureCollection: IProcedure[] = [sampleWithRequiredData];
        expectedResult = service.addProcedureToCollectionIfMissing(procedureCollection, undefined, null);
        expect(expectedResult).toEqual(procedureCollection);
      });
    });

    describe('compareProcedure', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProcedure(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProcedure(entity1, entity2);
        const compareResult2 = service.compareProcedure(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProcedure(entity1, entity2);
        const compareResult2 = service.compareProcedure(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProcedure(entity1, entity2);
        const compareResult2 = service.compareProcedure(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
