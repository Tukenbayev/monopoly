import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { MonopolistDetailComponent } from '../detail/monopolist-detail.component';
import { MonopolistUpdateComponent } from '../update/monopolist-update.component';

const monopolistRoute: Routes = [
  {
    path: 'view',
    component: MonopolistDetailComponent,
    resolve: {},
  },
  {
    path: 'new',
    component: MonopolistUpdateComponent,
    resolve: {},
  },
];

@NgModule({
  imports: [RouterModule.forChild(monopolistRoute)],
  exports: [RouterModule],
})
export class MonopolistRoutingModule {}
