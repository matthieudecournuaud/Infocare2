import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { ITicket } from 'app/entities/ticket/ticket.model';
import { TicketService } from 'app/entities/ticket/service/ticket.service';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  imports: [SharedModule, RouterModule],
})
export class HomeComponent implements OnInit, OnDestroy {
  recentTickets: ITicket[] = [];
  account: Account | null = null;
  showSuccessAlert = true;
  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private ticketService: TicketService,
    private router: Router,
  ) {}
  ngOnInit(): void {
    this.loadRecentTickets();
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => {
        this.account = account;
        this.showSuccessAlert = !!account;
      });
  }
  logout(): void {
    this.accountService.logout();
    this.account = null;
    this.router.navigate(['/login']);
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadRecentTickets() {
    this.ticketService.queryRecent().subscribe({
      next: (res: HttpResponse<ITicket[]>) => {
        this.recentTickets = res.body || [];
      },
      error: (res: HttpErrorResponse) => this.onError(res.message),
    });
  }

  private onError(errorMessage: string): void {
    console.error(errorMessage);
  }
}
