import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import SharedModule from 'app/shared/shared.module'; // Correction de l'import de SharedModule
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
  imports: [SharedModule, RouterModule], // Correction de l'import de SharedModule
})
export class HomeComponent implements OnInit, OnDestroy {
  resolvedTicketsPercentage: number = 0;
  recentTickets: ITicket[] = [];
  account: Account | null = null;
  showSuccessAlert = true;
  private readonly destroy$ = new Subject<void>();

  constructor(
    private accountService: AccountService,
    private ticketService: TicketService,
    private router: Router,
  ) {}

  // Correction : Déclaration des fonctions avant leur utilisation

  ngOnInit(): void {
    // Abonnement à l'état d'authentification
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe((account: Account | null) => {
        this.account = account;
        this.showSuccessAlert = !!account;
        if (account?.login) {
          this.loadResolvedTicketsPercentage(account.login);
        }
      });
    this.loadRecentTickets(); // Chargement des tickets récents
    this.loadTicketsCountByPriority(); // Chargement des tickets par priorité
  }

  loadResolvedTicketsPercentage(username: string): void {
    this.ticketService.getResolvedTicketsPercentage(username).subscribe(
      percentage => {
        this.resolvedTicketsPercentage = percentage;
        this.updateDoughnutChart();
      },
      error => this.onError(error),
    );
  }

  loadTicketsCountByPriority(): void {
    if (this.account?.login) {
      this.ticketService.getTicketsCountByPriorityForUser(this.account.login).subscribe(
        (res: HttpResponse<unknown[]>) => {
          const priorityData = res.body ?? [];
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

  initializeDoughnutChart(): void {
    const ctxD = document.getElementById('doughnutChart1') as HTMLCanvasElement;
    const myDoughnutChart = new Chart(ctxD, {
      type: 'doughnut',
      data: {
        labels: ['Résolus', 'Non Résolus'],
        datasets: [
          {
            data: [0, 100],
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

  updateDoughnutChart(): void {
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
              label(context: any) {
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

  updatePriorityDoughnutChart(priorityData: PriorityData[]): void {
    const labels = priorityData.map(item => item.priorityName);
    const priorityCounts = priorityData.map(item => item.ticketCount);

    const ctxD = document.getElementById('doughnutChart2') as HTMLCanvasElement;
    const myDoughnutChart = new Chart(ctxD, {
      type: 'doughnut',
      data: {
        labels,
        datasets: [
          {
            data: priorityCounts, // Utilisez ici la variable 'priorityCounts'
            backgroundColor: ['#ADD8E6', '#FFD700', '#FFA500', '#FF4500', '#90EE90', '#D3D3D3'],
            hoverBackgroundColor: ['#B0E0E6', '#FFDAB9', '#FFA07A', '#FA8072', '#98FB98', '#C0C0C0'],
          },
        ],
      },
      options: {
        responsive: true,
        aspectRatio: 2.5,
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

  loadRecentTickets(): void {
    this.ticketService.queryRecent().subscribe({
      next: (res: HttpResponse<ITicket[]>) => {
        this.recentTickets = res.body ?? [];
      },
      error: (res: HttpErrorResponse) => this.onError(res.message),
    });
  }

  private onError(errorMessage: string): void {
    console.error(errorMessage);
  }
}
