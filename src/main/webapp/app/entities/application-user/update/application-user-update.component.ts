import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ITicket } from 'app/entities/ticket/ticket.model';
import { TicketService } from 'app/entities/ticket/service/ticket.service';
import { ApplicationUserService } from '../service/application-user.service';
import { IApplicationUser } from '../application-user.model';
import { ApplicationUserFormService, ApplicationUserFormGroup } from './application-user-form.service';

@Component({
  standalone: true,
  selector: 'jhi-application-user-update',
  templateUrl: './application-user-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ApplicationUserUpdateComponent implements OnInit {
  isSaving = false;
  applicationUser: IApplicationUser | null = null;

  usersSharedCollection: IUser[] = [];
  ticketsSharedCollection: ITicket[] = [];

  editForm: ApplicationUserFormGroup = this.applicationUserFormService.createApplicationUserFormGroup();

  constructor(
    protected applicationUserService: ApplicationUserService,
    protected applicationUserFormService: ApplicationUserFormService,
    protected userService: UserService,
    protected ticketService: TicketService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareTicket = (o1: ITicket | null, o2: ITicket | null): boolean => this.ticketService.compareTicket(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicationUser }) => {
      this.applicationUser = applicationUser;
      if (applicationUser) {
        this.updateForm(applicationUser);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const applicationUser = this.applicationUserFormService.getApplicationUser(this.editForm);
    if (applicationUser.id !== null) {
      this.subscribeToSaveResponse(this.applicationUserService.update(applicationUser));
    } else {
      this.subscribeToSaveResponse(this.applicationUserService.create(applicationUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IApplicationUser>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(applicationUser: IApplicationUser): void {
    this.applicationUser = applicationUser;
    this.applicationUserFormService.resetForm(this.editForm, applicationUser);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, applicationUser.user);
    this.ticketsSharedCollection = this.ticketService.addTicketToCollectionIfMissing<ITicket>(
      this.ticketsSharedCollection,
      ...(applicationUser.tickets ?? []),
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.applicationUser?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.ticketService
      .query()
      .pipe(map((res: HttpResponse<ITicket[]>) => res.body ?? []))
      .pipe(
        map((tickets: ITicket[]) =>
          this.ticketService.addTicketToCollectionIfMissing<ITicket>(tickets, ...(this.applicationUser?.tickets ?? [])),
        ),
      )
      .subscribe((tickets: ITicket[]) => (this.ticketsSharedCollection = tickets));
  }
}
