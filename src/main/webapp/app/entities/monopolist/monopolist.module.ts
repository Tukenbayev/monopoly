import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { MonopolistDetailComponent } from './detail/monopolist-detail.component';
import { MonopolistUpdateComponent } from './update/monopolist-update.component';
import { MonopolistRoutingModule } from './route/monopolist-routing.module';

@NgModule({
  imports: [SharedModule, MonopolistRoutingModule],
  declarations: [MonopolistDetailComponent, MonopolistUpdateComponent],
  entryComponents: [],
})
export class MonopolistModule {}
