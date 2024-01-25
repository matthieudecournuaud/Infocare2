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
interface PriorityData {
  priorityName: string;
  ticketCount: number;
}
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
    this.loadTicketsCountByPriority();
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => {
        this.account = account;
        this.showSuccessAlert = !!account;
        if (account && account.login) {
          this.loadResolvedTicketsPercentage(account.login);
          this.loadTicketsCountByPriority();
        }
      });
  }

  initializeDoughnutChart() {
    const ctxD = document.getElementById('doughnutChart1') as HTMLCanvasElement;
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

  updateDoughnutChart() {
    const ctxD = document.getElementById('doughnutChart1') as HTMLCanvasElement;
    const myLineChart = new Chart(ctxD, {
      type: 'doughnut',
      data: {
        labels: ['Résolus', 'Non Résolus'],
        datasets: [
          {
            data: [this.resolvedTicketsPercentage, 100 - this.resolvedTicketsPercentage],
            backgroundColor: ['#46BFBD', '#ED3446'],
            hoverBackgroundColor: ['#5AD3D1', '#D9E3F1'],
          },
        ],
      },
      options: {
        responsive: true,
        aspectRatio: 2.2,
        cutout: '85%',
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

  updatePriorityDoughnutChart(priorityData: PriorityData[]) {
    const labels = priorityData.map(data => data.priorityName); // Noms des priorités
    const data = priorityData.map(data => data.ticketCount);

    const ctxD = document.getElementById('doughnutChart2') as HTMLCanvasElement;
    const myDoughnutChart = new Chart(ctxD, {
      type: 'doughnut',
      data: {
        labels: labels,
        datasets: [
          {
            data: data,
            backgroundColor: ['#ADD8E6', '#FFD700', '#FFA500', '#FF4500', '#90EE90', '#D3D3D3'],
            hoverBackgroundColor: ['#B0E0E6', '#FFDAB9', '#FFA07A', '#FA8072', '#98FB98', '#C0C0C0'],
          },
        ],
      },
      options: {
        responsive: true,
        aspectRatio: 2.2,
        cutout: '85%',
        plugins: {
          legend: {
            position: 'bottom',
            labels: {
              padding: 14,
            },
          },
        },
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

  loadResolvedTicketsPercentage(username: string) {
    this.ticketService.getResolvedTicketsPercentage(username).subscribe(
      percentage => {
        this.resolvedTicketsPercentage = percentage;
        this.updateDoughnutChart();
      },
      error => this.onError(error),
    );
  }

  loadTicketsCountByPriority(): void {
    if (this.account && this.account.login) {
      this.ticketService.getTicketsCountByPriorityForUser(this.account.login).subscribe(
        (res: HttpResponse<Object[]>) => {
          const priorityData = res.body || [];
          const formattedData: PriorityData[] = priorityData.map((data: any) => ({
            priorityName: data[0],
            ticketCount: data[1],
          }));
          this.updatePriorityDoughnutChart(formattedData);
        },
        error => this.onError(error),
      );
    }
  }
}
