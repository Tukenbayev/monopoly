import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IMonopolist } from '../monopolist.model';
import { ListItem } from 'app/entities/monopolist/list-item.model';

export type EntityResponseType = HttpResponse<IMonopolist>;

@Injectable({ providedIn: 'root' })
export class MonopolistService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/monopoly');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  login(monopolist: any): Observable<EntityResponseType> {
    return this.http.post<IMonopolist>(`${this.resourceUrl}/login`, monopolist, { observe: 'response' });
  }

  getInfo(id: number): Observable<EntityResponseType> {
    return this.http.get<IMonopolist>(`${this.resourceUrl}/get-info/${id}`, { observe: 'response' });
  }

  getList(except: number): Observable<HttpResponse<ListItem[]>> {
    return this.http.get<ListItem[]>(`${this.resourceUrl}/get-list/${except}`, { observe: 'response' });
  }

  sendMoney(transaction: any): Observable<HttpResponse<any>> {
    return this.http.post<IMonopolist>(`${this.resourceUrl}/send-money`, transaction, { observe: 'response' });
  }
}
