import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IApplicationUserTicket } from '../application-user-ticket.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../application-user-ticket.test-samples';

import { ApplicationUserTicketService } from './application-user-ticket.service';

const requireRestSample: IApplicationUserTicket = {
  ...sampleWithRequiredData,
};

describe('ApplicationUserTicket Service', () => {
  let service: ApplicationUserTicketService;
  let httpMock: HttpTestingController;
  let expectedResult: IApplicationUserTicket | IApplicationUserTicket[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ApplicationUserTicketService);
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

    it('should create a ApplicationUserTicket', () => {
      const applicationUserTicket = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(applicationUserTicket).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ApplicationUserTicket', () => {
      const applicationUserTicket = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(applicationUserTicket).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ApplicationUserTicket', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ApplicationUserTicket', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ApplicationUserTicket', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addApplicationUserTicketToCollectionIfMissing', () => {
      it('should add a ApplicationUserTicket to an empty array', () => {
        const applicationUserTicket: IApplicationUserTicket = sampleWithRequiredData;
        expectedResult = service.addApplicationUserTicketToCollectionIfMissing([], applicationUserTicket);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(applicationUserTicket);
      });

      it('should not add a ApplicationUserTicket to an array that contains it', () => {
        const applicationUserTicket: IApplicationUserTicket = sampleWithRequiredData;
        const applicationUserTicketCollection: IApplicationUserTicket[] = [
          {
            ...applicationUserTicket,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addApplicationUserTicketToCollectionIfMissing(applicationUserTicketCollection, applicationUserTicket);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ApplicationUserTicket to an array that doesn't contain it", () => {
        const applicationUserTicket: IApplicationUserTicket = sampleWithRequiredData;
        const applicationUserTicketCollection: IApplicationUserTicket[] = [sampleWithPartialData];
        expectedResult = service.addApplicationUserTicketToCollectionIfMissing(applicationUserTicketCollection, applicationUserTicket);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(applicationUserTicket);
      });

      it('should add only unique ApplicationUserTicket to an array', () => {
        const applicationUserTicketArray: IApplicationUserTicket[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const applicationUserTicketCollection: IApplicationUserTicket[] = [sampleWithRequiredData];
        expectedResult = service.addApplicationUserTicketToCollectionIfMissing(
          applicationUserTicketCollection,
          ...applicationUserTicketArray,
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const applicationUserTicket: IApplicationUserTicket = sampleWithRequiredData;
        const applicationUserTicket2: IApplicationUserTicket = sampleWithPartialData;
        expectedResult = service.addApplicationUserTicketToCollectionIfMissing([], applicationUserTicket, applicationUserTicket2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(applicationUserTicket);
        expect(expectedResult).toContain(applicationUserTicket2);
      });

      it('should accept null and undefined values', () => {
        const applicationUserTicket: IApplicationUserTicket = sampleWithRequiredData;
        expectedResult = service.addApplicationUserTicketToCollectionIfMissing([], null, applicationUserTicket, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(applicationUserTicket);
      });

      it('should return initial array if no ApplicationUserTicket is added', () => {
        const applicationUserTicketCollection: IApplicationUserTicket[] = [sampleWithRequiredData];
        expectedResult = service.addApplicationUserTicketToCollectionIfMissing(applicationUserTicketCollection, undefined, null);
        expect(expectedResult).toEqual(applicationUserTicketCollection);
      });
    });

    describe('compareApplicationUserTicket', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareApplicationUserTicket(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareApplicationUserTicket(entity1, entity2);
        const compareResult2 = service.compareApplicationUserTicket(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareApplicationUserTicket(entity1, entity2);
        const compareResult2 = service.compareApplicationUserTicket(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareApplicationUserTicket(entity1, entity2);
        const compareResult2 = service.compareApplicationUserTicket(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
