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
import Chart from 'chart.js/auto';

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
  resolvedTicketsPercentage: number = 0;

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
        if (account && account.login) {
          this.loadResolvedTicketsPercentage(account.login);
        }
      });
  }

  loadResolvedTicketsPercentage(username: string) {
    this.ticketService.getResolvedTicketsPercentage(username).subscribe(
      percentage => {
        this.resolvedTicketsPercentage = percentage;
        this.updateDoughnutChart();
      },
      error => this.onError(error),
    );
  }

  updateDoughnutChart() {
    const ctxD = document.getElementById('doughnutChart') as HTMLCanvasElement;
    const myLineChart = new Chart(ctxD, {
      type: 'doughnut',
      data: {
        labels: ['Résolus', 'Non Résolus'],
        datasets: [
          {
            data: [this.resolvedTicketsPercentage, 100 - this.resolvedTicketsPercentage],
            backgroundColor: ['#46BFBD', '#F7464A'],
            hoverBackgroundColor: ['#5AD3D1', '#FF5A5E'],
          },
        ],
      },
      options: {
        responsive: true,
        aspectRatio: 2,
        plugins: {
          legend: {
            position: 'bottom',
            labels: {
              padding: 30,
            },
          },
          tooltip: {
            callbacks: {
              label: (context: any) => {
                const labelIndex = context.dataIndex;
                const value = context.parsed;
                return `${value.toFixed(2)}%`;
              },
            },
          },
        },
      },
    });
  }

  initializeDoughnutChart() {
    const ctxD = document.getElementById('doughnutChart') as HTMLCanvasElement;
    const myDoughnutChart = new Chart(ctxD, {
      type: 'doughnut',
      data: {
        labels: ['Résolus', 'Non Résolus'],
        datasets: [
          {
            data: [0, 100], // Initialisez avec 0% résolus et 100% non résolus
            backgroundColor: ['#46BFBD', '#F7464A'],
            hoverBackgroundColor: ['#5AD3D1', '#FF5A5E'],
          },
        ],
      },
      options: {
        responsive: true,
      },
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
