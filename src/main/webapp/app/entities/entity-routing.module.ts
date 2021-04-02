import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'monopolist',
        data: { pageTitle: 'Monopolists' },
        loadChildren: () => import('./monopolist/monopolist.module').then(m => m.MonopolistModule),
      },
      {
        path: 'transaction',
        data: { pageTitle: 'Transactions' },
        loadChildren: () => import('./transaction/transaction.module').then(m => m.TransactionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
