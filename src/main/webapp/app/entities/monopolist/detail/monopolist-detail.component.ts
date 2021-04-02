import { Component, OnInit } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { Router } from '@angular/router';

import { IMonopolist } from '../monopolist.model';
import { MonopolistService } from 'app/entities/monopolist/service/monopolist.service';
import { ListItem } from 'app/entities/monopolist/list-item.model';
import { TransactionWsService } from 'app/entities/transaction/service/transaction-ws.service';

@Component({
  selector: 'jhi-monopolist-detail',
  templateUrl: './monopolist-detail.component.html',
})
export class MonopolistDetailComponent implements OnInit {
  monopolist: IMonopolist | null = null;
  public activeId = 2;
  public amount = '0';
  public amountNum?: number | null;
  public players: ListItem[] | null = null;
  public toPlayer: ListItem | null = null;

  constructor(
    protected localStorage: LocalStorageService,
    private monopolistService: MonopolistService,
    private wsService: TransactionWsService,
    protected router: Router
  ) {}

  ngOnInit(): void {
    this.monopolist = this.localStorage.retrieve('player');
    if (!this.monopolist) {
      this.router.navigate(['/monopolist/new']);
    } else {
      if (this.monopolist.id != null) {
        this.monopolistService.getInfo(this.monopolist.id).subscribe(
          res => {
            this.monopolist = res.body;
            this.wsService.connect();
            this.wsService.subscribe(this.monopolist?.id);
            this.wsService.receive().subscribe(obs => {
              this.monopolist?.transactions?.unshift(obs);
              if (obs.to_id === this.monopolist?.id) {
                // @ts-ignore
                this.monopolist?.balance += obs.amount;
              } else {
                // @ts-ignore
                this.monopolist?.balance -= obs.amount;
              }
            });
          },
          () => {
            this.router.navigate(['/']);
          }
        );

        this.monopolistService.getList(this.monopolist.id).subscribe(res => {
          this.players = res.body;
        });
      }
    }
  }

  clickNumber(num: string): void {
    if (this.amount === '0') {
      this.amount = num;
    } else {
      this.amount += num;
    }
  }

  drop(): void {
    this.amount = '0';
    this.amountNum = null;
  }

  multiply(num: number): void {
    if (this.amount !== '0') {
      this.amountNum = +this.amount * num;
      this.amount = this.amountNum.toString();
    }
  }

  sendMoney(): void {
    if (this.amountNum != null && this.toPlayer != null) {
      // @ts-ignore
      if (this.monopolist?.balance > this.amountNum) {
        this.monopolistService
          .sendMoney({ fromId: this.monopolist?.id, toId: this.toPlayer.id, amount: this.amountNum })
          .subscribe(() => (this.activeId = 1));
      }
    }
  }
}
