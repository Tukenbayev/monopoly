import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMonopolist, Monopolist } from '../monopolist.model';
import { MonopolistService } from '../service/monopolist.service';
import { LocalStorageService } from 'ngx-webstorage';

@Component({
  selector: 'jhi-monopolist-update',
  templateUrl: './monopolist-update.component.html',
})
export class MonopolistUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [null, [Validators.required]],
    name: [null, [Validators.required]],
    is_bank: [],
  });

  constructor(
    protected monopolistService: MonopolistService,
    private localStorage: LocalStorageService,
    private router: Router,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    if (this.localStorage.retrieve('player') != null) {
      this.router.navigate(['/monopolist/view']);
    }
    this.updateForm(new Monopolist());
  }

  save(): void {
    this.isSaving = true;
    const monopolist = this.createFromForm();
    this.subscribeToSaveResponse(this.monopolistService.login(monopolist));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMonopolist>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      res => this.onSaveSuccess(res),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(res: any): void {
    this.localStorage.store('player', res.body);
    this.router.navigate(['/monopolist/view']);
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(monopolist: IMonopolist): void {
    this.editForm.patchValue({
      id: monopolist.id,
      name: monopolist.name,
      is_bank: monopolist.isBank,
    });
  }

  protected createFromForm(): any {
    return {
      ...{},
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      isBank: this.editForm.get(['is_bank'])!.value,
    };
  }
}
